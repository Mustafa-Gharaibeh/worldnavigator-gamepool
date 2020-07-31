package com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restclass.restcommunicationbackend;

public class RestCommunicationBackEndBuilder {
    private String gamerName;
    private String mazeId;
    private String mazeName;
    private String communicationVariable;
    private String commandListName;

    public RestCommunicationBackEndBuilder setGamerName(String gamerName) {
        this.gamerName = gamerName;
        return this;
    }

    public RestCommunicationBackEndBuilder setMazeId(String mazeId) {
        this.mazeId = mazeId;
        return this;
    }

    public RestCommunicationBackEndBuilder setMazeName(String mazeName) {
        this.mazeName = mazeName;
        return this;
    }

    public RestCommunicationBackEndBuilder setCommunicationVariable(String communicationVariable) {
        this.communicationVariable = communicationVariable;
        return this;
    }

    public RestCommunicationBackEndBuilder setCommandListName(String commandListName) {
        this.commandListName = commandListName;
        return this;
    }

    public RestCommunicationBackEnd createRestCommunicationBackEnd() {
        return new RestCommunicationBackEnd(gamerName, mazeId, mazeName, communicationVariable, commandListName);
    }
}