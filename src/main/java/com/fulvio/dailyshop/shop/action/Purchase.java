package com.fulvio.dailyshop.shop.action;

import com.fulvio.dailyshop.DailyShopPlugin;
import com.fulvio.dailyshop.help.Util;
import com.fulvio.dailyshop.message.Message;
import com.fulvio.dailyshop.message.placeholder.Placeholder;
import com.fulvio.dailyshop.shop.ShopID;
import com.fulvio.dailyshop.shop.entry.ShopEntry;
import com.fulvio.dailyshop.shop.menu.active.OpenShop;
import com.fulvio.dailyshop.user.ShopUser;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Purchase {

    private final Economy economy = DailyShopPlugin.get().getEconomy();

    private final ShopUser user;

    private final ShopID shopID;

    private final ShopEntry entry;

    private final int amount;

    private final Placeholder[] placeholders;

    public Purchase(OpenShop openShop, ShopEntry entry, int amount) {
        this.user = openShop.getUser();
        this.shopID = openShop.getShop().getShopId();
        this.entry = entry;
        this.amount = amount;
        this.placeholders = user.getPlaceholders(entry, amount);
    }

    public void execute() {

        final Player player = user.getPlayer();

        if (amount > entry.getStock(user)) {
            user.sendMessage(Message.NO_STOCK, placeholders);
            return;
        }

        int cost = entry.getCost() * amount;

        if (!economy.has(player, cost)) {
            user.sendMessage(Message.NO_MONEY, placeholders);
            return;
        }

        if (entry.getType().isItemSet()) {
            ItemStack purchaseItem = entry.getItem(amount);

            if (!Util.canFitItems(user.getPlayer().getInventory(), purchaseItem)) {
                user.sendMessage(Message.NO_SPACE, placeholders);
                return;
            }
        }

        if (entry.getStock() != -1)
            user.registerPurchase(shopID, entry, amount);

        economy.withdrawPlayer(player, cost);
        entry.grant(player, amount);

        user.updateInventory();

        user.sendMessage(Message.PURCHASE, placeholders);
    }

}
