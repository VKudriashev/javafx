package com.testfxlab.dao;

import com.testfxlab.model.Human;
import com.testfxlab.utils.DbConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HumanDaoImpl implements HumanDao {

    @Override
    public List<Human> getPeople() throws SQLException {
        List<Human> people = new ArrayList<>();
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM people ORDER BY first_name")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Human human = new Human();
                human.setHumanId(resultSet.getInt("id"));
                human.setFirstName(resultSet.getString("first_name"));
                human.setLastName(resultSet.getString("last_name"));
                human.setBirthdate(resultSet.getDate("birthdate").toLocalDate());
                people.add(human);
            }
            return people;
        } catch (SQLException e) {
            System.out.println("Failed to execute getPeople query " + e);
            throw e;
        }
    }

    @Override
    public void update(Human human) throws SQLException {
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE people SET first_name=?, last_name=?, birthdate=? WHERE id=?")) {
            preparedStatement.setString(1, human.getFirstName());
            preparedStatement.setString(2, human.getLastName());
            preparedStatement.setDate(3, Date.valueOf(human.getBirthdate()));
            preparedStatement.setInt(4, human.getHumanId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to execute update human query " + e);
            throw e;
        }
    }

    @Override
    public int add(Human human) throws SQLException {
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO people (first_name, last_name, birthdate) VALUES (?, ?, ?)"
                     , Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, human.getFirstName());
            preparedStatement.setString(2, human.getLastName());
            preparedStatement.setDate(3, Date.valueOf(human.getBirthdate()));
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : -1;
        } catch (SQLException e) {
            System.out.println("Failed to execute add human query " + e);
            throw e;
        }
    }

    @Override
    public void delete(int humanId) throws SQLException {
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM people WHERE id=?")) {
            preparedStatement.setInt(1, humanId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to execute delete human query " + e);
            throw e;
        }
    }
}
