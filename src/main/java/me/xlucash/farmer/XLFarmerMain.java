package me.xlucash.farmer;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.xlucash.farmer.database.DatabaseManager;
import me.xlucash.farmer.database.impl.DatabaseManagerImpl;
import me.xlucash.farmer.hooks.PlaceHolderApiHook;
import me.xlucash.farmer.initializers.impl.CommandInitializer;
import me.xlucash.farmer.initializers.impl.ListenerInitializer;
import me.xlucash.farmer.models.FarmerData;
import me.xlucash.farmer.services.FarmerCacheService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class XLFarmerMain extends JavaPlugin {

    private Injector injector;

    @Inject
    private ListenerInitializer listenerInitializer;

    @Inject
    private CommandInitializer commandInitializer;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FarmingModule module = new FarmingModule(this);
        injector = module.createInjector();
        injector.getInstance(DatabaseManager.class).initialize();
        injector.getInstance(PlaceHolderApiHook.class).register();

        commandInitializer.init(injector);
        listenerInitializer.init(injector);
    }

    @Override
    public void onDisable() {
        var databaseManager = injector.getInstance(DatabaseManagerImpl.class);
        var farmerCacheService = injector.getInstance(FarmerCacheService.class);
        for (UUID playerUUID : farmerCacheService.getAllCachedPlayers()) {
            FarmerData farmerData = farmerCacheService.getFarmerData(playerUUID);
            if (farmerData != null) {
                databaseManager.saveFarmerData(farmerData);
            }
        }

        if (databaseManager != null) {
            databaseManager.close();
        }
    }

}
