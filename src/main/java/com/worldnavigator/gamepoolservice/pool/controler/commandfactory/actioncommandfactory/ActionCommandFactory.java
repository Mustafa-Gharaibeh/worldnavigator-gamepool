package com.worldnavigator.gamepoolservice.pool.controler.commandfactory.actioncommandfactory;

import com.worldnavigator.gamepoolservice.pool.controler.command.actioncommands.Action;
import com.worldnavigator.gamepoolservice.pool.controler.command.actioncommands.movecommands.backwardcommand.BackwardCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.actioncommands.movecommands.forwardcommand.ForwardCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.actioncommands.orentationcommands.leftcommand.LeftCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.actioncommands.orentationcommands.rightcommand.RightCommand;
import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.command.incorrectcommand.IncorrectCommand;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;

public class ActionCommandFactory {
    private final Maze maze;
    private final GamerWrapper gamerWrapper;

    public ActionCommandFactory(Maze maze, GamerWrapper gamerWrapper) {
        this.maze = maze;
        this.gamerWrapper = gamerWrapper;
    }

    private Action getTypeOfAction(String arg) {
        if ("left".equals(arg)) {
            return Action.LEFT;
        } else if ("right".equals(arg)) {
            return Action.RIGHT;
        } else if ("forward".equals(arg)) {
            return Action.FORWARD;
        } else {
            return Action.BACKWARD;
        }
    }

    public Command createActionCommand(String arg) {
        Action action = this.getTypeOfAction(arg);
        if (Action.LEFT.equals(action) || Action.RIGHT.equals(action)) {
            return orientationCommand(action);
        } else if (Action.FORWARD.equals(action) || Action.BACKWARD.equals(action)) {
            return moveCommand(action);
        }
        return IncorrectCommand.createIncorrectCommand(gamerWrapper);
    }

    private Command orientationCommand(Action action) {
        if (Action.LEFT.equals(action)) {
            return LeftCommand.createLeftCommand(this.gamerWrapper);
        } else {
            return RightCommand.createRightCommand(this.gamerWrapper);
        }
    }

    private Command moveCommand(Action action) {
        if (Action.FORWARD.equals(action)) {
            return ForwardCommand.createForwardCommand(this.maze, this.gamerWrapper);
        } else {
            return BackwardCommand.createBackwardCommand(this.maze, this.gamerWrapper);
        }
    }
}
