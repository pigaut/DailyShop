package com.fulvio.dailyshop.shop.menu;

import com.fulvio.dailyshop.config.Settings;
import com.fulvio.dailyshop.config.ShopConfig;
import com.fulvio.dailyshop.help.Util;
import com.fulvio.dailyshop.shop.action.MenuAction;
import com.fulvio.dailyshop.shop.entry.ShopEntry;
import com.fulvio.dailyshop.user.ShopUser;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PurchaseMenu extends MenuTemplate {

    private final Map<Integer, Integer> purchaseOptions;

    private final Map<Integer, MenuAction> slotActions;

    public PurchaseMenu(ShopConfig config) {
        super(config);

        this.purchaseOptions = new HashMap<>();

        for (String slot : config.getSection("purchase-options").getKeys(false)) {
            purchaseOptions.put(Util.getNumber(slot), config.getInt("purchase-options." + slot));
        }

        this.slotActions = config.getSlotActions();
    }

    public ItemStack[] getContents(ShopUser user, ShopEntry entry) {
        ItemStack[] contents = getContents();

        for (int i = 0; i < contents.length; i++)
            contents[i] = Util.formatItem(contents[i], user, user.getPlaceholders(entry));

        for (Map.Entry<Integer, Integer> buyOption : purchaseOptions.entrySet()) {

            ItemStack item = entry.getDisplayItem(buyOption.getValue());

            if (Settings.DATA.isDefaultMeta())
                item.setItemMeta(Settings.DATA.getDefaultMeta());

            contents[buyOption.getKey()] = Util.formatItem(item, user, user.getPlaceholders(entry, buyOption.getValue()));
        }

        return contents;
    }

    public Integer getPurchaseAmount(int slot) {
        return purchaseOptions.getOrDefault(slot, 1);
    }

    public MenuAction getSlotAction(int slot) {
        return slotActions.get(slot);
    }

}
