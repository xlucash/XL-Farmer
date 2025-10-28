package me.xlucash.farmer.database.impl;

import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.xlucash.farmer.XLFarmerMain;
import me.xlucash.farmer.database.Database;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

@Singleton
public class SQLiteDatabase extends Database {

    private HikariDataSource dataSource;

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setup() {
        var config = new HikariConfig();

        config.setPoolName("Farmer pool");

        config.setJdbcUrl("jdbc:sqlite:" + JavaPlugin.getPlugin(XLFarmerMain.class).getDataFolder() + "/farmer.db");
        config.setMaximumPoolSize(50);

        dataSource = new HikariDataSource(config);
        logConnectionSuccess("SQLite");
    }
}
