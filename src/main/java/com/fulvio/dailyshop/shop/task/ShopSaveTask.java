package com.fulvio.dailyshop.shop.task;

import com.fulvio.dailyshop.ShopPlugin;
import com.fulvio.dailyshop.config.Config;
import com.fulvio.dailyshop.config.directory.Folder;
import com.fulvio.dailyshop.config.serializer.EntrySerializer;
import com.fulvio.dailyshop.shop.menu.ShopMenu;
import org.bukkit.scheduler.BukkitRunnable;

public class ShopSaveTask extends BukkitRunnable {

    private final ShopPlugin plugin;

    public ShopSaveTask(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        for (ShopMenu shop : plugin.getShops().getAllShops()) {

            final Config config = new Config(Folder.SHOP_DATA.getSubFile(shop.getFileName()));

            config.set("state", shop.getState().toString());
            config.set("last-reset", shop.getLastReset().toString());
            config.set("entries", EntrySerializer.serialize(shop));

            config.save();
        }

    }

}
