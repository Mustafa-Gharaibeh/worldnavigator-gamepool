package com.worldnavigator.gamepoolservice.pool.createmaze;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.worldnavigator.gamepoolservice.pool.model.dao.mazedao.MazeDAO;
import com.worldnavigator.gamepoolservice.pool.model.dao.mazedao.file.MazeDAOFileImpl;
import com.worldnavigator.gamepoolservice.pool.model.gamer.Direction;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import com.worldnavigator.gamepoolservice.pool.model.material.item.flashlight.Flashlight;
import com.worldnavigator.gamepoolservice.pool.model.material.item.key.Key;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import com.worldnavigator.gamepoolservice.pool.model.maze.MazeGraph;
import com.worldnavigator.gamepoolservice.pool.model.room.Room;
import com.worldnavigator.gamepoolservice.pool.model.wall.Wall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.chest.Chest;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.door.Door;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.mirror.Mirror;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.nothing.PlainWall;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.painting.Painting;
import com.worldnavigator.gamepoolservice.pool.model.wall.onwall.seller.Seller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateMazes {

  public static void main(String[] args) {
    MazeDAO mazeDAO =
        new MazeDAOFileImpl(
            new ObjectMapper(),
            "/home/mustafa/Desktop/JavaEE8/spring/security/worldnavigatorassignment/src/main/resources/static/mazeFile");
    mazeDAO.saveMaze(maze1());
    Maze maze = mazeDAO.getMazeByName("maze1").get();
    System.out.println(maze.getMazeName());
    System.out.println(mazeDAO.mazeList());
    // System.out.println(maze.getCurrentRoom().getRoomLightingStatus().isARoomDark());
  }

  private static Maze maze1() {
    Key redKey = Key.createKey("red key", Gold.createGold(2));
    Key greenKey = Key.createKey("green key", Gold.createGold(1));
    Key finalKey = Key.createKey("final key", Gold.createGold(4));
    Key chestKey = Key.createKey("chest key", Gold.createGold(1));

    Wall w11 = Wall.createWall(Door.createUnLockDoor(Boolean.FALSE, "First Door"));
    Wall w12 = Wall.createWall(Painting.createPaintingWithHiddenItem(redKey));
    Wall w13 = Wall.createWall(PlainWall.createPlainWall());
    Wall w14 = Wall.createWall(Mirror.createMirrorHasHiddenItem(greenKey));
    Room r1 = Room.createRoomWithLights("Room1", createMapWalls(w11, w12, w13, w14));

    Wall w21 = Wall.createWall(Door.createLockDoor(finalKey, Boolean.TRUE, "Final Door"));
    Wall w22 = Wall.createWall(Door.createLockDoor(redKey, Boolean.FALSE, "Red Door"));
    Wall w23 = w11;
    Wall w24 = Wall.createWall(Door.createLockDoor(greenKey, Boolean.FALSE, "Green Door"));
    Room r2 = Room.createRoomWithLights("Room2", createMapWalls(w21, w22, w23, w24));

    Map<String, Item> itemMap = new HashMap<>();
    Item item = Flashlight.createFlashlight("flashlight", Gold.createGold(3));
    itemMap.put(item.name(), item);
    Wall w31 = Wall.createWall(Painting.createPaintingWithHiddenItem(chestKey));
    Wall w32 = Wall.createWall(Seller.createSeller(itemMap));
    Wall w33 = Wall.createWall(PlainWall.createPlainWall());
    Wall w34 = w22;
    Room r3 = Room.createRoomWithLights("Room3", createMapWalls(w31, w32, w33, w34));

    Map<String, Material> materialMap = new HashMap<>();
    materialMap.put(finalKey.name(), finalKey);
    Wall w41 = Wall.createWall(Chest.createChest(chestKey, materialMap));
    Wall w42 = w24;
    Wall w43 = Wall.createWall(PlainWall.createPlainWall());
    Wall w44 = Wall.createWall(PlainWall.createPlainWall());
    Room r4 = Room.createRoomWithoutLights("Room4", createMapWalls(w41, w42, w43, w44));
    List<Room> firstRooms = Collections.singletonList(r1);
    MazeGraph mazeGraph = MazeGraph.createMazeGraph(firstRooms, r1, r2, r3, r4);
    mazeGraph.addEdge(r1, r2);
    mazeGraph.addEdge(r2, r3);
    mazeGraph.addEdge(r2, r1);
    mazeGraph.addEdge(r2, r4);
    mazeGraph.addEdge(r3, r2);
    mazeGraph.addEdge(r4, r2);

    return Maze.createMaze("maze1", Gold.createGold(20), (long) 20, (long) 1, mazeGraph, 1);
  }

  private static Map<Direction, Wall> createMapWalls(Wall w1, Wall w2, Wall w3, Wall w4) {
    Map<Direction, Wall> wallMap = new HashMap<>();
    wallMap.put(Direction.NORTH, w1);
    wallMap.put(Direction.EAST, w2);
    wallMap.put(Direction.SOUTH, w3);
    wallMap.put(Direction.WEST, w4);
    return wallMap;
  }
}
