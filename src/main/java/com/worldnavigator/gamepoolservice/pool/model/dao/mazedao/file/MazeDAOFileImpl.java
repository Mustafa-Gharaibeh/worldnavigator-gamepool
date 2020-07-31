package com.worldnavigator.gamepoolservice.pool.model.dao.mazedao.file;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restclass.mazelistwrapper.MazeListWrapper;
import com.worldnavigator.gamepoolservice.pool.model.dao.mazedao.MazeDAO;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service("file")
public final class MazeDAOFileImpl implements MazeDAO {

  private final ObjectMapper mapper;

  private final String mazeFileLocation;

  public MazeDAOFileImpl(ObjectMapper mapper, @Value("${maze.file}") String mazeFileLocation) {
    this.mapper = mapper;
    this.mazeFileLocation = "/home/mustafa/Desktop/JavaEE8/spring/gamepoolservice/src/main/resources/static/mazeFile0";
    this.mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
  }

  @Override
  public void saveMaze(Maze maze) {
    Objects.requireNonNull(maze, "The maze Object is null");
    File file = new File(String.format("%s/%s.json", mazeFileLocation, maze.getMazeName()));
    try {
      List<String> mazeList = mazeList();
      mazeList.add(maze.getMazeName());
      MazeListWrapper mazeList1 = new MazeListWrapper(mazeList);
      mapper
          .writerWithDefaultPrettyPrinter()
          .writeValue(new File(String.format("%s/listOfMazes.json", mazeFileLocation)), mazeList1);

    } catch (NullPointerException e) {
      MazeListWrapper mazeList1 = new MazeListWrapper(new ArrayList<>());
      try {
        mapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(
                new File(String.format("%s/listOfMazes.json", mazeFileLocation)), mazeList1);
      } catch (IOException e1) {
        Logger logger = Logger.getLogger("IO Exception");
        logger.info(e1.getMessage());
      }
    } catch (IOException e) {
      Logger logger = Logger.getLogger("IO Exception");
      logger.info(e.getMessage());
    }

    try {
      mapper.writerWithDefaultPrettyPrinter().writeValue(file, maze);
    } catch (IOException e) {
      Logger logger = Logger.getLogger("IO Exception");
      logger.info(e.getMessage());
    }
  }

  @Override
  public Optional<Maze> getMazeByName(String mazeName) {
    File file = new File(String.format("%s/%s.json", mazeFileLocation, mazeName));

    Optional<Maze> mazeOptional = Optional.empty();
    try {
      mazeOptional = Optional.of(mapper.readValue(file, Maze.class));
    } catch (IOException e) {
      Logger logger = Logger.getLogger("IO Exception");
      logger.info(e.getMessage());
    }
    return mazeOptional;
  }

  @Override
  public List<String> mazeList() {
    File file = new File(String.format("%s/listOfMazes.json", mazeFileLocation));
    Optional<MazeListWrapper> mazeList = Optional.empty();
    try {
      mazeList = Optional.of(mapper.readValue(file, MazeListWrapper.class));
    } catch (IOException e) {
      Logger logger = Logger.getLogger("IO Exception");
      logger.info(e.getMessage());
    }
    return mazeList
        .orElseThrow(() -> new NullPointerException("The maze list not found"))
        .getMazeList();
  }
}
