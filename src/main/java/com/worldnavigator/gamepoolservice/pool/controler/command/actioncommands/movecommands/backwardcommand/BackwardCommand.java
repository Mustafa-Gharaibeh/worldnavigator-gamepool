package com.worldnavigator.gamepoolservice.pool.controler.command.actioncommands.movecommands.backwardcommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import com.worldnavigator.gamepoolservice.pool.model.room.Room;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.door.Door;

import java.util.Optional;

public final class BackwardCommand implements Command {
  private final Maze maze;
  private final GamerWrapper gamerWrapper;

  private BackwardCommand(Maze maze, GamerWrapper gamerWrapper) {
    this.maze = maze;
    this.gamerWrapper = gamerWrapper;
  }

  public static BackwardCommand createBackwardCommand(Maze maze, GamerWrapper gamer) {
    return new BackwardCommand(maze, gamer);
  }

  @Override
  public void execute() {
    String defaultListCommand = "default";
    if (this.getOnWall().typeOnWall().isLockable()) {
      if (this.getOnWall().typeOnWall().equals(TypeOnWall.DOOR)) {
        if (this.getOnWall().getLockable().isLock()) {
          gamerWrapper.setStatus("The Door is Lock");
          gamerWrapper.setLastListCommandUsed("backward");
        } else if (this.getOppositeDirectionRoom().isPresent()) {
          this.getOppositeDirectionRoom()
              .ifPresent(room -> maze.setCurrentRoom(gamerWrapper.getGamer(), room));
          gamerWrapper.setStatus(
              String.format(
                  "You in %s", this.maze.getCurrentRoom(gamerWrapper.getGamer()).getRoomName()));
          gamerWrapper.setLastListCommandUsed(defaultListCommand);
        }
      } else {
        gamerWrapper.setStatus(
            String.format(
                "You can not use Backward Command %s", "because the Opposite wall has not Door"));
        gamerWrapper.setLastListCommandUsed(defaultListCommand);
      }
    } else {
      gamerWrapper.setStatus(
          String.format(
              "You can not use Backward Command %s", "because the Opposite wall has not Door"));
      gamerWrapper.setLastListCommandUsed(defaultListCommand);
    }
  }

  private OnWall getOnWall() {
    return this.maze
        .getCurrentRoom(gamerWrapper.getGamer())
        .getWalls()
        .get(this.gamerWrapper.getGamer().getOppositeDirection())
        .getOnWall();
  }

  private Optional<Room> getOppositeDirectionRoom() {
    for (Room room :
        this.maze.getMazeGraph().adj(this.maze.getCurrentRoom(gamerWrapper.getGamer()))) {
      OnWall onWall =
          room.getWalls().get(this.gamerWrapper.getGamer().getCurrentDirection()).getOnWall();
      if (onWall.typeOnWall().equals(TypeOnWall.DOOR)
          && ((Door) getOnWall()).getDoorName().equals(((Door) onWall).getDoorName())) {
        return Optional.of(room);
      }
    }
    return Optional.empty();
  }
}
