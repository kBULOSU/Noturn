package com.noturn.spawners.dao;

import com.google.common.base.Enums;
import com.noturn.spawners.Spawner;
import com.noturn.spawners.SpawnersConstants;
import com.noturn.spawners.api.SerializedLocation;
import com.noturn.spawners.database.MysqlDatabase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;

import java.sql.*;

@RequiredArgsConstructor
public class SpawnersDAO {

    private static final String CREATE_TABLE_QUERY = String.format(
            "CREATE TABLE IF NOT EXISTS `%s` (`id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL," +
                    " `ownerName` VARCHAR(16) NOT NULL, `entityType` VARCHAR(255) NOT NULL, `amount` INT NOT NULL, " +
                    "`worldName` VARCHAR(255) NOT NULL, `x` DOUBLE NOT NULL, `y` DOUBLE NOT NULL, " +
                    "`z` DOUBLE NOT NULL);",
            SpawnersConstants.TABLE
    );

    private static final String INSERT_OR_UPDATE_QUERY = String.format(
            "INSERT INTO `%s` (`ownerName`, `entityType`, `amount`, `worldName`, `x`, `y`, `z`) ON DUPLICATE KEY UPDATE " +
                    "`amount`=VALUES(amount), `x`=VALUES(x), `y`=VALUES(y), `z`=VALUES(z);",
            SpawnersConstants.TABLE
    );

    private static final String SELECT_BY_ID_QUERY = String.format(
            "SELECT * FROM `%s` WHERE `id` = ? LIMIT 1;",
            SpawnersConstants.TABLE
    );

    private static final String SELECT_BY_LOCATION_QUERY = String.format(
            "SELECT * FROM `%s` WHERE `worldName` = ? AND `x` = ? AND `y` = ? AND `z` = ? LIMIT 1;",
            SpawnersConstants.TABLE
    );

    private static final String DELETE_QUERY = String.format(
            "DELETE FROM `%s` WHERE `id` = ? LIMIT 1;",
            SpawnersConstants.TABLE
    );

    @NonNull
    private final MysqlDatabase database;

    public Spawner fetch(SerializedLocation location) {
        Connection connection = database.getConnection();
        if (connection == null) {
            System.out.println("Conexão nula ao tentar dar fetch");
            return null;
        }

        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_LOCATION_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            String worldName = location.getWorldName();

            statement.setString(1, worldName);
            statement.setDouble(2, location.getX());
            statement.setDouble(3, location.getY());
            statement.setDouble(4, location.getZ());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");

                    String owner = resultSet.getString("ownerName");
                    int amount = resultSet.getInt("amount");

                    EntityType entityType = Enums.getIfPresent(EntityType.class, resultSet.getString("entityType")).orNull();
                    if (entityType == null) {
                        System.out.println("Entidade inválida. (" + location + ")");
                        return null;
                    }

                    double x = resultSet.getDouble("x");
                    double y = resultSet.getDouble("y");
                    double z = resultSet.getDouble("z");

                    return new Spawner(
                            id,
                            owner,
                            entityType,
                            amount,
                            new SerializedLocation(worldName, x, y, z)
                    );
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public Spawner fetch(Integer id) {
        Connection connection = database.getConnection();
        if (connection == null) {
            System.out.println("Conexão nula ao tentar dar fetch");
            return null;
        }

        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String owner = resultSet.getString("ownerName");
                    int amount = resultSet.getInt("amount");

                    EntityType entityType = Enums.getIfPresent(EntityType.class, resultSet.getString("entityType")).orNull();
                    if (entityType == null) {
                        System.out.println("Entidade inválida.");
                        return null;
                    }

                    double x = resultSet.getDouble("x");
                    double y = resultSet.getDouble("y");
                    double z = resultSet.getDouble("z");

                    return new Spawner(
                            id,
                            owner,
                            entityType,
                            amount,
                            new SerializedLocation(resultSet.getString("worldName"), x, y, z)
                    );
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void insert(Spawner spawner) {
        Connection connection = database.getConnection();
        if (connection == null) {
            System.out.println("Conexão nula ao tentar salvar");
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_OR_UPDATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, spawner.getOwner());
            statement.setString(2, spawner.getEntityType().name());
            statement.setInt(3, spawner.getAmount());

            SerializedLocation serializedLocation = spawner.getSerializedLocation();

            statement.setString(4, serializedLocation.getWorldName());
            statement.setDouble(5, serializedLocation.getX());
            statement.setDouble(6, serializedLocation.getY());
            statement.setDouble(7, serializedLocation.getZ());

            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void delete(int id) {
        Connection connection = database.getConnection();
        if (connection == null) {
            System.out.println("Conexão nula ao tentar deletar");
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void createTable() {
        Connection connection = database.getConnection();
        if (connection == null) {
            System.out.println("Conexão nula ao criar tabela");
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
