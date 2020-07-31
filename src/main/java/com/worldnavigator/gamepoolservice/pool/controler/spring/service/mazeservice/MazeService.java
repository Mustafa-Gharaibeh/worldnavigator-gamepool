package com.worldnavigator.gamepoolservice.pool.controler.spring.service.mazeservice;

import com.worldnavigator.gamepoolservice.pool.controler.spring.service.mazeservice.wrappermaze.WrapperMaze;
import com.worldnavigator.gamepoolservice.pool.model.gamer.Gamer;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MazeService {
  private final WrapperMaze wrapperMaze;
  private final ConcurrentMap<String, LocalTime> mazesTime;

  public MazeService(WrapperMaze wrapperMaze) {
    this.wrapperMaze = wrapperMaze;
    mazesTime = new ConcurrentHashMap<>();
  }

  public Gamer gamerRegister(String mazeName, String gamerName) {
    return wrapperMaze.gamerRegister(
        Objects.requireNonNull(mazeName), Objects.requireNonNull(gamerName));
  }

  public List<String> mazeList() {
    return wrapperMaze.mazeList();
  }

  public boolean isStart(String mazeId) {
    boolean isStart = wrapperMaze.isMazeUnderPlaying(Objects.requireNonNull(mazeId));
    if (isStart && !mazesTime.containsKey(mazeId)) {
      mazesTime.put(
          mazeId,
          LocalTime.now()
              .plusMinutes(wrapperMaze.mazeUnderPlaying(mazeId).getPlayingTimeMin()));
    }
    return isStart;
  }

  public boolean isTimeUp(String mazeId) {
    return mazesTime.get(Objects.requireNonNull(mazeId)).compareTo(LocalTime.now()) >= 0;
  }

  public void close(String mazeId) {
    // TODO
    wrapperMaze.removeMazeFromUnderPlaying(mazeId);
  }

  public Maze getMaze(String mazeId) {
    return wrapperMaze.mazeUnderPlaying(Objects.requireNonNull(mazeId));
  }

  public synchronized String getTimeToStartPlaying(String mazeName) {
    return wrapperMaze.getMessageToStartPlaying(mazeName);
  }

  public synchronized void leaveMaze(Maze maze){
    wrapperMaze.leaveMaze(maze);
  }
}
