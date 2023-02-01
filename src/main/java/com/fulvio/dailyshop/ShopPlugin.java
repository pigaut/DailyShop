package com.fulvio.dailyshop;

import com.fulvio.dailyshop.config.item.ItemManager;
import com.fulvio.dailyshop.shop.ShopManager;
import com.fulvio.dailyshop.user.UserManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;

public interface ShopPlugin extends Plugin {

    UserManager getUsers();

    ShopManager getShops();

    ItemManager getItems();

    Economy getEconomy();

}
