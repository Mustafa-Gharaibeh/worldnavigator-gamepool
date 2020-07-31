package com.worldnavigator.gamepoolservice.pool.controler.command.wall.seller.buycommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.selleroperations.BuyOperation;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.seller.Seller;

import java.util.Map;
import java.util.Objects;

public final class BuyCommand implements Command {
    private final BuyOperation buyOperation;
    private final GamerWrapper gamerWrapper;
    private final String itemName;

    private BuyCommand(BuyOperation buyOperation, GamerWrapper gamerWrapper, String itemName) {
        this.buyOperation = buyOperation;
        this.gamerWrapper = gamerWrapper;
        this.itemName = itemName.trim();
    }

    public static BuyCommand createBuyCommand(Seller seller, GamerWrapper gamer, String itemName) {
        return new BuyCommand(
                Objects.requireNonNull(seller),
                Objects.requireNonNull(gamer),
                Objects.requireNonNull(itemName));
    }

    @Override
    public void execute() {
        Map<String, Item> itemMap = this.buyOperation.getItems();
        Gold gamerGold = (Gold) this.gamerWrapper.getGamer().getMaterialMap().get("Gold");
        String commandListName = "trade";
        if (itemMap.containsKey(this.itemName)
                && gamerGold.compareTo(itemMap.get(this.itemName).price().getAmount()) >= 0) {
            Item item = buyOperation.buy(this.itemName);
            if (Objects.nonNull(item)) {
                this.gamerWrapper.getGamer().addItem(item);
                this.gamerWrapper.getGamer().addAmountOfGold(-1 * item.price().getAmount());
                gamerWrapper.setLastListCommandUsed(commandListName);
                gamerWrapper.setStatus(String.format("You got %s", this.itemName));
            } else {
                gamerWrapper.setStatus(String.format("You cannot get %s", this.itemName));
                gamerWrapper.setLastListCommandUsed(commandListName);
            }
        } else {
            gamerWrapper.setStatus(String.format("You cannot get %s", this.itemName));
            gamerWrapper.setLastListCommandUsed(commandListName);
        }
    }
}
