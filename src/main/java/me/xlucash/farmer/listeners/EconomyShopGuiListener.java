package me.xlucash.farmer.listeners;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import me.gypopo.economyshopgui.api.events.PreTransactionEvent;
import me.xlucash.farmer.config.ConfigContainer;
import me.xlucash.farmer.config.MessageContainer;
import me.xlucash.farmer.services.FarmingBypassCacheService;
import me.xlucash.farmer.utils.CropChecker;
import me.xlucash.farmer.utils.CropMapper;
import me.xlucash.farmer.utils.CropTranslation;
import me.xlucash.farmer.services.FarmerCacheService;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.gypopo.economyshopgui.util.Transaction.Type.*;
import static me.xlucash.farmer.utils.MessagesUtils.formatMessage;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class EconomyShopGuiListener implements Listener {

    private final MessageContainer messages;
    private final ConfigContainer config;
    private final FarmerCacheService farmerCacheService;
    private final FarmingBypassCacheService farmingBypassCacheService;

    @EventHandler
    public void onShopPreTransaction(PreTransactionEvent event) {
        if (farmingBypassCacheService.hasBypass(event.getPlayer().getUniqueId())) {
            return;
        }

        var action = event.getTransactionType();
        var itemMaterial = event.getShopItem().getShopItem().getType();

        if (!CropChecker.isCrop(itemMaterial)) {
            return;
        }

        if (itemMaterial == Material.WHEAT || itemMaterial == Material.WHEAT_SEEDS) {
            return;
        }

        var cropMaterial = CropMapper.getCorrespondingCrop(itemMaterial);

        if (farmerCacheService.getFarmerData(event.getPlayer().getUniqueId()).getLevel() < config.crops.get(cropMaterial.name()).getLevelRequired()) {
            var translatedCrop = CropTranslation.translate(itemMaterial);

            event.setCancelled(true);
            if (action == BUY_SCREEN || action == BUY_STACKS_SCREEN || action == SHOPSTAND_BUY_SCREEN || action == QUICK_BUY) {
                event.getPlayer().sendMessage(formatMessage(messages.shopNoUnlockBuy, "{crop}", translatedCrop));
            } else if (action == SELL_ALL_COMMAND || action == SELL_ALL_SCREEN || action == SELL_GUI_SCREEN || action == SHOPSTAND_SELL_SCREEN ||
                        action == SELL_SCREEN || action == QUICK_SELL || action == AUTO_SELL_CHEST) {
                event.getPlayer().sendMessage(formatMessage(messages.shopNoUnlockSell, "{crop}", translatedCrop));
            }
        }
    }
}
