package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.nothing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.acquire.UnAcquirable;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.lockable.NotLockable;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class PlainWall extends OnWall {
    @JsonCreator
    private PlainWall() {
        super(UnAcquirable.createUnAcquirable(), NotLockable.createNotLockable());
    }

    public static PlainWall createPlainWall() {
        return new PlainWall();
    }

    @Override
    public TypeOnWall typeOnWall() {
        return TypeOnWall.NOTHING;
    }
}
