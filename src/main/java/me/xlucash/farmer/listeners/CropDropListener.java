package me.xlucash.farmer.listeners;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.xlucash.farmer.config.ConfigContainer;
import me.xlucash.farmer.config.MessageContainer;
import me.xlucash.farmer.services.FarmerCacheService;
import me.xlucash.farmer.services.FarmingBypassCacheService;
import me.xlucash.farmer.utils.CropChecker;
import me.xlucash.farmer.utils.CropMapper;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CropDropListener implements Listener {

    private final ConfigContainer config;
    private final MessageContainer messages;
    private final FarmerCacheService farmerCacheService;
    private final FarmingBypassCacheService bypass;

    @EventHandler
    public void onPlayerPickupItem(PlayerDropItemEvent event) {
        var player = event.getPlayer();

        if (bypass.hasBypass(player.getUniqueId())) {
            return;
        }

        var item = event.getItemDrop().getItemStack().getType();

        if (CropChecker.isCrop(item)) {
            var farmerData = farmerCacheService.getFarmerData(player.getUniqueId());
            var mappedCrop = CropMapper.getCorrespondingCrop(item);

            if (!item.equals(Material.WHEAT)
                    && !item.equals(Material.WHEAT_SEEDS)
                    && farmerData.getLevel() < config.crops.get(mappedCrop.name()).getLevelRequired()) {
                event.setCancelled(true);
                player.sendMessage(messages.cropLocked);
            }
        }
    }
}
