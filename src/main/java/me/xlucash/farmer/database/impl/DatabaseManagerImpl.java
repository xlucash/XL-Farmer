package me.xlucash.farmer.database.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.xlucash.farmer.XLFarmerMain;
import me.xlucash.farmer.database.Database;
import me.xlucash.farmer.database.DatabaseManager;
import me.xlucash.farmer.factories.DatabaseFactory;
import me.xlucash.farmer.models.FarmerData;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class DatabaseManagerImpl implements DatabaseManager {

    private final XLFarmerMain plugin;
    private final DatabaseFactory databaseFactory;
    private Database database;

    @Override
    public void initialize() {
        this.database = databaseFactory.createDatabase();
        database.setup();
        createTablesIfNotExist();
    }

    @Override
    public void createTablesIfNotExist() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS farming_data (" +
                            "player_uuid VARCHAR(36) PRIMARY KEY," +
                            "level INT NOT NULL," +
                            "experience INT NOT NULL," +
                            "fortune_level INT NOT NULL," +
                            "claimed_rewards TEXT" +
                            ");"
            );
            statement.executeUpdate();
        } catch (SQLException e) {
            handleDatabaseError(e, plugin);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return database.getConnection();
    }

    @Override
    public void close() {
        if (database != null) {
            database.closeConnection();
        }
    }

    @Override
    public void handleDatabaseError(Exception e, XLFarmerMain plugin) {
        plugin.getLogger().severe(e.getMessage());
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }

    @Override
    public List<Map.Entry<String, Integer>> getTopFarmers() {
        List<Map.Entry<String, Integer>> topPlayers = new ArrayList<>();

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT player_uuid, level FROM farming_data ORDER BY level DESC LIMIT 10"
            );
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String playerUUID = resultSet.getString("player_uuid");
                int level = resultSet.getInt("level");

                String playerName = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)).getName();
                topPlayers.add(new AbstractMap.SimpleEntry<>(playerName, level));
            }
        } catch (SQLException e) {
            handleDatabaseError(e, plugin);
        }

        return topPlayers;
    }

    @Override
    public void saveFarmerData(FarmerData farmerData) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "REPLACE INTO farming_data (player_uuid, level, experience, fortune_level, claimed_rewards) " +
                            "VALUES (?, ?, ?, ?, ?)"
            );

            statement.setString(1, farmerData.getPlayerUUID().toString());
            statement.setInt(2, farmerData.getLevel());
            statement.setInt(3, farmerData.getExperience());
            statement.setInt(4, farmerData.getFortuneLevel());
            statement.setString(5, String.join(
                            ",",
                            farmerData.getClaimedRewards().stream()
                                    .map(String::valueOf).toArray(String[]::new)
                    )
            );
            statement.executeUpdate();
        } catch (SQLException e) {
            handleDatabaseError(e, plugin);
        }
    }

    @Override
    public FarmerData getFarmerData(UUID playerUUID) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM farming_data WHERE player_uuid = ?"
            );
            statement.setString(1, playerUUID.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int level = resultSet.getInt("level");
                int experience = resultSet.getInt("experience");
                int fortuneLevel = resultSet.getInt("fortune_level");

                String claimedRewardsString = resultSet.getString("claimed_rewards");
                Set<Integer> claimedRewards = claimedRewardsString != null ? Arrays.stream(claimedRewardsString.split(","))
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet()) : new HashSet<>();

                return new FarmerData(playerUUID, level, experience, fortuneLevel, claimedRewards);
            }
        } catch (SQLException e) {
            handleDatabaseError(e, plugin);
        }

        return new FarmerData(playerUUID, 1, 0, 0, new HashSet<>());
    }
}
