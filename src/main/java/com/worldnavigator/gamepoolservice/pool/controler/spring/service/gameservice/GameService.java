package com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice;

import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.rockpaperscissor.RockPaperScissor;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.mazeservice.MazeService;
import com.worldnavigator.gamepoolservice.pool.model.gamer.Gamer;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import com.worldnavigator.gamepoolservice.pool.model.room.Room;
import com.worldnavigator.gamepoolservice.pool.model.wall.Wall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.door.Door;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.worldnavigator.gamepoolservice.pool.controler.spring.service.commandpoolutil.CommandPoolUtil.commandList;

@Service
public class GameService {

  private final MazeService mazeService;
  private final ConcurrentMap<String, GamerWrapper> gamers;
  private final ConcurrentMap<String, String> mazesEndDescription;
  private final ConcurrentMap<String, String> lostDescription;

  public GameService(MazeService mazeService) {
    this.mazeService = mazeService;
    gamers = new ConcurrentHashMap<>();
    mazesEndDescription = new ConcurrentHashMap<>();
    lostDescription = new ConcurrentHashMap<>();
  }

  public List<String> mazeList() {
    return mazeService.mazeList();
  }

  public boolean gamerRegister(final String mazeName, final String gamerName) {
    GamerWrapper gamerWrapper =
        new GamerWrapper(
            mazeService.gamerRegister(
                Objects.requireNonNull(mazeName).trim(), Objects.requireNonNull(gamerName).trim()));

    gamerWrapper.setIsGamerInMaze(Boolean.TRUE);
    return Objects.isNull(gamers.put(gamerName.trim(), gamerWrapper));
  }

  public Gamer getGamer(String gamerName) {
    return gamers.get(Objects.requireNonNull(gamerName).trim()).getGamer();
  }

  public Maze getMaze(String mazeId) {
    return mazeService.getMaze(Objects.requireNonNull(mazeId).trim());
  }

  public synchronized boolean isDone(final GamerWrapper gamer, final Maze maze) {
    boolean isDone;
    Wall frontWall = frontWall(gamer.getGamer(), maze);
    isDone = mazeService.isTimeUp(maze.getMazeId()) && isFinalDoorAndOpen(frontWall);
    if (isDone && !mazeService.isTimeUp(maze.getMazeId())) {
      mazesEndDescription.put(gamer.getGamer().getGamerName(), String.format("%s Is Win", gamer));
      mazeEndSettings(gamer, maze);
    } else if (mazeService.isTimeUp(maze.getMazeId())) {
      mazesEndDescription.put(gamer.getGamer().getGamerName(), "Time Up");
mazeEndSettings(gamer, maze);
    }
    return isDone;
  }

  public synchronized boolean isStart(String mazeId) {
    return mazeService.isStart(mazeId);
  }

  public String finishGameDescription(final String gamerName) {
    return mazesEndDescription.remove(Objects.requireNonNull(gamerName));
  }

  private void mazeEndSettings(GamerWrapper gamer, Maze maze) {
    gamer.getGamer().setCurrentMazeId("");
    gamer.setIsGamerInMaze(Boolean.FALSE);
    mazeService.close(maze.getMazeId());
  }

  private Wall frontWall(Gamer gamer, Maze maze) {
    Room room = maze.getCurrentRoom(gamer);
    return room.getWalls().get(gamer.getCurrentDirection());
  }

  private boolean isFinalDoorAndOpen(Wall wall) {
    OnWall onWall = wall.getOnWall();
    return TypeOnWall.DOOR.equals(onWall.typeOnWall())
        && ((Door) onWall).getIsFinalDoor()
        && !onWall.getLockable().isLock();
  }

  public synchronized Optional<String> isAnotherGamerInSameRoom(
      final Gamer gamer, final Maze maze) {
    Map<String, Gamer> gamerMap = maze.getGamers();
    for (Map.Entry<String, Gamer> entry : gamerMap.entrySet()) {
      if (gamer.getGamerName().equals(entry.getKey())) {
        continue;
      }
      if (maze.getCurrentRoom(entry.getValue())
          .getRoomName()
          .equals(maze.getCurrentRoom(gamer).getRoomName())) {
        return Optional.of(fighting(entry.getValue(), gamer));
      }
    }
    return Optional.empty();
  }

  private String fighting(Gamer gamer1, Gamer gamer2) {
    // TODO Create Good performance for getTotalAmountOfGoldWithItems() method
    Integer gamer1Gold = gamer1.getTotalAmountOfGoldWithItems();
    Integer gamer2Gold = gamer2.getTotalAmountOfGoldWithItems();
    int fightingLogic = gamer1Gold.compareTo(gamer2Gold);
    String description = "The Winner is ";
    if (fightingLogic > 0) {
      description = getFightDescription(gamer1, gamer2, gamer1Gold, description);
    } else if (fightingLogic < 0) {
      description = getFightDescription(gamer2, gamer1, gamer1Gold, description);
    } else {
      // TODO rockPaperScissor()
      gamers.get(gamer1.getGamerName()).setIsUnderRockPaperScissor(Boolean.TRUE);
      gamers.get(gamer1.getGamerName()).setGamerFighterName(gamer2.getGamerName());
      gamers.get(gamer2.getGamerName()).setIsUnderRockPaperScissor(Boolean.TRUE);
      gamers.get(gamer2.getGamerName()).setGamerFighterName(gamer1.getGamerName());
      description = "RockPaperScissor";
    }
    return description;
  }

