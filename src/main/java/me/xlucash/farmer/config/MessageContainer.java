package me.xlucash.farmer.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.xlucash.farmer.XLFarmerMain;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static me.xlucash.farmer.utils.MessagesUtils.colorize;

@Singleton
public class MessageContainer {

    private final XLFarmerMain plugin;

    public String commandOnlyForPlayers;
    public String noPermission;
    public String configReloaded;
    public String invalidCommand;

    public String guiTitleLevels;
    public String levelLocked;
    public String levelReadyToClaim;
    public String levelRewardsDescription;
    public String levelClaimRewardInstruction;
    public String levelClaimed;
    public String levelUnlocked;
    public String previousPage;
    public String nextPage;

    public String guiTitleMain;
    public String farmingSkillName;
    public String farmingSkillInfo;
    public String farmingSkillLevel;
    public String farmingSkillProgress;
    public String farmingSkillClick;
    public String leaderboardName;
    public String leaderboardClick;

    public String guiTitleTop;
    public String topNoPlayer;

    public String shopNoUnlockBuy;
    public String shopNoUnlockSell;

    public String cropLocked;
    public String farmingActionBar;

    public String rewardClaimed;

    public String maxLevelReached;
    public String levelUp;

    @Inject
    private MessageContainer(XLFarmerMain plugin) {
        this.plugin = plugin;

        var messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        var config = YamlConfiguration.loadConfiguration(messagesFile);

        this.commandOnlyForPlayers = colorize(config.getString("messages.command_only_for_players", "&cThis command can only be used by players."));
        this.noPermission = colorize(config.getString("messages.no_permission", "&cYou do not have permission to use this command."));
        this.configReloaded = colorize(config.getString("messages.config_reloaded", "&aConfiguration has been reloaded."));
        this.invalidCommand = colorize(config.getString("messages.invalid_command", "&cInvalid command. Use /farma"));
        this.guiTitleLevels = colorize(config.getString("messages.gui_title_levels", "&eFarming Levels"));
        this.levelLocked = colorize(config.getString("messages.level_locked", "&cLevel {level} (Locked)"));
        this.levelReadyToClaim = colorize(config.getString("messages.level_ready_to_claim", "&eLevel {level} (Ready to Claim)"));
        this.levelRewardsDescription = colorize(config.getString("messages.level_rewards_description", "&eRewards for this level:"));
        this.levelClaimRewardInstruction = colorize(config.getString("messages.level_claim_reward_instruction", "&eClick to claim your reward!"));
        this.levelClaimed = colorize(config.getString("messages.level_claimed", "&aLevel {level} (Claimed)"));
        this.levelUnlocked = colorize(config.getString("messages.level_unlocked", "&aLevel {level} (Unlocked)"));
        this.previousPage = colorize(config.getString("messages.previous_page", "&aPrevious Page ({page})"));
        this.nextPage = colorize(config.getString("messages.next_page", "&aNext Page ({page})"));
        this.guiTitleMain = colorize(config.getString("messages.gui_title_main", "&aFarming Menu"));
        this.farmingSkillName = colorize(config.getString("messages.farming_skill_name", "&aFarming Skill"));
        this.farmingSkillInfo = colorize(config.getString("messages.farming_skill_info", "&7Information:"));
        this.farmingSkillLevel = colorize(config.getString("messages.farming_skill_level", "&7Level: &a{level}"));
        this.farmingSkillProgress = colorize(config.getString("messages.farming_skill_progress", "&7Progress: &a[{progress}%]"));
        this.farmingSkillClick = colorize(config.getString("messages.farming_skill_click", "&7Click to check the level of farming skill."));
        this.leaderboardName = colorize(config.getString("messages.leaderboard_name", "&aPlayers leaderboard"));
        this.leaderboardClick = colorize(config.getString("messages.leaderboard_click", "&7Click to check the top players."));
        this.guiTitleTop = colorize(config.getString("messages.gui_title_top", "&aTop Farmers"));
        this.topNoPlayer = colorize(config.getString("messages.top_no_player", "&cNone"));
        this.shopNoUnlockBuy = colorize(config.getString("messages.shop_no_unlock_buy", "&cYou cannot buy {crop} because you haven't unlocked it yet."));
        this.shopNoUnlockSell = colorize(config.getString("messages.shop_no_unlock_sell", "&cYou cannot sell {crop} because you haven't unlocked it yet."));
        this.cropLocked = colorize(config.getString("messages.crop_locked", "&cYou haven't unlocked this crop yet!"));
        this.farmingActionBar = colorize(config.getString("messages.farming_actionbar", "&aFarming +1 XP &7({current}/{required})"));
        this.rewardClaimed = colorize(config.getString("messages.rewardClaimed", "&aYou have claimed the rewards for level {level}."));
        this.maxLevelReached = colorize(config.getString("messages.maxLevelReached", "&cYou have reached the maximum farming level."));
        this.levelUp = colorize(config.getString("messages.levelUp", "&aYou have reached level {level}!"));
    }

