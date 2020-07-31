package com.worldnavigator.gamepoolservice.pool.controler.command.wall.seller.tradecommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.command.wall.seller.listcommand.ListCommand;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.seller.Seller;

public final class TradeCommand implements Command {
    private final OnWall onWall;
    private final GamerWrapper gamerWrapper;

    private TradeCommand(OnWall onWall, GamerWrapper gamerWrapper) {
        this.onWall = onWall;
        this.gamerWrapper = gamerWrapper;
    }

    public static TradeCommand createTradeCommand(OnWall onWall, GamerWrapper gamerWrapper) {
        return new TradeCommand(onWall, gamerWrapper);
    }

    @Override
    public void execute() {
        if (this.onWall.typeOnWall().equals(TypeOnWall.SELLER)) {
            ListCommand.createListCommand((Seller) this.onWall, gamerWrapper).execute();
        } else {
            gamerWrapper.setLastListCommandUsed("default");
            gamerWrapper.setStatus("You don't look to Seller");
        }
    }
}
