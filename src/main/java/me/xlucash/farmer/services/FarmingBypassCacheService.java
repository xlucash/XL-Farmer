package me.xlucash.farmer.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FarmingBypassCacheService {

    private static final Map<UUID, Boolean> bypassCache = new HashMap<>();

    public boolean hasBypass(UUID uuid) {
        return bypassCache.getOrDefault(uuid, false);
    }

    public void toggleBypass(UUID uuid) {
        bypassCache.put(uuid, !bypassCache.getOrDefault(uuid, false));
    }
}
