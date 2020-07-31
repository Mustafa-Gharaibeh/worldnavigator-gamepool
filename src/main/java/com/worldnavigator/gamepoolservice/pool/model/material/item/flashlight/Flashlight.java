package com.worldnavigator.gamepoolservice.pool.model.material.item.flashlight;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;

import java.util.Objects;

public final class Flashlight implements Item {

  @JsonProperty("flashlightName")
  private final String flashlightName;

  @JsonProperty("price")
  private final Gold price;

  @JsonCreator
  private Flashlight(
      @JsonProperty("flashlightName") String flashlightName, @JsonProperty("price") Gold price) {
    this.flashlightName = flashlightName;
    this.price = price;
  }

  public static Flashlight createFlashlight(String flashlightName, Gold price) {
    if (Objects.nonNull(flashlightName) && Objects.nonNull(price)) {
      if (price.getAmount() >= 0) {
        return new Flashlight(flashlightName, price);
      } else {
        return new Flashlight(flashlightName, Gold.createGold(0));
      }
    }
    return new Flashlight(flashlightName, price);
  }

  public static Flashlight createFlashlightWithPrice2(String flashlightName) {
    return new Flashlight(Objects.requireNonNull(flashlightName), Gold.createGold(2));
  }

  @Override
  public Gold price() {
    return this.price;
  }

  @Override
  public String name() {
    return this.flashlightName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Flashlight that = (Flashlight) o;
    return Objects.equals(flashlightName, that.flashlightName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flashlightName);
  }

  @Override
  public String toString() {
    return "Flashlight{" + "FLASHLIGHT_NAME='" + flashlightName + '\'' + ", price=" + price + '}';
  }
}
