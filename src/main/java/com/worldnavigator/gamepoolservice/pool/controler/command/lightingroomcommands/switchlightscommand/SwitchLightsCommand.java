package com.worldnavigator.gamepoolservice.pool.controler.command.lightingroomcommands.switchlightscommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.room.Room;

import java.util.Objects;

public final class SwitchLightsCommand implements Command {
  private final Room room;
  private final GamerWrapper gamerWrapper;

  private SwitchLightsCommand(Room room, GamerWrapper gamerWrapper) {
    this.room = room;
    this.gamerWrapper = gamerWrapper;
  }

  public static SwitchLightsCommand createSwitchLightsCommand(
      Room room, GamerWrapper gamerWrapper) {
    return new SwitchLightsCommand(
        Objects.requireNonNull(room), Objects.requireNonNull(gamerWrapper));
  }

  @Override
  public void execute() {
    String commandListName = "default";
    if (this.room.getRoomLightingStatus().isARoomHasLights()) {
      this.room
          .getRoomLightingStatus()
          .switchLights(!this.room.getRoomLightingStatus().isARoomLit());
      if (this.room.getRoomLightingStatus().isARoomLit()) {
        gamerWrapper.setStatus(String.format("The %s is Lit", this.room.getRoomName()));
        gamerWrapper.setLastListCommandUsed(commandListName);
      } else {
        gamerWrapper.setStatus(String.format("The %s is Dark", this.room.getRoomName()));
        gamerWrapper.setLastListCommandUsed(commandListName);
      }
    } else {
      gamerWrapper.setStatus(
          String.format(
              "The %s does not have a lights %s",
              this.room.getRoomName(), "will use the FlashLight to Lit the room"));
      gamerWrapper.setLastListCommandUsed("backward");
    }
  }
}
