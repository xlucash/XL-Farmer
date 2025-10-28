package me.xlucash.farmer.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.xlucash.farmer.database.DatabaseManager;
import me.xlucash.farmer.models.FarmerData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FarmerCacheService {

    private final DatabaseManager databaseManager;

    private final Map<UUID, FarmerData> cache = new HashMap<>();

    public void loadFarmerData(UUID playerUUID) {
        var farmerData = databaseManager.getFarmerData(playerUUID);
        cache.put(playerUUID, farmerData);
    }

    public void unloadFarmerData(UUID playerUUID) {
        var farmerData = cache.remove(playerUUID);
        if (farmerData != null) {
            databaseManager.saveFarmerData(farmerData);
        }
    }

    public FarmerData getFarmerData(UUID playerUUID) {
        return cache.get(playerUUID);
    }

    public void updateFarmerData(UUID playerUUID, FarmerData farmerData) {
        cache.put(playerUUID, farmerData);
    }

    public Set<UUID> getAllCachedPlayers() {
        return cache.keySet();
    }
}