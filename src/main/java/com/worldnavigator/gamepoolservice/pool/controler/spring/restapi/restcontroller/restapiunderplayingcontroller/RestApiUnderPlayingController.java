package com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restcontroller.restapiunderplayingcontroller;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.commandfactory.CommandFactory;
import com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restclass.restcommunicationbackend.RestCommunicationBackEnd;
import com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restclass.restcommunicationbackend.RestCommunicationBackEndBuilder;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.GameService;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.worldnavigator.gamepoolservice.pool.controler.spring.service.commandpoolutil.CommandPoolUtil.commandList;

@RestController
@RequestMapping("mazes/")
public class RestApiUnderPlayingController {

  private final GameService gameService;

  public RestApiUnderPlayingController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("/action")
  public RestCommunicationBackEnd getAction(
      @RequestBody RestCommunicationBackEnd restCommunicationBackEnd) {
    final RestCommunicationBackEndBuilder communicationBuilder =
        new RestCommunicationBackEndBuilder();

    final String gamerName = restCommunicationBackEnd.getGamerName();
    communicationBuilder.setGamerName(gamerName);

    final GamerWrapper gamerWrapper = gameService.getGamerWrapper(gamerName);

    final String mazeId = gamerWrapper.getGamer().getCurrentMazeId();
    communicationBuilder.setMazeId(mazeId);
    gamerWrapper.getGamer().setCurrentMazeId(mazeId);

    final Maze maze = gameService.getMaze(mazeId);

    final String mazeName = maze.getMazeName();
    communicationBuilder.setMazeName(mazeName);

    final String input = restCommunicationBackEnd.getCommunicationVariable();
    final String commandListName = gamerWrapper.getLastListCommandUsed();

    if (enterCheck(communicationBuilder, gamerName, mazeId, mazeName, input, commandListName)) {
      return communicationBuilder.createRestCommunicationBackEnd();
    }

    final CommandFactory commandFactory = CommandFactory.createCommandFactory(maze, gamerWrapper);
    final Command command = commandFactory.createCommand(input);
    command.execute();
    gamerWrapper.getGamer().setCurrentMazeId(mazeId);
    gameService.leaveGamerAndMaze(gamerWrapper, maze);
    communicationBuilder.setCommandListName(gamerWrapper.getLastListCommandUsed());
    communicationBuilder.setCommunicationVariable(gamerWrapper.getStatus());
    return communicationBuilder.createRestCommunicationBackEnd();
  }

  private synchronized boolean enterCheck(
      RestCommunicationBackEndBuilder communicationBuilder,
      String gamerName,
      String mazeId,
      String mazeName,
      String communicationVariable,
      String commandListName) {
    if (isInCorrectCommand(communicationBuilder, communicationVariable, commandListName)) {
      return true;
    }

    if (isTheGameDidNotStart(communicationBuilder, gamerName, mazeId, mazeName)) {
      return true;
    }

    final GamerWrapper gamerWrapper = gameService.getGamerWrapper(gamerName);
    final Maze maze = gameService.getMaze(mazeId);

    if (isGameDone(communicationBuilder, gamerName, gamerWrapper, maze)) {
      return true;
    }

    if (gameService.isGamerEnd(gamerName) || gameService.isGamerLost(gamerName)) {
      communicationBuilder.setCommunicationVariable(gameService.lostDescription(gamerName));
    }
    if (gameService.isUnderRockPaperScissor(gamerName)
        && rockPaperScissorAction(communicationBuilder, gamerName, gamerWrapper)) {
      return true;
    }

    Optional<String> isAnotherOneInSameRoom =
        gameService.isAnotherGamerInSameRoom(gamerWrapper.getGamer(), maze);
    if (isAnotherOneInSameRoom.isPresent()) {
      final String rockPaperScissor = "RockPaperScissor";
      if (isAnotherOneInSameRoom.get().equals(rockPaperScissor)) {
        communicationBuilder.setCommunicationVariable("Your are under Rock-Paper-Scissor");
        communicationBuilder.setCommandListName(rockPaperScissor);
        gameService.setListCommand(gamerName, rockPaperScissor);
      } else {
        communicationBuilder.setCommunicationVariable(isAnotherOneInSameRoom.get());
        communicationBuilder.setCommandListName("default");
        gameService.setListCommand(gamerName, "default");
      }
      return true;
    }
    return false;
  }

