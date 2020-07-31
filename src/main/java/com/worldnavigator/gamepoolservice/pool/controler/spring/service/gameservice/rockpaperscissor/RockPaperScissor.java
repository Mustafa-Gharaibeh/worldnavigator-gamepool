package com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.rockpaperscissor;

import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;

import java.util.Arrays;
import java.util.List;

public class RockPaperScissor {
  private final GamerWrapper gamer1;
  private final GamerWrapper gamer2;
  private final List<String> rockPaperScissor;

  public RockPaperScissor(GamerWrapper gamer1, GamerWrapper gamer2) {
    this.gamer1 = gamer1;
    this.gamer2 = gamer2;
    rockPaperScissor =
        Arrays.asList(Choices.ROCK.name(), Choices.PAPER.name(), Choices.SCISSOR.name());
  }

  public List<String> getRockPaperScissor() {
    return rockPaperScissor;
  }

  public String play() {
    String c1 = gamer1.getRockPaperScissorChoice();
    String c2 = gamer2.getRockPaperScissorChoice();

    if (c1.equals(c2)) {
      return "SYMMETRY";
    } else if (c1.equals(Choices.ROCK.name()) && c2.equals(Choices.SCISSOR.name())) {
      return gamer1.getGamer().getGamerName();
    } else if (c1.equals(Choices.PAPER.name()) && c2.equals(Choices.SCISSOR.name())) {
      return gamer2.getGamer().getGamerName();
    } else if (c1.equals(Choices.ROCK.name()) && c2.equals(Choices.PAPER.name())) {
      return gamer2.getGamer().getGamerName();
    }
    return "WRONG";
  }

  private enum Choices {
    ROCK,
    PAPER,
    SCISSOR;
  }
}
