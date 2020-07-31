package com.worldnavigator.gamepoolservice.pool.model.dao.mazedao.db.mazelist.dao;

import com.worldnavigator.gamepoolservice.pool.controler.spring.restapi.restclass.mazelistwrapper.MazeListWrapper;


import java.util.List;

public interface MazeDAO {
    void saveMazeName(MazeListWrapper mazeList);

    List<String> getMazeList();
}
