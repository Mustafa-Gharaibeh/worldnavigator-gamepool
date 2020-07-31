package com.worldnavigator.gamepoolservice.pool.controler.creator.mazecreator;

import com.worldnavigator.gamepoolservice.pool.model.dao.mazedao.MazeDAO;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public final class MazeCreator {

    private final MazeDAO mazeDAO;

    private MazeCreator(@Qualifier("file") MazeDAO mazeDAO) {
        this.mazeDAO = mazeDAO;
    }

    public void saveMaze(Maze maze) {
        this.mazeDAO.saveMaze(Objects.requireNonNull(maze));
    }

    public Maze getMazeByName(String mazeName) {
        return this.mazeDAO.getMazeByName(mazeName).orElseThrow(NullPointerException::new);
    }

    public List<String> mazeList() {
        return this.mazeDAO.mazeList();
    }
}
