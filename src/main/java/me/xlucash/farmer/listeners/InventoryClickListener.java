package me.xlucash.farmer.listeners;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.xlucash.farmer.config.ConfigContainer;
import me.xlucash.farmer.config.MessageContainer;
import me.xlucash.farmer.database.DatabaseManager;
import me.xlucash.farmer.guis.FarmingLevelGUI;
import me.xlucash.farmer.guis.FarmingTopGUI;
import me.xlucash.farmer.services.FarmerCacheService;
import me.xlucash.farmer.services.FarmerService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static me.xlucash.farmer.utils.MessagesUtils.formatMessage;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class InventoryClickListener implements Listener {

    private final ConfigContainer config;
    private final MessageContainer messages;
    private final DatabaseManager databaseManager;
    private final FarmerService farmerService;
    private final FarmerCacheService farmerCacheService;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        var inventoryTitle = event.getView().getOriginalTitle();

        if (inventoryTitle.equals(messages.guiTitleMain)) {
            handleFarmingMainClick(event);
        } else if (inventoryTitle.equals(messages.farmingSkillName)) {
            handleFarmingLevelsClick(event);
        } else if (inventoryTitle.equals(messages.guiTitleTop)) {
            handleTopFarmersClick(event);
        }
    }

    private void handleFarmingMainClick(InventoryClickEvent event) {
        event.setCancelled(true);

        var player = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 12:
                player.closeInventory();
                new FarmingLevelGUI(player, 0, messages, config, farmerCacheService).openLevelGUI();
                break;
            case 14:
                player.closeInventory();
                new FarmingTopGUI(player, config, messages, databaseManager).openTopGUI();
                break;
            default:
                break;
        }
    }

    private void handleFarmingLevelsClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

        var player = (Player) event.getWhoClicked();
        var itemMeta = event.getCurrentItem().getItemMeta();
        var displayName = itemMeta.getDisplayName();

        if (event.getSlot() == 45 || event.getSlot() == 53) {
            var pageString = displayName.replaceAll("[^0-9]", "");
            if (!pageString.isEmpty()) {
                var page = Integer.parseInt(pageString);
                new FarmingLevelGUI(player, page, messages, config, farmerCacheService).openLevelGUI();
            }
            return;
        }

        if (displayName.contains("(Gotowy do Odbioru)")) {
            var levelString = displayName.replaceAll("[^0-9]", "");
            if (!levelString.isEmpty()) {
                var level = Integer.parseInt(levelString);

                var farmerData = farmerCacheService.getFarmerData(player.getUniqueId());

                if (!farmerData.hasClaimedReward(level)) {
                    farmerService.awardLevelRewards(player, farmerData, level);

                    farmerData.claimReward(level);

                    farmerCacheService.updateFarmerData(player.getUniqueId(), farmerData);
                    databaseManager.saveFarmerData(farmerData);

                    player.sendMessage(formatMessage(messages.rewardClaimed, "{level}", String.valueOf(level)));

                    player.closeInventory();
                }
            }
        }
    }

    private void handleTopFarmersClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

}
