package com.fulvio.dailyshop.shop.task;

import com.fulvio.dailyshop.DailyShopPlugin;
import com.fulvio.dailyshop.ShopPlugin;
import com.fulvio.dailyshop.help.ItemComparator;
import com.fulvio.dailyshop.message.Message;
import com.fulvio.dailyshop.shop.ShopPage;
import com.fulvio.dailyshop.shop.ShopSettings;
import com.fulvio.dailyshop.shop.entry.EntryType;
import com.fulvio.dailyshop.shop.entry.ShopEntry;
import com.fulvio.dailyshop.shop.menu.ShopMenu;
import com.fulvio.dailyshop.user.ShopUser;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopResetTask extends BukkitRunnable {

    private final ShopPlugin plugin = DailyShopPlugin.get();

    private final ShopMenu shop;

    private final ShopSettings settings;

    public ShopResetTask(ShopMenu shop) {
        this.shop = shop;
        this.settings = shop.getSettings();
    }

    @Override
    public void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ShopUser user : plugin.getUsers().getAllUsers()) {
                    user.resetPurchases(shop.getShopId());

                    if (user.getOpenMenu() == null) continue;

                    user.closeInventorySilent();
                    user.sendMessage(Message.SHOP_REFRESHING, user.getPlaceholders());
                }
            }
        }.runTask(plugin);
        
        shop.newState();

        final int entriesPerPage = settings.getPageEntries();

        final List<EntryType> shopEntries = getShopEntries(settings.getTotalItems(), entriesPerPage);

        final int pages = entriesPerPage < 1 ? 0 : ((shopEntries.size() + entriesPerPage) / entriesPerPage);

        while (shopEntries.size() < pages * entriesPerPage) {
            shopEntries.add(null);
        }

        Collections.sort(shopEntries, new ItemComparator());

        for (int i = 0; i < pages; i++) {

            List<ShopEntry> pageEntries = new ArrayList<>();

            for (int z = 0; z < entriesPerPage; z++) {

                EntryType type = shopEntries.get((i * entriesPerPage) + z);

                pageEntries.add(type == null ? null : new ShopEntry(shop.getShopId(), type, settings.getPurchaseSlot(z)));

            }

            shop.addPage(new ShopPage(shop, i, pageEntries));

        }

    }

    private List<EntryType> getShopEntries(int totalItems, int pageItems) {
        final List<EntryType> items = new ArrayList<>(totalItems);

        while (items.size() < totalItems) {
            if (settings.allLimitsReached()) break;

            EntryType randomItem = settings.getRandomItem();

            if (randomItem.isOverLimit()) continue;
            if (!randomItem.runChance()) continue;

            items.add(randomItem);
            randomItem.increaseCount();

        }

        return items;
    }

}
