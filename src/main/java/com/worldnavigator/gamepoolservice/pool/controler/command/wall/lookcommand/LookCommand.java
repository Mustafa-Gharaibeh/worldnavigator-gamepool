package com.worldnavigator.gamepoolservice.pool.controler.command.wall.lookcommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.wall.Wall;

import java.util.Objects;

public final class LookCommand implements Command {
    private final Wall wall;
    private final GamerWrapper gamerWrapper;
    private final Boolean roomIsLit;

    private LookCommand(Wall wall, Boolean roomIsLit, GamerWrapper gamerWrapper) {
        this.wall = wall;
        this.roomIsLit = roomIsLit;
        this.gamerWrapper = gamerWrapper;
    }

    public static LookCommand createLookCommand(
            Wall wall, Boolean roomIsLit, GamerWrapper gamerWrapper) {
        return new LookCommand(
                Objects.requireNonNull(wall),
                Objects.requireNonNull(roomIsLit),
                Objects.requireNonNull(gamerWrapper));
    }

    @Override
    public void execute() {
        if (this.roomIsLit) {
            String lookDescription = this.wall.getOnWall().typeOnWall().getLookDescription();
            String commandListName = this.wall.getOnWall().typeOnWall().getCheckName();
            gamerWrapper.setStatus(lookDescription);
            gamerWrapper.setLastListCommandUsed(commandListName);
        } else {
            gamerWrapper.setStatus("Dark");
            gamerWrapper.setLastListCommandUsed("default");
        }
    }
}
