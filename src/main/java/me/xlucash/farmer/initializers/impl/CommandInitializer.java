package me.xlucash.farmer.initializers.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.xlucash.farmer.XLFarmerMain;
import me.xlucash.farmer.commands.FarmCommand;
import me.xlucash.farmer.initializers.Initializer;
import org.jetbrains.annotations.NotNull;

public class CommandInitializer implements Initializer {

    @Inject
    private XLFarmerMain plugin;

    @Override
    public void init(@NotNull Injector injector) {
        plugin.getCommand("farma").setExecutor(injector.getInstance(FarmCommand.class));
    }
}
