package com.worldnavigator.gamepoolservice.pool.controler.command.wall.seller.listcommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.seller.Seller;

import java.util.Map;

public final class ListCommand implements Command {
    private final Seller seller;
    private final GamerWrapper gamerWrapper;

    private ListCommand(Seller seller, GamerWrapper gamerWrapper) {
        this.seller = seller;
        this.gamerWrapper = gamerWrapper;
    }

    public static ListCommand createListCommand(Seller seller, GamerWrapper gamerWrapper) {
        return new ListCommand(seller, gamerWrapper);
    }

    @Override
    public void execute() {
        StringBuilder sd = new StringBuilder();
        Map<String, Item> itemMap = this.seller.getItems();
        for (Map.Entry<String, Item> entry : itemMap.entrySet()) {
            sd.append("* ").append(entry.getKey()).append(".\n");
        }
        if (sd.length() == 0) {
            gamerWrapper.setStatus("The seller has nothing");
        } else {
            gamerWrapper.setStatus(String.format("The seller has:%s%s", "\n", sd.toString()));
        }
        gamerWrapper.setLastListCommandUsed("trade");
    }
}
