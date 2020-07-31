package com.worldnavigator.gamepoolservice.pool.model.wall.onwall;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.chest.Chest;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.door.Door;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.mirror.Mirror;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.nothing.PlainWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.acquire.Acquirable;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.lockable.Lockable;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.painting.Painting;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.seller.Seller;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Seller.class, name = "Seller"),
        @JsonSubTypes.Type(value = Painting.class, name = "Painting"),
        @JsonSubTypes.Type(value = PlainWall.class, name = "PlainWall"),
        @JsonSubTypes.Type(value = Mirror.class, name = "Mirror"),
        @JsonSubTypes.Type(value = Door.class, name = "Door"),
        @JsonSubTypes.Type(value = Chest.class, name = "Chest")
})
public abstract class OnWall {
    @JsonProperty("acquirable")
    private final Acquirable acquirable;

    @JsonProperty("lockable")
    private final Lockable lockable;

    @JsonCreator
    protected OnWall(
            @JsonProperty("acquirable") Acquirable acquirable,
            @JsonProperty("lockable") Lockable lockable) {
        this.acquirable = acquirable;
        this.lockable = lockable;
    }

    public Acquirable getAcquirable() {
        return acquirable;
    }

    public Lockable getLockable() {
        return lockable;
    }

    public abstract TypeOnWall typeOnWall();
}
