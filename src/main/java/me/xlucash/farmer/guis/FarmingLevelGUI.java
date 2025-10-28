package me.xlucash.farmer.guis;

import me.xlucash.farmer.config.ConfigContainer;
import me.xlucash.farmer.config.MessageContainer;
import me.xlucash.farmer.models.FarmerData;
import me.xlucash.farmer.models.LevelRewardData;
import me.xlucash.farmer.services.FarmerCacheService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

import static me.xlucash.farmer.utils.MessagesUtils.formatMessage;

public class FarmingLevelGUI {

    private final Player player;
    private final int page;
    private final MessageContainer messages;
    private final ConfigContainer config;
    private final FarmerCacheService farmerCacheService;

    public FarmingLevelGUI(Player player, int page, MessageContainer messages, ConfigContainer config, FarmerCacheService farmerCacheService) {
        this.player = player;
        this.page = page;
        this.messages = messages;
        this.config = config;
        this.farmerCacheService = farmerCacheService;
    }

    public void openLevelGUI() {
        var inv = Bukkit.createInventory(null, 54, messages.farmingSkillName);

        var farmerData = farmerCacheService.getFarmerData(player.getUniqueId());
        var rewards = config.rewards;
        var maxLevel = config.maxLevel;

        var slots = new int[]{0, 9, 18, 27, 36, 37, 38, 29, 20, 11, 2, 3, 4, 13, 22, 31, 40, 41, 42, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44};

        var startLevel = page * slots.length + 1;

        var endLevel = Math.min(startLevel + slots.length, maxLevel + 1);


        for (int i = startLevel; i < endLevel; i++) {
            Material material;
            String title;
            var lore = new ArrayList<String>();

            var reward = rewards.get(i);
            if (reward != null && !farmerData.hasClaimedReward(i)) {
                lore.add("");
                lore.add(messages.levelRewardsDescription);
                lore.addAll(rewards.get(i).description());
                lore.add("");
            }


            if (isLevelLocked(farmerData, i)) {
                material = Material.valueOf(config.lockedSlot);

                title = formatMessage(messages.levelLocked, "{level}", String.valueOf(i));
            } else if (hasUnclaimedReward(farmerData, rewards, i)) {
                material = Material.valueOf(config.lockedSlot);

                title = formatMessage(messages.levelReadyToClaim, "{level}", String.valueOf(i));

                lore.add(messages.levelClaimRewardInstruction);
            } else if (hasClaimedReward(farmerData, rewards, i)) {
                material = Material.MINECART;

                title = formatMessage(messages.levelClaimed, "{level}", String.valueOf(i));

                lore.add("");
                lore.add(messages.levelRewardsDescription);
                lore.addAll(rewards.get(i).description());
                lore.add("");
            } else {
                material = Material.valueOf(config.unlockedSlot);

                title = formatMessage(messages.levelUnlocked, "{level}", String.valueOf(i));
            }

            if (reward != null && !farmerData.hasClaimedReward(i)) {
                material = Material.CHEST_MINECART;
            }

            var levelItem = new ItemStack(material);
            var meta = levelItem.getItemMeta();
            meta.setDisplayName(title);
            meta.setLore(lore);

            if (reward != null && config.isRewardSlotEnchanted) {
                meta.addEnchant(Enchantment.INFINITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            levelItem.setItemMeta(meta);

            inv.setItem(slots[i - startLevel], levelItem);
        }

        if (page > 0) {
            var prevPage = new ItemStack(Material.ARROW);
            var prevMeta = prevPage.getItemMeta();
            prevMeta.setDisplayName(formatMessage(messages.previousPage, "{page}", String.valueOf(page - 1)));
            prevPage.setItemMeta(prevMeta);
            inv.setItem(45, prevPage);
        }

        if (endLevel < maxLevel) {
            var nextPage = new ItemStack(Material.ARROW);
            var nextMeta = nextPage.getItemMeta();
            nextMeta.setDisplayName(formatMessage(messages.nextPage, "{page}", String.valueOf(page + 1)));
            nextPage.setItemMeta(nextMeta);
            inv.setItem(53, nextPage);
        }

        player.openInventory(inv);
    }

    private boolean isLevelLocked(FarmerData farmerData, int level) {
        return farmerData.getLevel() < level;
    }

    private boolean hasUnclaimedReward(FarmerData farmerData, Map<Integer, LevelRewardData> rewards, int level) {
        return rewards.containsKey(level) && !farmerData.hasClaimedReward(level);
    }

    private boolean hasClaimedReward(FarmerData farmerData, Map<Integer, LevelRewardData> rewards, int level) {
        return rewards.containsKey(level) && farmerData.hasClaimedReward(level);
    }
}
