package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.lockable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.worldnavigator.gamepoolservice.pool.model.material.item.key.Key;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LockableImpl.class, name = "LockableImpl"),
        @JsonSubTypes.Type(value = NotLockable.class, name = "NotLockable")
})
public interface Lockable {
    void useKey(Key key);

    boolean isLock();

    String status();
}
