package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.seller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.acquire.UnAcquirable;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.lockable.NotLockable;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.selleroperations.BuyOperation;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.selleroperations.SellOperation;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Value
@ToString
@EqualsAndHashCode(callSuper = true)
public class Seller extends OnWall implements BuyOperation, SellOperation {
  @JsonProperty("items")
  Map<String, Item> items;

  @JsonCreator
  private Seller(@JsonProperty("items") Map<String, Item> items) {
    super(UnAcquirable.createUnAcquirable(), NotLockable.createNotLockable());
    this.items = items;
  }

  public static Seller createSeller(Map<String, Item> items) {
    return new Seller(Objects.requireNonNull(items));
  }

  public static Seller createSellerHasNothing() {
    return new Seller(new HashMap<>());
  }

  @Override
  public Gold sell(Item item) {
    Objects.requireNonNull(item, "The item will non null");
    this.items.put(item.name(), item);
    return item.price();
  }

  @Override
  public Item buy(String name) {
    return this.items.remove(Objects.requireNonNull(name, "The name of item will non null"));
  }

  public Map<String, Item> getItems() {
    return this.items;
  }

  @Override
  public TypeOnWall typeOnWall() {
    return TypeOnWall.SELLER;
  }
}
