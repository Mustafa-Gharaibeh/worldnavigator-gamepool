package com.worldnavigator.gamepoolservice.pool.controler.command.incorrectcommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;

public final class IncorrectCommand implements Command {
    private final GamerWrapper gamerWrapper;

    private IncorrectCommand(GamerWrapper gamerWrapper) {
        this.gamerWrapper = gamerWrapper;
    }

    public static IncorrectCommand createIncorrectCommand(GamerWrapper gamerWrapper) {
        return new IncorrectCommand(gamerWrapper);
    }

    @Override
    public void execute() {
        gamerWrapper.setStatus("Incorrect command");
    }
}
