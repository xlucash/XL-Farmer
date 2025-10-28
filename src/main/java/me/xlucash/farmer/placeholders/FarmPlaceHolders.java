package me.xlucash.farmer.placeholders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.xlucash.farmer.services.FarmerCacheService;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FarmPlaceHolders extends PlaceholderExpansion {

    private final FarmerCacheService farmerCacheService;

    @Override
    public @NotNull String getIdentifier() {
        return "xlfarming";
    }

    @Override
    public @NotNull String getAuthor() {
        return "xlucash";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }

        // %xlfarming_player_level%
        if (identifier.equals("player_level")) {
            return String.valueOf(farmerCacheService.getFarmerData(player.getUniqueId()).getLevel());
        }

        return null;
    }
}
