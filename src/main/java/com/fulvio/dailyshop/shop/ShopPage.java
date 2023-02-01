package com.fulvio.dailyshop.shop;

import com.fulvio.dailyshop.help.Util;
import com.fulvio.dailyshop.message.placeholder.Placeholders;
import com.fulvio.dailyshop.shop.action.MenuAction;
import com.fulvio.dailyshop.shop.entry.ShopEntry;
import com.fulvio.dailyshop.shop.menu.ShopMenu;
import com.fulvio.dailyshop.user.ShopUser;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record ShopPage(ShopMenu shop, int page, List<ShopEntry> entries) {

    public ShopPage next() {
        return shop.getPage(page + 1);
    }

    public ShopPage previous() {
        return page < 1 ? null : shop.getPage(page - 1);
    }

    public ShopEntry getEntryAt(int slot) {
        for (ShopEntry entry : entries) {
            if (entry == null) return null;
            if (entry.getSlot() == slot) return entry;
        }
        return null;
    }

    public Inventory createInventory(ShopUser user) {
        return Bukkit.createInventory(null, shop.getSize(), Placeholders.set(shop.getDisplayName(), user, user.getPlaceholders()));
    }

    public ItemStack[] getContents(ShopUser user) {
        ItemStack[] contents = shop.getContents();

        for (int slot : shop.getSettings().getPurchaseSlots()) {
            ShopEntry entry = getEntryAt(slot);

            if (entry == null) {
                contents[slot] = null;
                continue;
            }

            contents[slot] = Util.formatItem(entry.getDisplayItem(user), user, user.getPlaceholders(entry));
        }

        return contents;
    }

    public MenuAction getSlotAction(int slot) {
        return shop.getSlotActions().get(slot);
    }

}
