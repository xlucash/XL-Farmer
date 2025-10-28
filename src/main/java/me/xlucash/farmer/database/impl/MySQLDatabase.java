package me.xlucash.farmer.database.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import me.xlucash.farmer.config.ConfigContainer;
import me.xlucash.farmer.database.Database;

import java.sql.Connection;
import java.sql.SQLException;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MySQLDatabase extends Database {

    private HikariDataSource dataSource;
    private final ConfigContainer configContainer;

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

        config.setConnectionTestQuery("SELECT 1");
        config.setPoolName("Farmer pool");

        config.setDriverClassName("com.mysql.jdbc.Driver");

        config.setJdbcUrl("jdbc:mysql://" + configContainer.mysqlHost + ":" + configContainer.mysqlPort + "/" + configContainer.mysqlDatabase);
        config.setUsername(configContainer.mysqlUsername);
        config.setPassword(configContainer.mysqlPassword);
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(60000);
        config.setIdleTimeout(1740000);
        config.setMaxLifetime(1740000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("tcpKeepAlive", "true");
        config.addDataSourceProperty("autoReconnect", "true");

        dataSource = new HikariDataSource(config);
        logConnectionSuccess("MySQL");
    }
}
