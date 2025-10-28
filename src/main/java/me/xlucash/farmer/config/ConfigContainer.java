package me.xlucash.farmer.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.foresttech.api.ColorAPI;
import me.xlucash.farmer.models.Crop;
import me.xlucash.farmer.models.LevelRewardData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ConfigContainer {

    public int maxLevel;

    public boolean isDefaultFormulaEnabled;
    public int defaultExpFormula;

    public int expPerCrop;

    public String unlockedSlot;
    public String lockedSlot;
    public boolean isRewardSlotEnchanted;
    public String topHeadFormat;

    public Map<Integer, LevelRewardData> rewards;

    public Map<String, Crop> crops;

    public String databaseType;
    public String mysqlHost;
    public int mysqlPort;
    public String mysqlDatabase;
    public String mysqlUsername;
    public String mysqlPassword;

    @Inject
    private ConfigContainer(FileConfiguration config) {
        this.maxLevel = config.getInt("max_level");

        this.isDefaultFormulaEnabled = config.getBoolean("use_default_formula");
        this.defaultExpFormula = config.getInt("exp_formula");

        this.expPerCrop = config.getInt("exp_per_crop");

        this.unlockedSlot = config.getString("gui.level_panel.unlocked_slot");
        this.lockedSlot = config.getString("gui.level_panel.locked_slot");
        this.isRewardSlotEnchanted = config.getBoolean("gui.level_panel.reward_slot_enchanted");
        this.topHeadFormat = ColorAPI.colorize(config.getString("gui.top_head_format"));

        this.rewards = loadRewards(config.getConfigurationSection("rewards"));

        this.crops = loadCrops(config);

        this.databaseType = config.getString("database.type");
        this.mysqlHost = config.getString("database.mysql.host");
        this.mysqlPort = config.getInt("database.mysql.port");
        this.mysqlDatabase = config.getString("database.mysql.name");
        this.mysqlUsername = config.getString("database.mysql.username");
        this.mysqlPassword = config.getString("database.mysql.password");
    }

    private Map<Integer, LevelRewardData> loadRewards(ConfigurationSection section) {
        if (section == null) {
            return Collections.emptyMap();
        }

        return section.getKeys(false).stream()
                .collect(Collectors.toMap(
                        Integer::parseInt,
                        key -> {
                            ConfigurationSection rewardSection = section.getConfigurationSection(key);
                            return new LevelRewardData(
                                    rewardSection.getStringList("commands"),
                                    rewardSection.contains("fortune_bonus") ? rewardSection.getDouble("fortune_bonus") : null,
                                    rewardSection.contains("description") ? rewardSection.getStringList("description").stream().map(ColorAPI::colorize).toList() : null
                            );
                        }
                ));
    }

    private Map<String, Crop> loadCrops(FileConfiguration config) {
        return config.getConfigurationSection("crops").getKeys(false).stream()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> new Crop(
                                key,
                                config.getInt("crops." + key + ".level_required")
                        )
                ));
    }


    public void reload(FileConfiguration config) {
        this.maxLevel = config.getInt("max_level");

        this.isDefaultFormulaEnabled = config.getBoolean("use_default_formula");
        this.defaultExpFormula = config.getInt("exp_formula");

        this.expPerCrop = config.getInt("exp_per_crop");

        this.unlockedSlot = config.getString("gui.level_panel.unlocked_slot");
        this.lockedSlot = config.getString("gui.level_panel.locked_slot");
        this.isRewardSlotEnchanted = config.getBoolean("gui.level_panel.reward_slot_enchanted");
        this.topHeadFormat = ColorAPI.colorize(config.getString("gui.top_head_format"));

        this.rewards = loadRewards(config.getConfigurationSection("rewards"));

        this.crops = loadCrops(config);

        this.databaseType = config.getString("database.type");
        this.mysqlHost = config.getString("database.mysql.host");
        this.mysqlPort = config.getInt("database.mysql.port");
        this.mysqlDatabase = config.getString("database.mysql.name");
        this.mysqlUsername = config.getString("database.mysql.username");
        this.mysqlPassword = config.getString("database.mysql.password");
    }
}

