package com.worldnavigator.gamepoolservice.pool.controler.command.lightingroomcommands.useflashlightcommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.room.Room;

import java.util.Objects;

public final class UseFlashLightCommand implements Command {
    private final Room room;
    private final GamerWrapper gamerWrapper;

    private UseFlashLightCommand(Room room, GamerWrapper gamerWrapper) {
        this.room = room;
        this.gamerWrapper = gamerWrapper;
    }

    public static UseFlashLightCommand createUseFlashLightCommand(
            Room room, GamerWrapper gamerWrapper) {
        return new UseFlashLightCommand(
                Objects.requireNonNull(room), Objects.requireNonNull(gamerWrapper));
    }

    @Override
    public void execute() {
        if (this.room.getRoomLightingStatus().isARoomDark()) {
            this.room.getRoomLightingStatus().switchLights(true);
            gamerWrapper.setLastListCommandUsed("default");
            gamerWrapper.setStatus(
                    String.format("The %s is Lit by Using FlashLight", this.room.getRoomName()));
        } else {
            gamerWrapper.setStatus(String.format("The %s is already Lit", this.room.getRoomName()));
            gamerWrapper.setLastListCommandUsed("default");
        }
    }
}
