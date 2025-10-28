package me.xlucash.farmer.models;

import java.util.*;

public class FarmerData {

    private final UUID playerUUID;
    private int level;
    private int experience;
    private int fortuneLevel;
    private Set<Integer> claimedRewards;

    public FarmerData(UUID playerUUID, int level, int experience, int fortuneLevel, Set<Integer> claimedRewards) {
        this.playerUUID = playerUUID;
        this.level = level;
        this.experience = experience;
        this.fortuneLevel = fortuneLevel;
        this.claimedRewards = (claimedRewards != null) ? new HashSet<>(claimedRewards) : new HashSet<>();
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getFortuneLevel() {
        return fortuneLevel;
    }

    public void setFortuneLevel(int fortuneLevel) {
        this.fortuneLevel = fortuneLevel;
    }

    public double getFortuneChance() {
        return fortuneLevel / 100.0;
    }

    public Set<Integer> getClaimedRewards() {
        return claimedRewards;
    }

    public boolean hasClaimedReward(int level) {
        return claimedRewards.contains(level);
    }

    public void claimReward(int level) {
        claimedRewards.add(level);
    }
}
