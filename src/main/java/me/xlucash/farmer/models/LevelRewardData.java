package me.xlucash.farmer.models;

import java.util.List;

public record LevelRewardData(List<String> rewardCommands,
                              Double fortuneBonus,
                              List<String> description) {}
