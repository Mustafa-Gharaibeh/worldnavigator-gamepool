package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.lockable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.worldnavigator.gamepoolservice.pool.model.material.item.key.Key;
import lombok.Value;

@Value
public class NotLockable implements Lockable {

    @JsonCreator
    public NotLockable() {
    }

    public static NotLockable createNotLockable() {
        return new NotLockable();
    }

    @Override
    public void useKey(Key key) {
    }

    @JsonIgnore
    @Override
    public boolean isLock() {
        return false;
    }

    @Override
    public String status() {
        return "NotLockable";
    }
}
