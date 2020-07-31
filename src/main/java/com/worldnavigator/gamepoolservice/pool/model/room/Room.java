package com.worldnavigator.gamepoolservice.pool.model.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.gamer.Direction;
import com.worldnavigator.gamepoolservice.pool.model.wall.Wall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall;
import lombok.Value;

import java.util.Map;
import java.util.Objects;

@Value
public class Room {
  @JsonProperty("roomName")
  String roomName;

  @JsonProperty("roomLightingStatus")
  RoomLightingStatus roomLightingStatus;

  @JsonProperty("walls")
  Map<Direction, Wall> walls;

  @JsonCreator
  private Room(
      @JsonProperty("roomName") String roomName,
      @JsonProperty("walls") Map<Direction, Wall> walls,
      @JsonProperty("roomLightingStatus") RoomLightingStatus roomLightingStatus) {
    this.roomLightingStatus = roomLightingStatus;
    this.roomName = roomName;
    this.walls = walls;
    this.isHasAtLeastOneDoor();
  }

  public static Room createRoomWithLights(String roomName, Map<Direction, Wall> walls) {
    if (walls.size() != 4) {
      throw new IllegalArgumentException("The number of walls will be 4");
    }
    return new Room(roomName, walls, RoomLightingStatus.createRoomLightingStatus(Boolean.TRUE));
  }

  public static Room createRoomWithoutLights(String roomName, Map<Direction, Wall> walls) {
    if (walls.size() != 4) {
      throw new IllegalArgumentException("The number of walls will be 4");
    }
    return new Room(roomName, walls, RoomLightingStatus.createRoomLightingStatus(Boolean.FALSE));
  }

  public Map<Direction, Wall> getWalls() {
    return walls;
  }

  private void isHasAtLeastOneDoor() {
    for (Map.Entry<Direction, Wall> entry : walls.entrySet()) {
      if (entry.getValue().getOnWall().typeOnWall().equals(TypeOnWall.DOOR)) {
        return;
      }
    }
    throw new IllegalArgumentException("The Room Will has at least on door");
  }

  public RoomLightingStatus getRoomLightingStatus() {
    return roomLightingStatus;
  }

  public String getRoomName() {
    return roomName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Room room = (Room) o;
    return Objects.equals(roomName, room.roomName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roomName);
  }
}
