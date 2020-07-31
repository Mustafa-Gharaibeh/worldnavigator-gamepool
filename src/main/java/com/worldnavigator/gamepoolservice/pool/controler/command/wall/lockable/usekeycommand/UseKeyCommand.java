package com.worldnavigator.gamepoolservice.pool.controler.command.wall.lockable.usekeycommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.material.item.key.Key;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.lockable.Lockable;

import java.util.Objects;
import java.util.Optional;

public final class UseKeyCommand implements Command {
    private final OnWall onWall;
    private final GamerWrapper gamerWrapper;
    private final Key key;

    private UseKeyCommand(OnWall onWall, GamerWrapper gamerWrapper, String arg) {
        this.onWall = onWall;
        this.gamerWrapper = gamerWrapper;
        this.key = (Key) gamerWrapper.getGamer().getMaterialMap().get(arg.trim());
    }

    public static UseKeyCommand createUseKeyCommand(
            OnWall onWall, GamerWrapper gamerWrapper, String arg) {
        return new UseKeyCommand(Objects.requireNonNull(onWall), gamerWrapper, arg);
    }

    @Override
    public void execute() {
        if (this.onWall.typeOnWall().isLockable()) {
            Lockable lockable = this.onWall.getLockable();
            Optional<Key> optional = Optional.ofNullable(this.key);
            optional.ifPresent(lockable::useKey);
            gamerWrapper.setStatus(lockable.status());
            gamerWrapper.setLastListCommandUsed(onWall.typeOnWall().getCheckName());
        } else {
            gamerWrapper.setStatus("Incorrect command");
            gamerWrapper.setLastListCommandUsed("default");
        }
    }
}
