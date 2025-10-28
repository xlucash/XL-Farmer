package me.xlucash.farmer.guis;

import me.xlucash.farmer.XLFarmerMain;
import me.xlucash.farmer.config.MessageContainer;
import me.xlucash.farmer.services.FarmerService;
import me.xlucash.farmer.services.FarmerCacheService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.xlucash.farmer.utils.MessagesUtils.formatMessage;

public class FarmingMainGUI {

    private final Player player;
    private final MessageContainer messages;
    private final FarmerCacheService farmerCacheService;
    private final FarmerService farmerService;
    private final XLFarmerMain plugin;

    public FarmingMainGUI(Player player, MessageContainer messages, FarmerCacheService farmerCacheService, FarmerService farmerService, XLFarmerMain plugin) {
        this.player = player;
        this.messages = messages;
        this.farmerCacheService = farmerCacheService;
        this.farmerService = farmerService;
        this.plugin = plugin;
    }

    public void openMainGui() {
        var farmerData = farmerCacheService.getFarmerData(player.getUniqueId());
        var inventory = Bukkit.createInventory(null, 27, messages.guiTitleMain);

        var hoeItem = new ItemStack(Material.DIAMOND_HOE);

        var hoeMeta = hoeItem.getItemMeta();

        var currentLevel = farmerData.getLevel();
        var currentExp = farmerData.getExperience();
        var expForNextLevel = farmerService.calculateRequiredExpForLevel(currentLevel + 1);
        var progress = ((double) currentExp / expForNextLevel) * 100;
        hoeMeta.setDisplayName(messages.farmingSkillName);

        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(messages.farmingSkillInfo);
        lore.add(formatMessage(messages.farmingSkillLevel, "{level}", String.valueOf(currentLevel)));
        lore.add(formatMessage(messages.farmingSkillProgress, "{progress}", String.format("%.2f", progress)));
        lore.add(" ");
        lore.add(messages.farmingSkillClick);
        hoeMeta.setLore(lore);

        var keyDamage = new NamespacedKey(plugin, "generic.attackDamage");
        var keyAttackSpeed = new NamespacedKey(plugin, "generic.attackSpeed");
        hoeMeta.addAttributeModifier(
                Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(keyDamage, 0, AttributeModifier.Operation.ADD_NUMBER)
        );
        hoeMeta.addAttributeModifier(
                Attribute.GENERIC_ATTACK_SPEED,
                new AttributeModifier(keyAttackSpeed, 0, AttributeModifier.Operation.ADD_NUMBER)
        );
        hoeItem.setItemMeta(hoeMeta);

        var headItem = new ItemStack(Material.PLAYER_HEAD);
        var headMeta = headItem.getItemMeta();
        headMeta.setDisplayName(messages.leaderboardName);
        headMeta.setLore(Collections.singletonList(messages.leaderboardClick));
        headItem.setItemMeta(headMeta);

        inventory.setItem(12, hoeItem);

        inventory.setItem(14, headItem);

        player.openInventory(inventory);
    }
}
