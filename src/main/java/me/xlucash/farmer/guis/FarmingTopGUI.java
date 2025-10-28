package me.xlucash.farmer.guis;

import me.xlucash.farmer.config.ConfigContainer;
import me.xlucash.farmer.config.MessageContainer;
import me.xlucash.farmer.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class FarmingTopGUI {

    private final Player player;
    private final ConfigContainer config;
    private final MessageContainer messages;
    private final DatabaseManager databaseManager;

    public FarmingTopGUI(Player player, ConfigContainer config, MessageContainer messages, DatabaseManager databaseManager) {
        this.player = player;
        this.config = config;
        this.messages = messages;
        this.databaseManager = databaseManager;
    }

    public void openTopGUI() {
        var inv = Bukkit.createInventory(null, 54, messages.guiTitleTop);

        var topFarmers = databaseManager.getTopFarmers();

        var slots = new int[]{13,21,23,29,31,33,37,39,41,43};
        int slotIndex = 0;

        for (int i = 0; i < slots.length; i++) {
            ItemStack head;

            if (i < topFarmers.size()) {
                var entry = topFarmers.get(i);
                var playerName = entry.getKey();
                int level = entry.getValue();

                OfflinePlayer topPlayer = Bukkit.getOfflinePlayer(playerName);
                head = new ItemStack(Material.PLAYER_HEAD, 1);
                var skullMeta = (SkullMeta) head.getItemMeta();
                skullMeta.setOwningPlayer(topPlayer);

                var title = config.topHeadFormat
                        .replace("{position}", String.valueOf(i + 1))
                        .replace("{player}", playerName)
                        .replace("{level}", String.valueOf(level));

                skullMeta.setDisplayName(title);
                head.setItemMeta(skullMeta);

            } else {
                head = new ItemStack(Material.PLAYER_HEAD, 1);
                var skullMeta = (SkullMeta) head.getItemMeta();

                skullMeta.setDisplayName(messages.topNoPlayer);
                head.setItemMeta(skullMeta);
            }

            inv.setItem(slots[slotIndex], head);
            slotIndex++;
        }

        player.openInventory(inv);
    }
}
