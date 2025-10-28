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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CropPickupListener implements Listener {

    private final FarmingBypassCacheService bypass;
    private final ConfigContainer config;
    private final MessageContainer messages;
    private final FarmerCacheService farmerCacheService;

    private final Map<UUID, Long> lastMessageTime = new HashMap<>();
    private static final long MESSAGE_COOLDOWN = 5000;

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        var player = event.getEntity();
        if (!(player instanceof Player)) {
            return;
        }

        if (bypass.hasBypass(player.getUniqueId())) {
            return;
        }

        var item = event.getItem().getItemStack().getType();

        if (CropChecker.isCrop(item)) {
            var farmerData = farmerCacheService.getFarmerData(player.getUniqueId());
            var mappedCrop = CropMapper.getCorrespondingCrop(item);

            if (!item.equals(Material.WHEAT)
                    && !item.equals(Material.WHEAT_SEEDS)
                    && farmerData.getLevel() < config.crops.get(mappedCrop.name()).getLevelRequired()) {
                event.setCancelled(true);

                long currentTime = System.currentTimeMillis();

                if (!lastMessageTime.containsKey(player.getUniqueId())
                        || (currentTime - lastMessageTime.get(player.getUniqueId())) > MESSAGE_COOLDOWN
                ) {

                    player.sendMessage(messages.cropLocked);
                    lastMessageTime.put(player.getUniqueId(), currentTime);
                }
            }
        }
    }
}
