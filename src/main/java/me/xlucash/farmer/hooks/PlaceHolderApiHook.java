package me.xlucash.farmer.hooks;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.xlucash.farmer.placeholders.FarmPlaceHolders;
import me.xlucash.farmer.services.FarmerCacheService;
import org.bukkit.Bukkit;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class PlaceHolderApiHook {

    private final FarmerCacheService farmerCacheService;

    public void register() {
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new FarmPlaceHolders(farmerCacheService).register();
        }
    }

}
