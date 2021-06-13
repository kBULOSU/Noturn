package com.noturn.gems.dao;

import com.noturn.gems.NoturnGemsConstants;
import com.noturn.gems.database.MysqlDatabase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class GemsUserDAO {

    private static final String CREATE_TABLE_QUERY = String.format(
            "CREATE TABLE IF NOT EXISTS `%s` (`userName` VARCHAR(16) PRIMARY KEY NOT NULL, `gems` DOUBLE NOT NULL);",
            NoturnGemsConstants.Mysql.Tables.GEMS_TABLE
    );

    private static final String INSERT_OR_UPDATE_QUERY = String.format(
            "INSERT INTO `%s` (`userName`, `gems`) VALUES (?,?) ON DUPLICATE KEY UPDATE `gems`=VALUES(gems);",
            NoturnGemsConstants.Mysql.Tables.GEMS_TABLE
    );

    private static final String SELECT_QUERY = String.format(
            "SELECT `gems` FROM `%s` WHERE `userName` = ? LIMIT 1;",
            NoturnGemsConstants.Mysql.Tables.GEMS_TABLE
    );

    @NonNull
    private final MysqlDatabase database;

    public Double fetch(String userName) {
        Connection connection = database.getConnection();
        if (connection == null) {
            System.out.println("Conexão nula ao tentar dar fetch no usuário " + userName + ".");
            return 0.0;
        }

        try (PreparedStatement statement = connection.prepareStatement(SELECT_QUERY)) {

            statement.setString(1, userName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("gems");
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return 0.0;
    }

    public void insertOrUpdate(String userName, double gems) {
        Connection connection = database.getConnection();
        if (connection == null) {
            System.out.println("Conexão nula ao tentar salvar o usuário " + userName + ".");
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_OR_UPDATE_QUERY)) {

            statement.setString(1, userName);
            statement.setDouble(2, gems);

            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void createTable() {
        Connection connection = database.getConnection();
        if (connection == null) {
            System.out.println("Conexão nula ao tentar criar a database");
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE_QUERY)) {
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
