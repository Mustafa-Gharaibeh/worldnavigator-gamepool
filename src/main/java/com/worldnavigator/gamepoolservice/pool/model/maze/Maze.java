package com.worldnavigator.gamepoolservice.pool.model.maze;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.worldnavigator.gamepoolservice.pool.model.gamer.Gamer;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.room.Room;
import lombok.Value;

import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Value
public class Maze {
  @JsonIgnore String mazeId;
  @JsonIgnore static Integer count = 0;

  @JsonProperty("mazeName")
  String mazeName;

  @JsonProperty("mazeGraph")
  MazeGraph mazeGraph;

  @JsonProperty("initialGamerGold")
  Gold initialGamerGold;

  @JsonProperty("playingTimeMin")
  AtomicLong playingTimeMin;

  @JsonProperty("waitingTimeInSec")
  AtomicLong waitingTimeInSec;

  @JsonIgnore ConcurrentMap<String, Gamer> gamers;

  @JsonIgnore Map<String, Room> theLocationOfTheGamers;

  @JsonProperty("numberOfGamers")
  AtomicInteger numberOfGamers;

  @JsonIgnore LocalTime waitingTime;

  @JsonCreator
  private Maze(
      @JsonProperty("mazeName") String mazeName,
      @JsonProperty("initialGamerGold") Gold initialGamerGold,
      @JsonProperty("playingTimeMin") Long playingTimeMin,
      @JsonProperty("mazeGraph") MazeGraph mazeGraph,
      @JsonProperty("numberOfGamers") AtomicInteger numberOfGamers,
      @JsonProperty("waitingTimeInSec") AtomicLong waitingTimeInSec) {
    this.mazeName = mazeName;
    this.mazeGraph = mazeGraph;
    this.initialGamerGold = initialGamerGold;
    this.playingTimeMin = new AtomicLong(playingTimeMin);
    this.gamers = new ConcurrentHashMap<>();
    this.theLocationOfTheGamers = new ConcurrentHashMap<>();
    this.numberOfGamers = numberOfGamers;
    this.waitingTimeInSec = waitingTimeInSec;
    mazeId = String.format("%s%d", mazeName, ++count);
    waitingTime = LocalTime.now().plusSeconds(waitingTimeInSec.get());
  }

  public static Maze createMaze(
      String mazeName,
      Gold initialGamerGold,
      Long playingTimeMin,
      Long waitingTimeInSec,
      MazeGraph mazeGraph,
      Integer numberOfGamers) {
    if (Objects.requireNonNull(numberOfGamers)
        != Objects.requireNonNull(mazeGraph).getFirstRooms().size()) {
      throw new IllegalArgumentException(
          "Number of Initial rooms will be equal with number of gamers");
    }
    return new Maze(
        Objects.requireNonNull(mazeName),
        Objects.requireNonNull(initialGamerGold),
        Objects.requireNonNull(playingTimeMin),
        mazeGraph,
        new AtomicInteger(numberOfGamers),
        new AtomicLong(waitingTimeInSec));
  }

  public synchronized long getPlayingTimeMin() {
    return playingTimeMin.get();
  }

  public synchronized Gold getInitialGamerGold() {
    return initialGamerGold;
  }

  public synchronized MazeGraph getMazeGraph() {
    return mazeGraph;
  }

  public synchronized Room getCurrentRoom(Gamer gamer) {
    return theLocationOfTheGamers.get(gamer.getGamerName());
  }

  public synchronized void setCurrentRoom(Gamer gamer, Room currentRoom) {
    theLocationOfTheGamers.put(gamer.getGamerName(), currentRoom);
  }

  public synchronized Optional<Gamer> registerGamer(String gamerName) {
    if (this.gamers.size() == this.numberOfGamers.get()) {
      return Optional.empty();
    }
    Gamer gamer = Gamer.createGamer(gamerName, initialGamerGold);
    this.gamers.put(gamerName, gamer);
    gamer.setCurrentMazeId(mazeId);
    theLocationOfTheGamers.put(gamer.getGamerName(), this.mazeGraph.getFirstRoom());
    return Optional.of(gamer);
  }

  public synchronized ConcurrentMap<String, Gamer> getGamers() {
    return gamers;
  }

  public synchronized void removeGamer(Gamer gamer) {
    gamers.remove(gamer.getGamerName());
  }

  public synchronized String getMazeName() {
    return mazeName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Maze maze = (Maze) o;
    return Objects.equals(mazeName, maze.mazeName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mazeName);
  }
}
