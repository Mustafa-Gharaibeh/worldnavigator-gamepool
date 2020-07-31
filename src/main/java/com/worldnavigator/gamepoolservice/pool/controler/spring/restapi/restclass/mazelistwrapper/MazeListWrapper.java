package com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restclass.mazelistwrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class MazeListWrapper {
  @JsonProperty("mazeList")
  List<String> mazeList;

  public MazeListWrapper(@JsonProperty("mazeList") List<String> mazeList) {
    this.mazeList = mazeList;
  }
}
