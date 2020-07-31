package com.worldnavigator.gamepoolservice.pool.controler.command.actioncommands.movecommands.forwardcommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import com.worldnavigator.gamepoolservice.pool.model.room.Room;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.door.Door;

import java.util.Objects;
import java.util.Optional;

import static com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall.DOOR;

public final class ForwardCommand implements Command {
  private final Maze maze;
  private final GamerWrapper gamerWrapper;

  private ForwardCommand(Maze maze, GamerWrapper gamerWrapper) {
    this.maze = maze;
    this.gamerWrapper = gamerWrapper;
  }

  public static ForwardCommand createForwardCommand(Maze maze, GamerWrapper gamerWrapper) {
    return new ForwardCommand(Objects.requireNonNull(maze), Objects.requireNonNull(gamerWrapper));
  }

  @Override
  public void execute() {
    String defaultCommandList = "default";
    if (this.getOnWall().typeOnWall().isLockable()) {
      if (this.getOnWall().typeOnWall().equals(DOOR)) {
        if (this.getOnWall().getLockable().isLock()) {
          gamerWrapper.setStatus("The Door is Lock");
          gamerWrapper.setLastListCommandUsed(defaultCommandList);
        } else if (this.getNextRoom().isPresent()) {
          this.getNextRoom()
              .ifPresent(room -> maze.setCurrentRoom(this.gamerWrapper.getGamer(), room));
          gamerWrapper.setStatus(
              String.format(
                  "You in %s", this.maze.getCurrentRoom(gamerWrapper.getGamer()).getRoomName()));
          gamerWrapper.setLastListCommandUsed(defaultCommandList);
        }
      } else {
        gamerWrapper.setStatus(
            String.format(
                "You can not use Forward Command %s", "because the front wall has not Door"));
        gamerWrapper.setLastListCommandUsed(defaultCommandList);
      }
    } else {
      gamerWrapper.setStatus(
          String.format(
              "You can not use Forward Command %s", "because the front wall has not Door"));
      gamerWrapper.setLastListCommandUsed(defaultCommandList);
    }
  }

  private OnWall getOnWall() {
    return this.maze
        .getCurrentRoom(gamerWrapper.getGamer())
        .getWalls()
        .get(this.gamerWrapper.getGamer().getCurrentDirection())
        .getOnWall();
  }

  private Optional<Room> getNextRoom() {
    for (Room room :
        this.maze.getMazeGraph().adj(this.maze.getCurrentRoom(gamerWrapper.getGamer()))) {
      OnWall onWall =
          room.getWalls().get(this.gamerWrapper.getGamer().getOppositeDirection()).getOnWall();
      if (onWall.typeOnWall().equals(DOOR)
          && ((Door) getOnWall()).getDoorName().equals(((Door) onWall).getDoorName())) {
        return Optional.of(room);
      }
    }
    return Optional.empty();
  }
}
