package me.xlucash.farmer.listeners;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.xlucash.farmer.services.FarmerCacheService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class PlayerEventListener implements Listener {

    private final FarmerCacheService farmerCacheService;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var playerUUID = event.getPlayer().getUniqueId();
        farmerCacheService.loadFarmerData(playerUUID);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var playerUUID = event.getPlayer().getUniqueId();
        farmerCacheService.unloadFarmerData(playerUUID);
    }
}
