package me.xlucash.farmer.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.xlucash.farmer.config.ConfigContainer;
import me.xlucash.farmer.config.MessageContainer;
import me.xlucash.farmer.database.DatabaseManager;
import me.xlucash.farmer.models.FarmerData;
import me.xlucash.farmer.api.events.FarmingLevelUpEvent;
import me.xlucash.farmer.hooks.SuperiorSkyblockHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static me.xlucash.farmer.utils.MessagesUtils.formatMessage;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FarmerService {

    private final ConfigContainer config;
    private final MessageContainer messages;
    private final DatabaseManager databaseManager;
    private final FarmerCacheService farmerCacheService;
    private final SuperiorSkyblockHook skyblockHook;

    public int calculateRequiredExpForLevel(int level) {
        if (config.isDefaultFormulaEnabled) {
            return config.defaultExpFormula;
        }

        return (int) Math.pow(level/0.025, 1.5);
    }


    public boolean addExp(Player player, FarmerData farmerData, int exp) {
        if (farmerData.getLevel() >= config.maxLevel) {
            return true;
        }

        farmerData.setExperience(farmerData.getExperience() + exp);
        int nextLevelExp = calculateRequiredExpForLevel(farmerData.getLevel() + 1);

        while (farmerData.getExperience() >= nextLevelExp) {
            if (farmerData.getLevel() < config.maxLevel) {
                farmerData.setLevel(farmerData.getLevel() + 1);
                farmerData.setExperience(0);
                player.sendMessage(formatMessage(messages.levelUp, "{level}", String.valueOf(farmerData.getLevel())));
                tryPublishLevelUpEvent(player, farmerData);
            } else {
                farmerData.setExperience(0);
                return true;
            }
            nextLevelExp = calculateRequiredExpForLevel(farmerData.getLevel() + 1);
        }

        farmerCacheService.updateFarmerData(player.getUniqueId(), farmerData);
        databaseManager.saveFarmerData(farmerData);
        return false;
    }

    private void tryPublishLevelUpEvent(Player player, FarmerData farmerData) {
        var island = skyblockHook.getIsland(player);
        island.ifPresent(value -> Bukkit.getPluginManager().callEvent(new FarmingLevelUpEvent(player.getUniqueId(), value.getUniqueId(), farmerData.getLevel())));
    }


    public void awardLevelRewards(Player player, FarmerData farmerData, int level) {
        var levelReward = config.rewards.get(level);

        if (levelReward == null) {
            return;
        }

        var commands = levelReward.rewardCommands();
        if (commands != null) {
            for (String command : commands) {
                String formattedCommand = command.replace("{player}", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
            }
        }

        var fortuneBonus = levelReward.fortuneBonus();
        if (fortuneBonus != null) {
            farmerData.setFortuneLevel(fortuneBonus.intValue());
        }
    }
}
