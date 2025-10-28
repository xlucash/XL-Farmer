package me.xlucash.farmer.factories;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.xlucash.farmer.config.ConfigContainer;
import me.xlucash.farmer.database.Database;
import me.xlucash.farmer.database.impl.MySQLDatabase;
import me.xlucash.farmer.database.impl.SQLiteDatabase;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class DatabaseFactory {

    private final ConfigContainer config;

    public Database createDatabase() {
        var databaseType = config.databaseType;
        if ("mysql".equalsIgnoreCase(databaseType)) {
            return new MySQLDatabase(config);
        } else {
            return new SQLiteDatabase();
        }
    }
}
