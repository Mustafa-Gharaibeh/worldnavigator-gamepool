package com.worldnavigator.gamepoolservice.pool.controler.spring.service.mazeservice.wrappermaze;

import com.worldnavigator.gamepoolservice.pool.controler.creator.mazecreator.MazeCreator;
import com.worldnavigator.gamepoolservice.pool.model.gamer.Gamer;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Service
public class WrapperMaze {
  private final MazeCreator mazeCreator;
  private final ConcurrentMap<String, Queue<Maze>> readyMazes;
  private final Integer numberOfMazesInReadyMazesMap;
  private final ConcurrentMap<String, Queue<Maze>> availableMazes;
  private final ConcurrentMap<String, Maze> mazesUnderPlaying;

  public WrapperMaze(
      MazeCreator mazeCreator,
      @Value("${number.of.mazes.in.ready.stage}") Integer numberOfMazesInReadyMazesMap) {
    this.mazeCreator = mazeCreator;
    this.numberOfMazesInReadyMazesMap = numberOfMazesInReadyMazesMap;
    this.readyMazes = new ConcurrentHashMap<>();
    availableMazes = new ConcurrentHashMap<>();
    mazesUnderPlaying = new ConcurrentHashMap<>();
    initialization();
  }

  private void initialization() {
    mazeCreator
        .mazeList()
        .forEach(
            mazeName -> {
              readyMazes.put(mazeName, new ConcurrentLinkedQueue<>());
              availableMazes.put(mazeName, new ConcurrentLinkedQueue<>());
              createNewMazesInReadyMap(mazeName);
            });
  }

  private void createNewMazesInReadyMap(String mazeName) {
    while (readyMazes.get(mazeName).size() != numberOfMazesInReadyMazesMap) {
      readyMazes.get(mazeName).offer(mazeCreator.getMazeByName(mazeName));
    }
  }

  private void insertAvailableMazes(String mazeName) {
    availableMazes.get(mazeName).offer(readyMazes.get(mazeName).poll());
  }

  public synchronized Gamer gamerRegister(String mazeName, String gamerName) {
    if (availableMazes.get(mazeName).isEmpty()) {
      if (readyMazes.get(mazeName).isEmpty()) {
        createNewMazesInReadyMap(mazeName);
      }
      insertAvailableMazes(mazeName);
    }
    Gamer gamer =
        Objects.requireNonNull(availableMazes.get(mazeName).peek())
            .registerGamer(gamerName)
            .orElseThrow(NullPointerException::new);
    if (isCanUnderPlaying(mazeName)) {
      String mazeId = Objects.requireNonNull(availableMazes.get(mazeName).peek()).getMazeId();
      Maze maze = availableMazes.get(mazeName).poll();

      mazesUnderPlaying.put(mazeId, maze);
      mazesUnderPlaying.get(mazeId);
    }
    return gamer;
  }

  private boolean isCanUnderPlaying(String mazeName) {
    Maze maze = Objects.requireNonNull(availableMazes.get(mazeName)).peek();
    return Objects.requireNonNull(maze).getNumberOfGamers().get() == maze.getGamers().size()
        || maze.getWaitingTime().compareTo(LocalTime.now()) >= 0;
  }

  public synchronized boolean isMazeUnderPlaying(final String mazeId) {
    return mazesUnderPlaying.containsKey(mazeId);
  }

  public synchronized Maze mazeUnderPlaying(final String mazeId) {
    return mazesUnderPlaying.get(mazeId.trim());
  }

  public synchronized boolean removeMazeFromUnderPlaying(String mazeId) {
    return Objects.nonNull(mazesUnderPlaying.remove(mazeId));
  }

  public synchronized List<String> mazeList() {
    return mazeCreator.mazeList();
  }

  public synchronized String getMessageToStartPlaying(String mazeName) {
    Maze maze = Objects.requireNonNull(availableMazes.get(mazeName)).peek();
    return String.format(
        "To Start Play you will wait %s sec OR Until Enters %s players",
        Objects.requireNonNull(maze).getWaitingTime().getSecond(), maze.getNumberOfGamers());
  }

  public void leaveMaze(Maze maze) {
    this.mazesUnderPlaying.put(maze.getMazeId(), maze);
  }
}
