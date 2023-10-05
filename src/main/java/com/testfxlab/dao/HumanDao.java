package com.testfxlab.dao;

import com.testfxlab.model.Human;

import java.sql.SQLException;
import java.util.List;

public interface HumanDao {

    List<Human> getPeople() throws SQLException;

    void update(Human human) throws SQLException;

    int add(Human human) throws SQLException;

    void delete(int humaId) throws SQLException;
}
