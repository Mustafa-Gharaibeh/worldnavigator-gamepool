package com.worldnavigator.gamepoolservice.pool.model.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.concurrent.atomic.AtomicBoolean;

@Value
public class RoomLightingStatus {

    @JsonProperty("isARoomHasLights")
    Boolean isARoomHasLights;

    @JsonIgnore
    AtomicBoolean isARoomLit;

    @JsonCreator
    private RoomLightingStatus(@JsonProperty("isARoomHasLights") boolean isARoomHasLights) {
        this.isARoomLit = new AtomicBoolean(Boolean.FALSE);
        this.isARoomHasLights = isARoomHasLights;
    }

    public static RoomLightingStatus createRoomLightingStatus(Boolean isARoomHasLights) {
        return new RoomLightingStatus(isARoomHasLights);
    }

    @JsonIgnore
    public boolean isARoomLit() {
        return isARoomLit.get();
    }

    @JsonIgnore
    public boolean isARoomDark() {
        return !isARoomLit.get();
    }

    public void switchLights(boolean isARoomLit) {
        this.isARoomLit.set(isARoomLit);
    }

    @JsonIgnore
    public boolean isARoomHasLights() {
        return isARoomHasLights;
    }
}
