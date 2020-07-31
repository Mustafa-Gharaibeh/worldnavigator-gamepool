package com.worldnavigator.gamepoolservice.pool.model.material;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import com.worldnavigator.gamepoolservice.pool.model.material.item.flashlight.Flashlight;
import com.worldnavigator.gamepoolservice.pool.model.material.item.key.Key;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Gold.class, name = "Gold"),
  @JsonSubTypes.Type(value = Item.class, name = "item"),
  @JsonSubTypes.Type(value = Key.class, name = "Key"),
  @JsonSubTypes.Type(value = Flashlight.class, name = "Flashlight")
})
public interface Material {
  String name();
}
