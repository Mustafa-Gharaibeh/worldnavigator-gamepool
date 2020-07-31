package com.worldnavigator.gamepoolservice.pool.controler.command.wall.checkcommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.acquire.Acquirable;

import java.util.Map;
import java.util.Objects;

public final class CheckCommand implements Command {
  private final OnWall onWall;
  private final GamerWrapper gamerWrapper;
  private final String arg;

  private CheckCommand(OnWall onWall, GamerWrapper gamerWrapper, String arg) {
    this.onWall = onWall;
    this.gamerWrapper = gamerWrapper;
    this.arg = arg;
  }

  public static CheckCommand createCheckCommand(OnWall onWall, GamerWrapper gamer, String arg) {
    return new CheckCommand(
        Objects.requireNonNull(onWall), Objects.requireNonNull(gamer), Objects.requireNonNull(arg));
  }

  @Override
  public void execute() {
    if (this.arg.contains(this.onWall.typeOnWall().getCheckName())) {
      if (this.onWall.typeOnWall().isLockable()) {
        if (this.onWall.typeOnWall().isAcquirable() && !this.onWall.getLockable().isLock()) {
          this.acquire(this.onWall.getAcquirable(), onWall.typeOnWall().getCheckName());
        } else {
          gamerWrapper.setStatus(this.onWall.getLockable().status());
          gamerWrapper.setLastListCommandUsed(onWall.typeOnWall().getCheckName());
        }
      } else if (this.onWall.typeOnWall().isAcquirable()) {
        this.acquire(this.onWall.getAcquirable(), onWall.typeOnWall().getCheckName());
      }
    } else {
      gamerWrapper.setStatus("Incorrect arg Command");
      gamerWrapper.setLastListCommandUsed("default");
    }
  }

  private void acquire(Acquirable acquirable, String commandListName) {
    StringBuilder sd = new StringBuilder();
    for (Map.Entry<String, Material> entry : acquirable.acquire().entrySet()) {
      if (entry.getKey().equals("Gold")) {
        this.gamerWrapper.getGamer().addAmountOfGold((Gold) entry.getValue());
      } else {
        this.gamerWrapper.getGamer().addItem((Item) entry.getValue());
      }
      sd.append("* ").append(entry.getValue().toString()).append(".\n");
    }
    if (sd.length() == 0) {
      gamerWrapper.setStatus(
          String.format("The %s is empty", this.onWall.typeOnWall().getCheckName()));
    } else {
      gamerWrapper.setStatus(String.format("You acquired:%s %s", "\n", sd.toString()));
    }
    gamerWrapper.setLastListCommandUsed(commandListName);
  }
}
