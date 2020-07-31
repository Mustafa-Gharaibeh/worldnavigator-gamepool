package com.worldnavigator.gamepoolservice.pool.model.dao.mazedao;

import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;

import java.util.List;
import java.util.Optional;

public interface MazeDAO {
    void saveMaze(Maze maze);

    Optional<Maze> getMazeByName(String mazeName);

    List<String> mazeList();
}
