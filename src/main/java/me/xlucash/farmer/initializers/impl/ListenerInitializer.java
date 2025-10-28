package me.xlucash.farmer.initializers.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.xlucash.farmer.XLFarmerMain;
import me.xlucash.farmer.initializers.Initializer;
import me.xlucash.farmer.listeners.*;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class ListenerInitializer implements Initializer {

    @Inject
    private XLFarmerMain plugin;
    @Inject
    private PluginManager pluginManager;

    @Override
    public void init(@NotNull Injector injector) {
        if (isPluginEnabled("SuperiorSkyblock2")) {
            pluginManager.registerEvents(injector.getInstance(CropBlockListener.class), plugin);
        }

        if (isPluginEnabled("EconomyShopGUI-Premium")) {
            pluginManager.registerEvents(injector.getInstance(EconomyShopGuiListener.class), plugin);
            plugin.getLogger().info("EconomyShopGUI-Premium hook is active.");
        }

        pluginManager.registerEvents(injector.getInstance(PlayerEventListener.class), plugin);
        pluginManager.registerEvents(injector.getInstance(InventoryClickListener.class), plugin);
        pluginManager.registerEvents(injector.getInstance(CropDropListener.class), plugin);
        pluginManager.registerEvents(injector.getInstance(CropPickupListener.class), plugin);
    }

    private boolean isPluginEnabled(String pluginName) {
        var requiredPlugin = pluginManager.getPlugin(pluginName);
        return requiredPlugin != null && requiredPlugin.isEnabled();
    }
}
