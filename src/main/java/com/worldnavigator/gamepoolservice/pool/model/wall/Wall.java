package com.worldnavigator.gamepoolservice.pool.model.wall;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.OnWall;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class Wall {
    @JsonProperty("onWall")
    OnWall onWall;

    @JsonCreator
    private Wall(@JsonProperty("onWall") OnWall onWall) {
        this.onWall = onWall;
    }

    public static Wall createWall(OnWall onWall) {
        return new Wall(onWall);
    }

    @Override
    public String toString() {
        return "Wall{" + "onWall=" + onWall + '}';
    }
}