  private boolean rockPaperScissorAction(
      RestCommunicationBackEndBuilder communicationBuilder,
      String gamerName,
      GamerWrapper gamerWrapper) {
    if (gameService.isCanPlayUnderRockPaperScissor(gamerName, gamerWrapper.getGamerFighterName())) {
      String result =
          gameService.rockPaperScissorMode(gamerName, gamerWrapper.getGamerFighterName());
      if (result.equals("SYMMETRY") || result.equals("WRONG")) {
        communicationBuilder.setCommunicationVariable(
            String.format("The Result is %s you will enter your choice again.", result));
        communicationBuilder.setCommandListName("RockPaperScissor");
        gameService.setListCommand(gamerName, "RockPaperScissor");
        return true;
      } else if (findFirst(result, gamerName, gamerWrapper.getGamerFighterName())
          .equals(gamerName)) {
        communicationBuilder.setCommunicationVariable(result);
        communicationBuilder.setCommandListName("default");
        gameService.setListCommand(gamerName, "default");
        return true;
      } else {
        communicationBuilder.setCommunicationVariable(result);
        communicationBuilder.setCommandListName("");
        gameService.setListCommand(gamerName, "");
      }
    }
    return false;
  }

  private String findFirst(String result, String gamerName, String gamerFighterName) {
    String[] seq = result.split("\\s");
    for (String s : seq) {
      if (s.equals(gamerName) || s.equals(gamerFighterName)) {
        return s;
      }
    }
    return "null";
  }

  private synchronized boolean isGameDone(
      RestCommunicationBackEndBuilder communicationBuilder,
      String gamerName,
      GamerWrapper gamerWrapper,
      Maze maze) {
    if (gameService.isDone(gamerWrapper, maze)) {
      communicationBuilder.setCommunicationVariable(gameService.finishGameDescription(gamerName));
      communicationBuilder.setCommandListName("endMessage");
      gameService.setListCommand(gamerName, "endMessage");
      return true;
    }
    return false;
  }

  private synchronized boolean isTheGameDidNotStart(
      RestCommunicationBackEndBuilder communicationBuilder,
      String gamerName,
      String mazeId,
      String mazeName) {
    if (!gameService.isStart(mazeId)) {
      communicationBuilder.setCommunicationVariable(gameService.getTimeToStartPlaying(mazeName));
      communicationBuilder.setCommandListName("isStart");
      gameService.setListCommand(gamerName, "isStart");
      return true;
    }
    return false;
  }

  private synchronized boolean isInCorrectCommand(
      RestCommunicationBackEndBuilder communicationBuilder,
      String communicationVariable,
      String commandListName) {
    if (isInCorrectCommand(communicationVariable, commandListName)) {
      communicationBuilder.setCommandListName(commandListName);
      communicationBuilder.setCommunicationVariable("Incorrect Command");
      return true;
    }
    return false;
  }

  private synchronized boolean isInCorrectCommand(String command, String commandListName) {
    List<String> commands = commandList(commandListName);
    if (command.contains("use")) {
      if (command.contains("key") && commands.contains("use <name> key")) {
        return false;
      } else if (command.contains("flashlight") && commands.contains("use flashlight")) {
        return false;
      }
    }
    if (command.contains("buy") && commands.contains("buy <item>")) {
      return false;
    }
    if (command.contains("sell") && commands.contains("sell <item>")) {
      return false;
    }
    return !commands.contains(command);
  }
}
