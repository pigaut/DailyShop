package com.fulvio.dailyshop.shop.menu.active;

import com.fulvio.dailyshop.shop.menu.ShopMenu;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

public interface OpenMenu {

    ShopMenu getShop();

    OpenShop getOpenShop();

    Inventory getInventory();

    void update();

    void click(ClickType click, int slot);

}