  private String getFightDescription(
      Gamer win, Gamer loser, Integer loserGold, String description) {
    description = String.format("%s %s and get %d", description, win.getGamerName(), loserGold);
    win.addAmountOfGold(loserGold);
    mazeService.getMaze(loser.getCurrentMazeId()).removeGamer(loser);
    gamers.remove(loser.getGamerName());
    lostDescription.put(
        loser.getGamerName(),
        String.format("You Fight With %s and lost you are out of Game", loser.getGamerName()));
    return description;
  }

  public synchronized String rockPaperScissorMode(
      final String gamer1Name, final String gamer2Name) {
    Gamer gamer1 = gamers.get(gamer1Name).getGamer();
    Gamer gamer2 = gamers.get(gamer2Name).getGamer();
    RockPaperScissor rockPaperScissor =
        new RockPaperScissor(gamers.get(gamer1Name), gamers.get(gamer2Name));
    String result = rockPaperScissor.play();
    if (result.equals("SYMMETRY") || result.equals("WRONG")) {
      return result;
    } else if (result.equals(gamer1.getGamerName()) || result.equals(gamer2.getGamerName())) {
      gamers
          .get(gamers.get(gamer1Name).getGamer().getGamerName())
          .setIsUnderRockPaperScissor(Boolean.FALSE);
      gamers.get(gamers.get(gamer1Name).getGamer().getGamerName()).setGamerFighterName("Nobody");
      gamers
          .get(gamers.get(gamer2Name).getGamer().getGamerName())
          .setIsUnderRockPaperScissor(Boolean.FALSE);
      gamers.get(gamers.get(gamer2Name).getGamer().getGamerName()).setGamerFighterName("Nobody");
      if (result.equals(gamer1.getGamerName())) {
        return getFightDescription(
            gamer1,
            gamer2,
            gamer2.getTotalAmountOfGoldWithItems(),
            "In Rock-Paper-Scissor The Winner is ");
      } else {
        return getFightDescription(
            gamer2,
            gamer1,
            gamer1.getTotalAmountOfGoldWithItems(),
            "In Rock-Paper-Scissor The Winner is ");
      }
    }
    return result;
  }

  public synchronized String lostDescription(String gamer) {
    return lostDescription.remove(gamer.trim());
  }

  public synchronized boolean isGamerLost(String gamerName) {
    return lostDescription.containsKey(gamerName);
  }

  public synchronized boolean isGamerUnderGame(String gamerName) {
    return gamers.containsKey(gamerName.trim());
  }

  public synchronized boolean isUnderRockPaperScissor(String gamerName) {
    return gamers.get(gamerName.trim()).getIsUnderRockPaperScissor();
  }

  public synchronized boolean isCanPlayUnderRockPaperScissor(String gamer1Name, String gamer2Name) {
    return Objects.nonNull(gamers.get(gamer1Name).getRockPaperScissorChoice())
        && Objects.nonNull(gamers.get(gamer2Name).getRockPaperScissorChoice());
  }

  public synchronized boolean isCorrectCommand(String gamerName, String command) {
    List<String> commandList = commandList(gamers.get(gamerName).getLastListCommandUsed());
    if (command.contains("use") && command.contains("key")) {
      return commandList.contains("use <name> key");
    } else if (command.contains("use") && command.contains("flashlight")) {
      return commandList.contains("use flashlight");
    }
    return commandList.contains(command);
  }

  public synchronized void setListCommand(String gamerName, String commands) {
    gamers.get(gamerName).setLastListCommandUsed(commands);
  }

  public synchronized boolean isGamerEnd(String gamerName) {
    return this.mazesEndDescription.containsKey(gamerName);
  }

  public synchronized String getGamerFighterName(String gamerName) {
    return gamers.get(gamerName).getGamerFighterName();
  }

  public synchronized GamerWrapper getGamerWrapper(String gamerName) {
    return gamers.get(gamerName);
  }

  public synchronized String getTimeToStartPlaying(String mazeName) {
    return mazeService.getTimeToStartPlaying(mazeName);
  }

  public synchronized void leaveGamerAndMaze(GamerWrapper gamerWrapper, Maze maze) {
    this.gamers.put(gamerWrapper.getGamer().getGamerName(), gamerWrapper);
    mazeService.leaveMaze(maze);
  }
}
