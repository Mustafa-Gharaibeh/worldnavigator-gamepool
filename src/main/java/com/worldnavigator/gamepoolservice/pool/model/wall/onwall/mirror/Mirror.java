package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.mirror;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.acquire.AcquirableImpl;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.lockable.NotLockable;
import lombok.ToString;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Value
@ToString
public class Mirror extends OnWall {

    @JsonCreator
    private Mirror(@JsonProperty("MaterialMap") Map<String, Material> map) {
        super(AcquirableImpl.createAcquirable(map), NotLockable.createNotLockable());
    }

    @JsonCreator
    private Mirror() {
        super(AcquirableImpl.createEmptyAcquirable(), NotLockable.createNotLockable());
    }

    public static Mirror createMirrorHasHiddenItem(Item hiddenItem) {
        Objects.requireNonNull(hiddenItem);
        Map<String, Material> map = new HashMap<>();
        map.put(hiddenItem.name(), hiddenItem);
        return new Mirror(map);
    }

    public static Mirror createMirrorHasNoHiddenItem() {
        return new Mirror();
    }

    @Override
    public TypeOnWall typeOnWall() {
        return TypeOnWall.MIRROR;
    }
}
