package com.fulvio.dailyshop.help;

import com.fulvio.dailyshop.message.placeholder.Placeholder;
import com.fulvio.dailyshop.message.placeholder.Placeholders;
import com.fulvio.dailyshop.user.ShopUser;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class Util {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static Integer getNumber(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public static boolean canFitItems(Inventory inventory, ItemStack item) {
        if (item == null) return true;
        if (inventory.firstEmpty() != -1) return true;

        int amount = item.getAmount();
        for (ItemStack invItem : inventory.getContents()) {
            if (!item.isSimilar(invItem)) continue;
            amount -= invItem.getMaxStackSize() - invItem.getAmount();
        }
        return amount < 1;
    }


    public static ItemStack formatItem(ItemStack item, ShopUser user, Placeholder[] placeholders) {

        if (item == null) return null;

        if (!item.hasItemMeta()) return item;

        final ItemMeta meta = item.getItemMeta();

        if (meta.hasDisplayName()) {
            meta.setDisplayName(Placeholders.set(meta.getDisplayName(), user, placeholders));
        }

        if (meta.hasLore()) {

            List<String> lore = meta.getLore();

            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, Placeholders.set(lore.get(i), user, placeholders));
            }

            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack formatItem(ShopUser user, ItemStack item) {

        if (item == null) return null;

        if (!item.hasItemMeta()) return item;

        final ItemMeta meta = item.getItemMeta();

        if (meta.hasDisplayName()) {
            meta.setDisplayName(Placeholders.set(user, meta.getDisplayName()));
        }

        if (meta.hasLore()) {

            List<String> lore = meta.getLore();

            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, Placeholders.set(user, lore.get(i)));
            }

            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack[] cloneContents(ItemStack[] contents) {
        contents = contents.clone();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] == null) continue;
            contents[i] = contents[i].clone();
        }
        return contents;
    }

}
