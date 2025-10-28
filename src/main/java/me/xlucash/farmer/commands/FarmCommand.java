package me.xlucash.farmer.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.xlucash.farmer.XLFarmerMain;
import me.xlucash.farmer.config.ConfigContainer;
import me.xlucash.farmer.config.MessageContainer;
import me.xlucash.farmer.guis.FarmingMainGUI;
import me.xlucash.farmer.services.FarmerCacheService;
import me.xlucash.farmer.services.FarmerService;
import me.xlucash.farmer.services.FarmingBypassCacheService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FarmCommand implements CommandExecutor {

    private final XLFarmerMain plugin;
    private final ConfigContainer config;
    private final MessageContainer messages;
    private final FarmingBypassCacheService bypassCacheService;
    private final FarmerCacheService farmerCacheService;
    private final FarmerService farmerService;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messages.commandOnlyForPlayers);
            return false;
        }

        if (args.length == 0) {
            new FarmingMainGUI(player, messages, farmerCacheService, farmerService, plugin).openMainGui();
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("farming.reload")) {
                player.sendMessage(messages.noPermission);
                return false;
            }

            plugin.reloadConfig();
            config.reload(plugin.getConfig());
            messages.reloadMessages();
            player.sendMessage(messages.configReloaded);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("bypass")) {
            if (!player.hasPermission("farming.bypass")) {
                player.sendMessage(messages.noPermission);
                return false;
            }

            bypassCacheService.toggleBypass(player.getUniqueId());
            boolean isBypassEnabled = bypassCacheService.hasBypass(player.getUniqueId());
            player.sendMessage(isBypassEnabled ? "§aBypass enabled." : "§cBypass disabled.");
            return true;
        }

        player.sendMessage(messages.invalidCommand);
        return true;
    }
}
