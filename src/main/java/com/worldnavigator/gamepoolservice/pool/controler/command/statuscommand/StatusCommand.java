package com.worldnavigator.gamepoolservice.pool.controler.command.statuscommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;

import java.util.Map;
import java.util.Objects;

public final class StatusCommand implements Command {
    private final GamerWrapper gamerWrapper;

    private StatusCommand(GamerWrapper gamerWrapper) {
        this.gamerWrapper = gamerWrapper;
    }

    public static StatusCommand createStatusCommand(GamerWrapper gamerWrapper) {
        return new StatusCommand(Objects.requireNonNull(gamerWrapper));
    }

    @Override
    public void execute() {
        StringBuilder sd = new StringBuilder("The player status :\n");
        sd.append("-")
                .append("The Current Direction is ")
                .append(this.gamerWrapper.getGamer().getCurrentDirection())
                .append(".\n");
        if (!this.gamerWrapper.getGamer().getMaterialMap().isEmpty()) {
            sd.append("-Material: \n");
            for (Map.Entry<String, Material> entry :
                    this.gamerWrapper.getGamer().getMaterialMap().entrySet()) {
                sd.append("*").append(entry.getValue().toString()).append(".\n");
            }
            gamerWrapper.setStatus(sd.toString());
        }
    }
}
