package com.worldnavigator.gamepoolservice.pool.controler.command.quitcommand;

import com.worldnavigator.gamepoolservice.pool.controler.command.commandinterface.Command;
import com.worldnavigator.gamepoolservice.pool.controler.spring.service.gameservice.gamerwrapper.GamerWrapper;
import com.worldnavigator.gamepoolservice.pool.model.material.Material;
import com.worldnavigator.gamepoolservice.pool.model.material.gold.Gold;
import com.worldnavigator.gamepoolservice.pool.model.material.item.Item;
import com.worldnavigator.gamepoolservice.pool.model.maze.Maze;
import com.worldnavigator.gamepoolservice.pool.model.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class QuitCommand implements Command {
    private final Maze maze;
    private final GamerWrapper gamerWrapper;

    public QuitCommand(Maze maze, GamerWrapper gamerWrapper) {
        this.maze = maze;
        this.gamerWrapper = gamerWrapper;
    }

    @Override
    public void execute() {
        maze.removeGamer(gamerWrapper.getGamer());
        ConcurrentMap<String, Material> materialMap =
                (ConcurrentMap<String, Material>) gamerWrapper.getGamer().getMaterialMap();
        List<Item> items = new ArrayList<>();

        distributeGold((Gold) materialMap.remove("Gold"));
        materialMap.forEach(
                (name, item) -> {
                    if (item instanceof Item) {
                        items.add((Item) item);
                    }
                });
        this.distributeItemsInRooms(items);

        gamerWrapper.setLastListCommandUsed("endMessage");
        gamerWrapper.setStatus(String.format("You Out The Game Under %s", maze.getMazeName()));
    }

    private Iterable<Room> getRoomsThatBehindTheGamerRoom() {
        return maze.getMazeGraph().adj(maze.getCurrentRoom(gamerWrapper.getGamer()));
    }

    private void distributeGold(Gold gold) {
        int amountOfGoldForEachOne = gold.getAmount() / maze.getGamers().size();
        maze.getGamers().forEach((name, player) -> player.addAmountOfGold(amountOfGoldForEachOne));
    }

    private void distributeItemsInRooms(List<Item> items) {
        for (Room room : getRoomsThatBehindTheGamerRoom()) {
            room.getWalls()
                    .forEach(
                            ((direction, wall) -> {
                                if (wall.getOnWall().typeOnWall().isAcquirable()) {
                                    wall.getOnWall().getAcquirable().addItems(items);
                                    items.clear();
                                }
                            }));
            if (items.isEmpty()) {
                break;
            }
        }
    }
}