    public void reloadMessages() {
        var messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        var config = YamlConfiguration.loadConfiguration(messagesFile);

        this.commandOnlyForPlayers = colorize(config.getString("messages.command_only_for_players", "&cThis command can only be used by players."));
        this.noPermission = colorize(config.getString("messages.no_permission", "&cYou do not have permission to use this command."));
        this.configReloaded = colorize(config.getString("messages.config_reloaded", "&aConfiguration has been reloaded."));
        this.invalidCommand = colorize(config.getString("messages.invalid_command", "&cInvalid command. Use /farma"));
        this.guiTitleLevels = colorize(config.getString("messages.gui_title_levels", "&eFarming Levels"));
        this.levelLocked = colorize(config.getString("messages.level_locked", "&cLevel {level} (Locked)"));
        this.levelReadyToClaim = colorize(config.getString("messages.level_ready_to_claim", "&eLevel {level} (Ready to Claim)"));
        this.levelRewardsDescription = colorize(config.getString("messages.level_rewards_description", "&eRewards for this level:"));
        this.levelClaimRewardInstruction = colorize(config.getString("messages.level_claim_reward_instruction", "&eClick to claim your reward!"));
        this.levelClaimed = colorize(config.getString("messages.level_claimed", "&aLevel {level} (Claimed)"));
        this.levelUnlocked = colorize(config.getString("messages.level_unlocked", "&aLevel {level} (Unlocked)"));
        this.previousPage = colorize(config.getString("messages.previous_page", "&aPrevious Page ({page})"));
        this.nextPage = colorize(config.getString("messages.next_page", "&aNext Page ({page})"));
        this.guiTitleMain = colorize(config.getString("messages.gui_title_main", "&aFarming Menu"));
        this.farmingSkillName = colorize(config.getString("messages.farming_skill_name", "&aFarming Skill"));
        this.farmingSkillInfo = colorize(config.getString("messages.farming_skill_info", "&7Information:"));
        this.farmingSkillLevel = colorize(config.getString("messages.farming_skill_level", "&7Level: &a{level}"));
        this.farmingSkillProgress = colorize(config.getString("messages.farming_skill_progress", "&7Progress: &a[{progress}%]"));
        this.farmingSkillClick = colorize(config.getString("messages.farming_skill_click", "&7Click to check the level of farming skill."));
        this.leaderboardName = colorize(config.getString("messages.leaderboard_name", "&aPlayers leaderboard"));
        this.leaderboardClick = colorize(config.getString("messages.leaderboard_click", "&7Click to check the top players."));
        this.guiTitleTop = colorize(config.getString("messages.gui_title_top", "&aTop Farmers"));
        this.topNoPlayer = colorize(config.getString("messages.top_no_player", "&cNone"));
        this.shopNoUnlockBuy = colorize(config.getString("messages.shop_no_unlock_buy", "&cYou cannot buy {crop} because you haven't unlocked it yet."));
        this.shopNoUnlockSell = colorize(config.getString("messages.shop_no_unlock_sell", "&cYou cannot sell {crop} because you haven't unlocked it yet."));
        this.cropLocked = colorize(config.getString("messages.crop_locked", "&cYou haven't unlocked this crop yet!"));
        this.farmingActionBar = colorize(config.getString("messages.farming_actionbar", "&aFarming +1 XP &7({current}/{required})"));
        this.rewardClaimed = colorize(config.getString("messages.rewardClaimed", "&aYou have claimed the rewards for level {level}."));
        this.maxLevelReached = colorize(config.getString("messages.maxLevelReached", "&cYou have reached the maximum farming level."));
        this.levelUp = colorize(config.getString("messages.levelUp", "&aYou have reached level {level}!"));
    }
}
