package com.worldnavigator.gamepoolservice.pool.controler.spring.service.commandpoolutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.worldnavigator.gamepoolservice.pool.model.wall.onwall.TypeOnWall.*;

public class CommandPoolUtil {
  private static final ConcurrentMap<String, List<String>> commands = new ConcurrentHashMap<>();

  private CommandPoolUtil() {}

  public static synchronized List<String> commandList(String command) {
    return commands.get(command);
  }

  static {
    List<String> isStart = Collections.singletonList("isStart");
    commands.put("isStart",isStart);
    List<String> defaultList =
        new ArrayList<>(
            Arrays.asList(
                "left",
                "right",
                "look",
                "quit",
                "restart",
                "status",
                "switchLights",
                "use flashlight"));
    commands.put("default", defaultList);
    commands.put(NOTHING.getCheckName(), defaultList);

    List<String> doorList =
        new ArrayList<>(
            Arrays.asList(
                "left",
                "right",
                "look",
                "quit",
                "restart",
                "status",
                "switchLights",
                "use flashlight",
                "check door",
                "open",
                "use <name> key",
                "forward"));
    commands.put(DOOR.getCheckName(), doorList);

    List<String> mirrorList =
        new ArrayList<>(
            Arrays.asList(
                "left",
                "right",
                "look",
                "quit",
                "restart",
                "status",
                "switchLights",
                "use flashlight",
                "check mirror"));
    commands.put(MIRROR.getCheckName(), mirrorList);

    List<String> paintingList =
        new ArrayList<>(
            Arrays.asList(
                "left",
                "right",
                "look",
                "quit",
                "restart",
                "status",
                "switchLights",
                "use flashlight",
                "check painting"));
    commands.put(PAINTING.getCheckName(), paintingList);

    List<String> chestList =
        new ArrayList<>(
            Arrays.asList(
                "left",
                "right",
                "look",
                "quit",
                "restart",
                "status",
                "switchLights",
                "use flashlight",
                "check chest",
                "use <name> key"));
    commands.put(CHEST.getCheckName(), chestList);

    List<String> sellerList =
        new ArrayList<>(
            Arrays.asList(
                "left",
                "right",
                "look",
                "quit",
                "restart",
                "status",
                "switchLights",
                "use flashlight",
                "trade"));
    commands.put(SELLER.getCheckName(), sellerList);

    List<String> tradeList =
        new ArrayList<>(Arrays.asList("buy <item>", "sell <item>", "list", "finish trade"));
    commands.put("trade", tradeList);

    List<String> backward =
        new ArrayList<>(
            Arrays.asList(
                "left",
                "right",
                "look",
                "quit",
                "restart",
                "status",
                "switchLights",
                "use flashlight",
                "backward"));
    commands.put("backward", backward);

    List<String> forwardAndHasOppositeDoor =
        new ArrayList<>(
            Arrays.asList(
                "left",
                "right",
                "look",
                "quit",
                "restart",
                "status",
                "switchLights",
                "use flashlight",
                "forward",
                "backward"));
    commands.put("forward", forwardAndHasOppositeDoor);

    List<String> rockPaperScissor = Arrays.asList("rock", "paper", "scissor");
    commands.put("RockPaperScissor", rockPaperScissor);

    List<String> endMessage = Collections.singletonList("endMessage");
    commands.put("endMessage",endMessage);
  }
}
