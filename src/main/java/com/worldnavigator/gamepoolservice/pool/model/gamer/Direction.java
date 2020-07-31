package com.worldnavigator.gamepoolservice.pool.model.gamer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Direction {
    @JsonProperty("NORTH")
    NORTH,
    @JsonProperty("SOUTH")
    SOUTH,
    @JsonProperty("WEST")
    WEST,
    @JsonProperty("EAST")
    EAST;

    @JsonCreator
    Direction() {
    }
}
