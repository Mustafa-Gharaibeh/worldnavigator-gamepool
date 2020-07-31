package com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restcontroller.startgamecontroller;

import com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restclass.mazelistwrapper.MazeListWrapper;
import com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restclass.restcommunicationbackend.RestCommunicationBackEnd;
import com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restclass.restcommunicationbackend.RestCommunicationBackEndBuilder;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.GameService;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mazes")
public class StartGameController {

  private final GameService gameService;

  public StartGameController(GameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping("/mazeList")
  public MazeListWrapper mazeList() {
    return new MazeListWrapper(gameService.mazeList());
  }

  @PostMapping("/register")
  public RestCommunicationBackEnd gamerRegister(@RequestBody RestCommunicationBackEnd backEnd) {

    final String gamerName = backEnd.getGamerName();
    final String mazeName = backEnd.getMazeName();
    final RestCommunicationBackEndBuilder backEndBuilder = new RestCommunicationBackEndBuilder();
    try {
      if (gameService.gamerRegister(mazeName, gamerName)) {
        final GamerWrapper gamerWrapper = gameService.getGamerWrapper(gamerName);
        if (!gameService.isStart(gamerWrapper.getGamer().getCurrentMazeId())) {
          gamerWrapper.setStatus(
              String.format(
                  "You are in %s and you will %s ",
                  gamerWrapper.getGamer().getCurrentMazeId(),
                  gameService.getTimeToStartPlaying(mazeName)));
          gamerWrapper.setLastListCommandUsed("isStart");
        } else {
          Maze maze = gameService.getMaze(gamerWrapper.getGamer().getCurrentMazeId());
          gamerWrapper.setStatus(
              String.format(
                  "You are in %s", maze.getCurrentRoom(gamerWrapper.getGamer()).getRoomName()));
          gamerWrapper.setLastListCommandUsed("default");
        }
        backEndBuilder
            .setCommunicationVariable(gamerWrapper.getStatus())
            .setCommandListName(gamerWrapper.getLastListCommandUsed())
            .setGamerName(gamerName)
            .setMazeId(gamerWrapper.getGamer().getCurrentMazeId())
            .setMazeName(mazeName);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return backEndBuilder.createRestCommunicationBackEnd();
  }
}
