package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.selleroperations;


import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;

public interface SellOperation {
    Gold sell(Item item);
}
