package com.worldnavigator.gamepoolservice.pool.model.gamer;

import com.worldnavigator.gamepoolservice.pool.model.material.Material;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import lombok.ToString;

import java.util.*;

@ToString
public final class Gamer {

  private final String gamerName;
  private final Map<String, Material> materialMap;
  private final List<Direction> directions;
  private Direction currentDirection;
  private Direction oppositeDirection;
  private String currentMazeId;

  private Gamer(String gamerName, Map<String, Material> materialMap) {
    this.gamerName = gamerName;
    this.materialMap = materialMap;
    this.directions = new LinkedList<>();
    this.setDirections();
  }

  public static Gamer createGamer(String gamerName, Gold gold) {
    Objects.requireNonNull(gamerName);
    Objects.requireNonNull(gold);
    Map<String, Material> map = new HashMap<>();
    map.put(gold.name(), gold);
    return new Gamer(gamerName, map);
  }

  public static Gamer createGamer(String gamerName, Map<String, Material> materialMap) {
    return new Gamer(Objects.requireNonNull(gamerName), Objects.requireNonNull(materialMap));
  }

  public String getCurrentMazeId() {
    return currentMazeId;
  }

  public void addItem(Item item) {
    this.materialMap.put(Objects.requireNonNull(item).name().trim(), item);
  }

  public void addAmountOfGold(Integer amountOfGold) {
    ((Gold) this.materialMap.get("Gold")).setAmount(Objects.requireNonNull(amountOfGold));
  }

  public void addAmountOfGold(Gold gold) {
    Objects.requireNonNull(gold);
    ((Gold) this.materialMap.get("Gold")).setAmount(gold.getAmount());
  }

  public void setCurrentMazeId(final String currentMazeId) {
    this.currentMazeId = currentMazeId;
  }

  public Optional<Item> deleteItem(String itemName) {
    return Optional.ofNullable((Item) this.materialMap.remove(itemName));
  }

  public Map<String, Material> getMaterialMap() {
    return materialMap;
  }

  public String getGamerName() {
    return gamerName;
  }

  public Direction getOppositeDirection() {
    return oppositeDirection;
  }

  private void setOppositeDirection(Direction oppositeDirection) {
    this.oppositeDirection = oppositeDirection;
  }

  public List<Direction> getDirections() {
    return directions;
  }

  public Direction getCurrentDirection() {
    return currentDirection;
  }

  public void setCurrentDirection(Direction currentDirection) {
    this.currentDirection = currentDirection;

    if (currentDirection.equals(Direction.NORTH)) {
      setOppositeDirection(Direction.SOUTH);
    } else if (currentDirection.equals(Direction.SOUTH)) {
      setOppositeDirection(Direction.NORTH);
    } else if (currentDirection.equals(Direction.EAST)) {
      setOppositeDirection(Direction.WEST);
    } else {
      setOppositeDirection(Direction.EAST);
    }
  }

  private void setDirections() {
    directions.add(Direction.NORTH);
    directions.add(Direction.EAST);
    directions.add(Direction.SOUTH);
    directions.add(Direction.WEST);
    this.setCurrentDirection(Direction.NORTH);
    this.setOppositeDirection(Direction.SOUTH);
  }

  public Integer getTotalAmountOfGoldWithItems() {
    int goldTotal = 0;
    for (Map.Entry<String, Material> entry : materialMap.entrySet()) {
      if (entry.getValue() instanceof Gold) {
        goldTotal = goldTotal + ((Gold) entry.getValue()).getAmount();
      } else if (entry.getValue() instanceof Item) {
        goldTotal = goldTotal + ((Item) entry.getValue()).price().getAmount();
      }
    }
    return goldTotal;
  }
}
