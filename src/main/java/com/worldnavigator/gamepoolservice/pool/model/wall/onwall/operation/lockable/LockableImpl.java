package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.lockable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.material.item.key.Key;
import lombok.ToString;
import lombok.Value;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Value
@ToString
public class LockableImpl implements Lockable {
  @JsonProperty("lockKey")
  Key lockKey;

  @JsonProperty("lock")
  AtomicBoolean lock;

  @JsonCreator
  private LockableImpl(@JsonProperty("lockKey") Key lockKey, @JsonProperty("lock") Boolean lock) {
    this.lockKey = lockKey;
    this.lock = new AtomicBoolean(lock);
  }

  public static LockableImpl createLockable(Key lockKey) {
    if (Objects.nonNull(lockKey) && "open".equalsIgnoreCase(lockKey.name())) {
      return new LockableImpl(lockKey, Boolean.FALSE);
    } else {
      return new LockableImpl(lockKey, Boolean.TRUE);
    }
  }

  @Override
  public void useKey(Key key) {
    if (this.lockKey.name().equalsIgnoreCase(key.name())) {
      this.lock.set(!this.lock.get());
    }
  }

  @Override
  public boolean isLock() {
    return this.lock.get();
  }

  @Override
  public String status() {
    if (this.isLock()) {
      return String.format("It is Locked and need %s to open", this.lockKey.name());
    }
    return "It is open";
  }
}
