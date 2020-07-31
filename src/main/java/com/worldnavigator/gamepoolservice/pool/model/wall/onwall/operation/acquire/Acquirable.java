package com.worldnavigator.gamepoolservice.pool.model.wall.onwall.operation.acquire;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AcquirableImpl.class, name = "AcquirableImpl"),
        @JsonSubTypes.Type(value = UnAcquirable.class, name = "UnAcquirable")
})
public interface Acquirable {
    Map<String, Material> acquire();

    void addItems(List<Item> items);
}
