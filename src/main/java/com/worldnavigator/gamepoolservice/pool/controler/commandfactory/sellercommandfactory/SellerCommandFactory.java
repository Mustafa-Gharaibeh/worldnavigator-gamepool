package com.worldnavigator.gamepoolservice.pool.controler.commandfactory.sellercommandfactory;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.command.incorrectcommand.IncorrectCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.wall.seller.buycommand.BuyCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.wall.seller.listcommand.ListCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.wall.seller.sellcommand.SellCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.wall.seller.tradecommand.TradeCommand;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.seller.Seller;

public class SellerCommandFactory {
  private final Seller seller;
  private final GamerWrapper gamerWrapper;

  public SellerCommandFactory(Seller seller, GamerWrapper gamerWrapper) {
    this.seller = seller;
    this.gamerWrapper = gamerWrapper;
  }

  public Command createSellerCommand(String arg, StringBuilder sd) {
    if ("trade".equals(arg)) {
      return TradeCommand.createTradeCommand(this.seller, gamerWrapper);
    } else if ("list".equals(arg)) {
      return ListCommand.createListCommand(this.seller, gamerWrapper);
    } else if (arg.contains("buy")) {
      return BuyCommand.createBuyCommand(this.seller, this.gamerWrapper, sd.toString());
    } else if (arg.contains("sell")) {
      return SellCommand.createSellCommand(this.seller, this.gamerWrapper, sd.toString());
    }
    return IncorrectCommand.createIncorrectCommand(gamerWrapper);
  }
}
