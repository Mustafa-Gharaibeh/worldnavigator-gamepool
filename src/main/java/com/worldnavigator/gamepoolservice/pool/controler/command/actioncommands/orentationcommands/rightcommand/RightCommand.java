package com.worldnavigator.gamepoolservice.pool.controler.command.actioncommands.orentationcommands.rightcommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.gamer.Direction;

import java.util.List;

public final class RightCommand implements Command {
    private final GamerWrapper gamerWrapper;

    private RightCommand(GamerWrapper gamerWrapper) {
        this.gamerWrapper = gamerWrapper;
    }

    public static RightCommand createRightCommand(GamerWrapper gamerWrapper) {
        return new RightCommand(gamerWrapper);
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
        if (indexOfCurrentDirection == directions.size() - 1) {
            return directions.get(0);
        } else {
            return directions.get(indexOfCurrentDirection + 1);
        }
    }
}
