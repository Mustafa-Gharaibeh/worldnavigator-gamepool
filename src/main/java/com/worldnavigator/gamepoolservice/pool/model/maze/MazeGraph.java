package com.worldnavigator.gamepoolservice.pool.model.maze;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.room.Room;
import lombok.Value;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Value
public class MazeGraph {

  @JsonProperty("firstRooms")
  List<Room> firstRooms;

  @JsonProperty("rooms")
  Room[] rooms;

  @JsonProperty("map")
  Map<String, List<Room>> stringTMap;

  @JsonProperty("Edges")
  AtomicInteger e;

  @JsonCreator
  private MazeGraph(
      @JsonProperty("firstRooms") List<Room> firstRooms,
      @JsonProperty("rooms") Room[] rooms,
      @JsonProperty("map") Map<String, List<Room>> stringTMap,
      @JsonProperty("Edges") AtomicInteger e) {
    this.firstRooms = Collections.synchronizedList(firstRooms);
    this.rooms = rooms;
    this.stringTMap = new ConcurrentHashMap<>(stringTMap);
    this.e = e;
  }

  private MazeGraph(List<Room> firstRooms, Room... rooms) {
    this.firstRooms = Collections.synchronizedList(firstRooms);
    this.rooms = rooms;
    e = new AtomicInteger();
    this.stringTMap = new ConcurrentHashMap<>();
    Arrays.stream(rooms)
        .forEach(room -> this.stringTMap.put(room.getRoomName(), new LinkedList<>()));
  }

  public static MazeGraph createMazeGraph(List<Room> firstRooms, Room... rooms) {
    return new MazeGraph(Objects.requireNonNull(firstRooms), Objects.requireNonNull(rooms));
  }

  @JsonIgnore
  public synchronized Room getFirstRoom() {
    return firstRooms.remove(firstRooms.size() - 1);
  }

  @JsonIgnore
  public int numOfVertices() {
    return rooms.length;
  }

  @JsonIgnore
  public int numOfEdges() {
    return this.e.get();
  }

  public void addEdge(Room v, Room w) {
    if (!stringTMap.containsKey(v.getRoomName()) && !stringTMap.containsKey(w.getRoomName())) {
      throw new IllegalArgumentException();
    }
    this.stringTMap.get(v.getRoomName()).add(w);
    this.stringTMap.get(w.getRoomName()).add(v);
    e.addAndGet(1);
  }

  public synchronized Iterable<Room> adj(Room v) {
    return this.stringTMap.get(v.getRoomName());
  }

  public int degree(Room v) {
    return this.stringTMap.get(v.getRoomName()).size();
  }
}
