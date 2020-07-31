package com.worldnavigator.gamepoolservice.pool.controler.command.actioncommands.orentationcommands.leftcommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.gamer.Direction;

import java.util.List;

public final class LeftCommand implements Command {
    private final GamerWrapper gamerWrapper;

    private LeftCommand(GamerWrapper gamerWrapper) {
        this.gamerWrapper = gamerWrapper;
    }

    public static LeftCommand createLeftCommand(GamerWrapper gamerWrapper) {
        return new LeftCommand(gamerWrapper);
    }

    @Override
    public void execute() {
        this.gamerWrapper.getGamer().setCurrentDirection(this.getNewDirection());
        gamerWrapper.setStatus(
                String.format(
                        "You look to %s direction", this.gamerWrapper.getGamer().getCurrentDirection()));
        gamerWrapper.setLastListCommandUsed("default");
    }

    private Direction getNewDirection() {
        List<Direction> directions = this.gamerWrapper.getGamer().getDirections();
        int indexOfCurrentDirection =
                directions.indexOf(this.gamerWrapper.getGamer().getCurrentDirection());
        if (indexOfCurrentDirection == 0) {
            return directions.get(directions.size() - 1);
        }
        return directions.get(indexOfCurrentDirection - 1);
    }
}
