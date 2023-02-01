package com.fulvio.dailyshop.shop.menu;

import com.fulvio.dailyshop.config.ShopConfig;
import com.fulvio.dailyshop.help.Util;
import org.bukkit.inventory.ItemStack;

public class MenuTemplate {

    private final String displayName;

    private final int size;

    private final ItemStack[] contents;

    public MenuTemplate(ShopConfig config) {
        this.displayName = config.getMenuName();
        this.size = config.getMenuSize();
        this.contents = config.getMenuContents();
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getSize() {
        return size;
    }

    public ItemStack[] getContents() {
        return Util.cloneContents(contents);
    }

}
