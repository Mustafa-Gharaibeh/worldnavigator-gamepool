package com.worldnavigator.gamepoolservice.pool.controler.command.wall.lockable.opencommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;

import static com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall.*;

public final class OpenCommand implements Command {
  private final OnWall onWall;
  private final GamerWrapper gamerWrapper;

  private OpenCommand(OnWall onWall, GamerWrapper gamerWrapper) {
    this.onWall = onWall;
    this.gamerWrapper = gamerWrapper;
  }

  public static OpenCommand createOpenCommand(OnWall onWall, GamerWrapper gamerWrapper) {
    return new OpenCommand(onWall, gamerWrapper);
  }

  @Override
  public void execute() {
    if (this.onWall.typeOnWall().equals(DOOR)) {
      gamerWrapper.setLastListCommandUsed(DOOR.getCheckName());
      gamerWrapper.setStatus(onWall.getLockable().status());
    } else {
      gamerWrapper.setStatus("Incorrect command");
      gamerWrapper.setLastListCommandUsed("default");
    }
  }
}
