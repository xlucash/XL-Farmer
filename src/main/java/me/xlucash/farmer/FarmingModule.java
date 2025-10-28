package me.xlucash.farmer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import me.xlucash.farmer.config.ConfigContainer;
import me.xlucash.farmer.config.MessageContainer;
import me.xlucash.farmer.database.DatabaseManager;
import me.xlucash.farmer.database.impl.DatabaseManagerImpl;
import me.xlucash.farmer.factories.DatabaseFactory;
import me.xlucash.farmer.hooks.PlaceHolderApiHook;
import me.xlucash.farmer.hooks.SuperiorSkyblockHook;
import me.xlucash.farmer.services.FarmerCacheService;
import me.xlucash.farmer.services.FarmerService;
import me.xlucash.farmer.services.FarmingBypassCacheService;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class FarmingModule extends AbstractModule {

    private final XLFarmerMain plugin;

    public FarmingModule(final @NotNull XLFarmerMain plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        bind(XLFarmerMain.class).toInstance(plugin);
        bind(FileConfiguration.class).toInstance(plugin.getConfig());
        bind(PluginManager.class).toInstance(plugin.getServer().getPluginManager());
        bind(ConfigContainer.class).in(Singleton.class);
        bind(MessageContainer.class).in(Singleton.class);
        bind(DatabaseFactory.class).in(Singleton.class);
        bind(DatabaseManagerImpl.class).in(Singleton.class);
        bind(DatabaseManager.class).to(DatabaseManagerImpl.class);
        bind(PlaceHolderApiHook.class).in(Singleton.class);
        bind(SuperiorSkyblockHook.class).in(Singleton.class);
        bind(FarmerCacheService.class).in(Singleton.class);
        bind(FarmerService.class).in(Singleton.class);
        bind(FarmingBypassCacheService.class).in(Singleton.class);
    }
}
