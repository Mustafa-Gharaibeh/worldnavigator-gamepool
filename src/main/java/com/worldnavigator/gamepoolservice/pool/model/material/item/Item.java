package com.worldnavigator.gamepoolservice.pool.model.material.item;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.material.item.flashlight.Flashlight;
import com.worldnavigator.gamepoolservice.pool.model.material.item.key.Key;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Key.class, name = "Key"),
  @JsonSubTypes.Type(value = Flashlight.class, name = "Flashlight")
})
public interface Item extends Material {
  Gold price();

  String name();
}
