package com.noturn.ranks.dao;

import com.noturn.ranks.Rank;
import com.noturn.ranks.RankConstants;
import com.noturn.ranks.RanksRegistry;
import com.noturn.ranks.database.MysqlDatabase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class PlayerRanksDAO {

    private static final String CREATE_TABLE_QUERY = String.format(
            "CREATE TABLE IF NOT EXISTS `%s` (`userName` VARCHAR(16) PRIMARY KEY NOT NULL, `rankId` INT NOT NULL);",
            RankConstants.SQL_TABLE
    );

    private static final String INSERT_OR_UPDATE_QUERY = String.format(
            "INSERT INTO `%s` (`userName`, `rankId`) VALUES (?,?) ON DUPLICATE KEY UPDATE `rankId`=VALUES(rankId);",
            RankConstants.SQL_TABLE
    );

    private static final String SELECT_SQL_QUERY = String.format(
            "SELECT `rankId` FROM `%s` WHERE `userName` = ? LIMIT 1;",
            RankConstants.SQL_TABLE
    );

    @NonNull
    private final MysqlDatabase database;

    public Rank fetch(String userName) {
        Connection connection = database.getConnection();
        if (connection == null) {
            System.out.println("Conexão nula ao tentar dar fetch no usuário " + userName + ".");
            return RanksRegistry.DEFAULT_RANK;
        }

        try (PreparedStatement statement = connection.prepareStatement(SELECT_SQL_QUERY)) {

            statement.setString(1, userName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int rankId = resultSet.getInt("rankId");

                    return RanksRegistry.getById(rankId);
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return RanksRegistry.DEFAULT_RANK;
    }

    public void insertOrUpdate(String userName, Rank rank) {
        Connection connection = database.getConnection();
        if (connection == null) {
            System.out.println("Conexão nula ao tentar salvar o usuário " + userName + ".");
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_OR_UPDATE_QUERY)) {

            statement.setString(1, userName);
            statement.setInt(2, rank.getId());

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
