package com.fulvio.dailyshop.shop;

import com.fulvio.dailyshop.config.ShopConfig;
import com.fulvio.dailyshop.config.directory.Folder;
import com.fulvio.dailyshop.shop.menu.ShopMenu;
import com.fulvio.dailyshop.shop.task.ShopSaveTask;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ShopManager {

    private final Map<String, ShopMenu> shopsByName = new HashMap<>();

    private ShopSaveTask shopSaveTask;

    public ShopMenu getShop(String name) {
        return shopsByName.get(name);
    }

    public ShopMenu getShop(ShopID shopID) {
        final ShopMenu shop = getShop(shopID.getName());
        return shop.getShopId().equals(shopID) ? shop : null;
    }

    public Collection<ShopMenu> getAllShops() {
        return shopsByName.values();
    }

    public void loadShops() {
        for (File file : Folder.SHOPS.getSubFiles()) {
            final String shopId = file.getName().replace(".yml", "");

            shopsByName.put(shopId, new ShopMenu(shopId, new ShopConfig(file)));
        }
    }

    public ShopSaveTask getShopSaveTask() {
        return shopSaveTask;
    }

    public void setShopSaveTask(ShopSaveTask shopSaveTask) {
        this.shopSaveTask = shopSaveTask;
    }

    public void cancelTasks() {
        shopSaveTask.cancel();
        for (ShopMenu shop : shopsByName.values()) {
            shop.getResetTask().cancel();
        }
    }

}
