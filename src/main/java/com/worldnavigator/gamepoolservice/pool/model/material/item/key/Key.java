package com.worldnavigator.gamepoolservice.pool.model.material.item.key;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import lombok.Value;

import java.util.Objects;

@Value
public class Key implements Item {
  @JsonProperty("keyName")
  String keyName;

  @JsonProperty("price")
  Gold price;

  @JsonCreator
  private Key(@JsonProperty("keyName") String keyName, @JsonProperty("price") Gold price) {
    this.keyName = keyName;
    this.price = price;
  }

  public static Key createKey(String keyName, Gold price) {
    if (Objects.requireNonNull(price).getAmount() >= 0) {
      return new Key(Objects.requireNonNull(keyName), price);
    } else {
      return new Key(Objects.requireNonNull(keyName), Gold.createGold(0));
    }
  }

  public static Key createKeyWithPriceEqual10(String keyName) {
    return new Key(Objects.requireNonNull(keyName), Gold.createGold(10));
  }

  @Override
  public Gold price() {
    return this.price;
  }

  @Override
  public String name() {
    return this.keyName;
  }

  @Override
  public String toString() {
    return "Key{" + "KEY_NAME='" + keyName + '\'' + ", price=" + price + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Key key = (Key) o;
    return keyName.equals(key.keyName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keyName);
  }
}
