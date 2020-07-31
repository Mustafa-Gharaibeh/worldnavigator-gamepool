package com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restclass.restcommunicationbackend;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class RestCommunicationBackEnd {
  @JsonProperty("gamerName")
  String gamerName;

  @JsonProperty("mazeId")
  String mazeId;

  @JsonProperty("mazeName")
  String mazeName;

  @JsonProperty("communicationVariable")
  String communicationVariable;

  @JsonProperty("commandListName")
  String commandListName;

  @JsonCreator
  public RestCommunicationBackEnd(
      @JsonProperty("gamerName") String gamerName,
      @JsonProperty("mazeId") String mazeId,
      @JsonProperty("mazeName") String mazeName,
      @JsonProperty("communicationVariable") String communicationVariable,
      @JsonProperty("commandListName") String commandListName) {
    this.gamerName = gamerName;
    this.mazeId = mazeId;
    this.mazeName = mazeName;
    this.communicationVariable = communicationVariable;
    this.commandListName = commandListName;
  }
}
