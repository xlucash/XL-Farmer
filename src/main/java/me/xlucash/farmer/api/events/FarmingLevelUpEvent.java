package me.xlucash.farmer.api.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

@Getter
public class FarmingLevelUpEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final UUID islandId;
    private final int level;

    public FarmingLevelUpEvent(UUID playerId, UUID islandId, int level) {
        this.playerId = playerId;
        this.islandId = islandId;
        this.level = level;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
