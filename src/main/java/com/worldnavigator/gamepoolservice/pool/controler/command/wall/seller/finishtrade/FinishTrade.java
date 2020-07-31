package com.worldnavigator.gamepoolservice.pool.controler.command.wall.seller.finishtrade;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall;

public class FinishTrade implements Command {
    private final GamerWrapper gamerWrapper;

    public FinishTrade(GamerWrapper gamerWrapper) {
        this.gamerWrapper = gamerWrapper;
    }

    @Override
    public void execute() {
        gamerWrapper.setStatus("finish trade");
        gamerWrapper.setLastListCommandUsed(TypeOnWall.SELLER.getCheckName());
    }
}
