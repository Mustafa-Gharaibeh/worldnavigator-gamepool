package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.chest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;
import com.worldnavigator.gamepoolservice.pool.model.material.item.key.Key;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.acquire.AcquirableImpl;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.lockable.LockableImpl;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Map;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Value
public class Chest extends OnWall {

  @JsonCreator
  private Chest(
      @JsonProperty("lockKey") Key lockKey,
      @JsonProperty("MaterialMap") Map<String, Material> map) {
    super(AcquirableImpl.createAcquirable(map), LockableImpl.createLockable(lockKey));
  }

  public static Chest createChest(Key lockKey, Map<String, Material> map) {
    return new Chest(Objects.requireNonNull(lockKey), Objects.requireNonNull(map));
  }

  @Override
  public TypeOnWall typeOnWall() {
    return TypeOnWall.CHEST;
  }
}
