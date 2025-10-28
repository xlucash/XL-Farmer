package me.xlucash.farmer.database;

import me.xlucash.farmer.XLFarmerMain;
import me.xlucash.farmer.models.FarmerData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DatabaseManager {
    void initialize();

    void createTablesIfNotExist();

    Connection getConnection() throws SQLException;

    void close();

    void handleDatabaseError(Exception e, XLFarmerMain plugin);

    List<Map.Entry<String, Integer>> getTopFarmers();

    void saveFarmerData(FarmerData farmerData);

    FarmerData getFarmerData(UUID playerUUID);
}
