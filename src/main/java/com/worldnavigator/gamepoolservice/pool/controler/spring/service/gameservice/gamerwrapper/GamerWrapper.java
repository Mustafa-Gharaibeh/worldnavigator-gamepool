package com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper;

import com.worldnavigator.gamepoolservice.pool.model.gamer.Gamer;
import lombok.Data;

@Data
public class GamerWrapper {

    private final Gamer gamer;
    private String gamerFighterName;
    private Boolean isUnderRockPaperScissor;
    private String rockPaperScissorChoice;
    private Boolean isGamerInMaze;
    private String lastListCommandUsed;
    private String status;

    public GamerWrapper(Gamer gamer) {
        this.gamer = gamer;
        isUnderRockPaperScissor = false;
        isGamerInMaze = false;
        gamerFighterName = "Nobody";
        rockPaperScissorChoice = null;
        lastListCommandUsed = "default";
        status = "";
    }
}
