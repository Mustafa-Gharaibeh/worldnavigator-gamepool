package com.worldnavigator.gamepoolservice.pool.controler.command.wall.seller.sellcommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.selleroperations.SellOperation;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.seller.Seller;

import java.util.Objects;

public final class SellCommand implements Command {
    private final SellOperation sellOperation;
    private final GamerWrapper gamerWrapper;
    private final String itemName;

    private SellCommand(SellOperation sellOperation, GamerWrapper gamerWrapper, String itemName) {
        this.sellOperation = sellOperation;
        this.gamerWrapper = gamerWrapper;
        this.itemName = itemName.trim();
    }

    public static SellCommand createSellCommand(Seller seller, GamerWrapper gamer, String itemName) {
        return new SellCommand(
                Objects.requireNonNull(seller),
                Objects.requireNonNull(gamer),
                Objects.requireNonNull(itemName));
    }

    @Override
    public void execute() {
        if (this.gamerWrapper.getGamer().getMaterialMap().containsKey(this.itemName)) {
            this.gamerWrapper
                    .getGamer()
                    .deleteItem(this.itemName)
                    .ifPresent(
                            item -> {
                                Gold gold = this.sellOperation.sell(item);
                                this.gamerWrapper.getGamer().addAmountOfGold(gold);
                                gamerWrapper.setStatus(
                                        String.format(
                                                "You sell %s and you got %s gold", this.itemName, gold.getAmount()));
                                gamerWrapper.setLastListCommandUsed("trade");
                            });
        } else {
            gamerWrapper.setStatus(String.format("You cannot sell %s", this.itemName));
            gamerWrapper.setLastListCommandUsed("trade");
        }
    }
}
