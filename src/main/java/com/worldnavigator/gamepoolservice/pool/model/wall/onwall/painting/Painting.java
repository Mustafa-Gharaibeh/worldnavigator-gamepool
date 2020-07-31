package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.painting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.acquire.AcquirableImpl;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.lockable.NotLockable;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString
public class Painting extends OnWall {

    @JsonCreator
    private Painting(@JsonProperty("MaterialMap") Map<String, Material> map) {
        super(AcquirableImpl.createAcquirable(map), NotLockable.createNotLockable());
    }

    public Painting() {
        super(AcquirableImpl.createEmptyAcquirable(), NotLockable.createNotLockable());
    }

    public static Painting createPaintingWithHiddenItem(Item hiddenItem) {
        Objects.requireNonNull(hiddenItem);
        Map<String, Material> map = new HashMap<>();
        map.put(hiddenItem.name(), hiddenItem);
        return new Painting(map);
    }

    public static Painting createPaintingHasNoHiddenKey() {
        return new Painting();
    }

    @Override
    public TypeOnWall typeOnWall() {
        return TypeOnWall.PAINTING;
    }
}
