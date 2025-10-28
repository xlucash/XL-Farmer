package me.xlucash.farmer.listeners;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.xlucash.farmer.config.ConfigContainer;
import me.xlucash.farmer.config.MessageContainer;
import me.xlucash.farmer.enums.IslandPrivilege;
import me.xlucash.farmer.hooks.SuperiorSkyblockHook;
import me.xlucash.farmer.services.FarmerCacheService;
import me.xlucash.farmer.services.FarmerService;
import me.xlucash.farmer.services.FarmingBypassCacheService;
import me.xlucash.farmer.models.FarmerData;
import me.xlucash.farmer.utils.CropChecker;
import me.xlucash.farmer.utils.CropMapper;
import net.coreprotect.CoreProtect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

import static me.xlucash.farmer.utils.MessagesUtils.formatMessage;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CropBlockListener implements Listener {

    private final ConfigContainer config;
    private final MessageContainer messages;
    private final SuperiorSkyblockHook skyblockHook;
    private final FarmerService farmerService;
    private final FarmerCacheService farmerCacheService;
    private final FarmingBypassCacheService bypass;
    private final Random random = new Random();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onBlockBreak(BlockBreakEvent event) {
        var player = event.getPlayer();
        var block = event.getBlock();

        if (bypass.hasBypass(player.getUniqueId())) {
            return;
        }

        if (!skyblockHook.playerHasPrivilege(player, block.getLocation(), IslandPrivilege.BREAK.name())) {
            event.setCancelled(true);
            return;
        }

        var blockType = block.getType();
        if (CropChecker.isCrop(blockType)) {
            var farmerData = farmerCacheService.getFarmerData(player.getUniqueId());

            if (!blockType.equals(Material.WHEAT) && !blockType.equals(Material.WHEAT_SEEDS)
                    && farmerData.getLevel() < config.crops.get(blockType.name()).getLevelRequired()) {
                event.setCancelled(true);
                player.sendMessage(messages.cropLocked);
                return;
            }

            if (block.getBlockData() instanceof Ageable ageable
                    && ageable.getAge() == ageable.getMaximumAge()
                    || (checkCoreProtectLogsIsEmpty(player, block)
                        && (blockType.equals(Material.SUGAR_CANE) || blockType.equals(Material.MELON) || blockType.equals(Material.PUMPKIN)))
            ) {

                addExpAndNotifyPlayer(player, farmerData, block);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();
        var block = event.getBlock();

        if (bypass.hasBypass(player.getUniqueId())) {
            return;
        }

        if (!skyblockHook.playerHasPrivilege(player, block.getLocation(), IslandPrivilege.BUILD.name())) {
            event.setCancelled(true);
            return;
        }

        var blockType = block.getType();
        if (CropChecker.isCrop(blockType)) {
            var farmerData = farmerCacheService.getFarmerData(player.getUniqueId());

            if (!blockType.equals(Material.WHEAT)
                    && !blockType.equals(Material.WHEAT_SEEDS)
                    && farmerData.getLevel() < config.crops.get(blockType.name()).getLevelRequired()) {
                event.setCancelled(true);
                player.sendMessage(messages.cropLocked);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();
        var action = event.getAction();
        var block = event.getClickedBlock();

        if (bypass.hasBypass(player.getUniqueId())) {
            return;
        }

        if (block != null && !skyblockHook.playerHasPrivilege(player, block.getLocation(), IslandPrivilege.INTERACT.name())) {
            event.setCancelled(true);
            return;
        }

        if (block != null && block.getType() == Material.SWEET_BERRY_BUSH
                && action == Action.RIGHT_CLICK_BLOCK
                && block.getBlockData() instanceof Ageable ageable) {

            var farmerData = farmerCacheService.getFarmerData(player.getUniqueId());

            if (farmerData.getLevel() < config.crops.get(block.getType().name()).getLevelRequired()) {
                player.sendMessage(messages.cropLocked);
                event.setCancelled(true);
                return;
            }

            if (ageable.getAge() == ageable.getMaximumAge() && CropChecker.isCropAddingExperience(block.getType())) {
                addExpAndNotifyPlayer(player, farmerData, block);
            }
        }
    }

    private void addExpAndNotifyPlayer(Player player, FarmerData farmerData, Block block) {
        var maxLevelReached = farmerService.addExp(player, farmerData, config.expPerCrop);

        if (maxLevelReached) {
            applyFortuneBonus(block, farmerData);
            return;
        }

        int nextLevelExp = farmerService.calculateRequiredExpForLevel(farmerData.getLevel() + 1);
        player.sendActionBar(formatMessage(
                messages.farmingActionBar,
                Map.of("experience", String.valueOf(config.expPerCrop),
                        "current", String.valueOf(farmerData.getExperience()),
                        "required", String.valueOf(nextLevelExp))
        ));
    }

    private void applyFortuneBonus(Block block, FarmerData farmerData) {
        var fortuneBonus = farmerData.getFortuneChance();

        if (fortuneBonus > 0 && random.nextDouble() < fortuneBonus) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(CropMapper.getCorrespondingItem(block.getType()), 1));
        }
    }

    private boolean checkCoreProtectLogsIsEmpty(Player player, Block block) {
        return !CoreProtect.getInstance().getAPI().hasPlaced(player.getName(), block, random.nextInt(6, 30), 0);
    }
}
