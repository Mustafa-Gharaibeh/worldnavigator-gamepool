package com.worldnavigator.gamepoolservice.pool.controler.commandfactory;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.command.incorrectcommand.IncorrectCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.lightingroomcommands.switchlightscommand.SwitchLightsCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.lightingroomcommands.useflashlightcommand.UseFlashLightCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.quitcommand.QuitCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.statuscommand.StatusCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.wall.checkcommand.CheckCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.wall.lockable.opencommand.OpenCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.wall.lockable.usekeycommand.UseKeyCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.wall.lookcommand.LookCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.wall.seller.finishtrade.FinishTrade;
import com.worldnavigator.gamepoolservice.pool.controler.commandfactory.actioncommandfactory.ActionCommandFactory;
import com.worldnavigator.gamepoolservice.pool.controler.commandfactory.sellercommandfactory.SellerCommandFactory;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.material.item.flashlight.Flashlight;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import com.worldnavigator.gamepoolservice.pool.model.room.Room;
import com.worldnavigator.gamepoolservice.pool.model.wall.Wall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.seller.Seller;

import java.util.Objects;

public class CommandFactory {
    private final Maze maze;
    private final GamerWrapper gamerWrapper;
    private Room currentRoom;
    private Wall wall;

    private CommandFactory(Maze maze, GamerWrapper gamerWrapper) {
        this.maze = maze;
        this.gamerWrapper = gamerWrapper;
        this.setCurrentRoom().setWall();
    }

    public static synchronized CommandFactory createCommandFactory(
            Maze maze, GamerWrapper gamerWrapper) {
        return new CommandFactory(maze, gamerWrapper);
    }

    protected CommandFactory setCurrentRoom() {
        this.currentRoom = this.maze.getCurrentRoom(gamerWrapper.getGamer());
        return this;
    }

    protected CommandFactory setWall() {
        this.wall = this.currentRoom.getWalls().get(this.gamerWrapper.getGamer().getCurrentDirection());
        return this;
    }

    public Command createCommand(String arg) {
        String[] argSplit = arg.split("\\s");
        StringBuilder sd = new StringBuilder();
        for (int i = 1; i < argSplit.length; i++) {
            sd.append(argSplit[i]).append(" ");
        }
        if (arg.equals("quit")) {
            return new QuitCommand(maze, gamerWrapper);
        } else if (this.isActionCommand(arg)) {
            return this.createActionCommand(arg);
        } else if (this.isSellerCommand(arg)) {
            return this.createSellerCommand(arg, sd);
        } else if ("look".equals(arg)) {
            return this.createLookCommand();
        } else if (this.isASwitchLightCommand(arg)) {
            return createSwitchLightCommand(arg);
        } else if (arg.contains("use") && arg.contains("key")) {
            return createUseKeyCommand(sd.toString());
        } else if ("status".equals(arg)) {
            return createStatusCommand();
        } else if ("check".equals(argSplit[0])) {
            return createCheckCommand(sd.toString());
        } else if ("open".equals(arg)) {
            return createOpenCommand();
        } else if ("finish trade".equals(arg)) {
            return new FinishTrade(gamerWrapper);
        }
        return IncorrectCommand.createIncorrectCommand(gamerWrapper);
    }

    private Command createActionCommand(String arg) {
        return new ActionCommandFactory(this.maze, this.gamerWrapper).createActionCommand(arg);
    }

    private boolean isActionCommand(String arg) {
        return arg.equals("left")
                || arg.equals("right")
                || arg.equals("forward")
                || arg.equals("backward");
    }

    private Command createSellerCommand(String arg, StringBuilder split) {
        return new SellerCommandFactory((Seller) this.wall.getOnWall(), this.gamerWrapper)
                .createSellerCommand(arg, split);
    }

    private boolean isSellerCommand(String arg) {
        return arg.equals("trade") || arg.equals("list") || arg.contains("buy") || arg.contains("sell");
    }

    private Command createCheckCommand(String arg) {
        return CheckCommand.createCheckCommand(this.wall.getOnWall(), this.gamerWrapper, arg);
    }

    private Command createLookCommand() {
        return LookCommand.createLookCommand(
                this.wall, this.currentRoom.getRoomLightingStatus().isARoomLit(), gamerWrapper);
    }

    private Command createUseKeyCommand(String arg) {
        return UseKeyCommand.createUseKeyCommand(this.wall.getOnWall(), gamerWrapper, arg);
    }

    private Command createOpenCommand() {
        return OpenCommand.createOpenCommand(this.wall.getOnWall(), gamerWrapper);
    }

    private Command createStatusCommand() {
        return StatusCommand.createStatusCommand(this.gamerWrapper);
    }

    private boolean isASwitchLightCommand(String arg) {
        if ("switchLights".equalsIgnoreCase(arg)) {
            return true;
        } else {
            return arg.contains("use") && arg.contains("flashlight");
        }
    }

    private Command createSwitchLightCommand(String arg) {
        if ("switchLights".equalsIgnoreCase(arg)) {
            return SwitchLightsCommand.createSwitchLightsCommand(this.currentRoom, gamerWrapper);
        } else if (arg.contains("use") && arg.contains("flashlight")) {
            Flashlight flashlight =
                    (Flashlight) this.gamerWrapper.getGamer().getMaterialMap().get("flashlight");
            if (Objects.nonNull(flashlight)) {
                return UseFlashLightCommand.createUseFlashLightCommand(this.currentRoom, gamerWrapper);
            }
        }
        return IncorrectCommand.createIncorrectCommand(gamerWrapper);
    }
}
