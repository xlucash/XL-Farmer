package me.xlucash.farmer.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SuperiorSkyblockHook {

    public Island getIslandAtLocation(Location location) {
        return SuperiorSkyblockAPI.getGrid().getIslandAt(location);
    }

    public Optional<Island> getIsland(Player player) {
        return Optional.ofNullable(SuperiorSkyblockAPI.getPlayer(player).getIsland());
    }

    public boolean playerHasPrivilege(Player player, Location location, String privilege) {
        var island = getIslandAtLocation(location);
        if (island == null) {
            return true;
        }
        return island.hasPermission(player, IslandPrivilege.getByName(privilege));
    }
}
