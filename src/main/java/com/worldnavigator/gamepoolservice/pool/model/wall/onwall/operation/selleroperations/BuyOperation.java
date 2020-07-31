package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.selleroperations;

import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;

import java.util.Map;

public interface BuyOperation {
    Map<String, Item> getItems();

    Item buy(String name);
}
