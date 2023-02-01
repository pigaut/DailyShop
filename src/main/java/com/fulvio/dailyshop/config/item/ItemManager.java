package com.fulvio.dailyshop.config.item;

import com.fulvio.dailyshop.config.Config;
import com.fulvio.dailyshop.config.directory.Folder;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    private final Map<String, ItemStack> items = new HashMap<>();

    public void loadItems() {
        for (File file : Folder.ITEMS.getSubFiles()) {
            if (!file.canRead()) continue;
            Config config = new Config(file);
            for (String itemId : config.getKeys(false)) {
                items.put(itemId, config.getItemStack(itemId));
            }
        }
    }

    public void unloadItems() {
        items.clear();
    }

    public ItemStack getItem(String name) {
        return items.get(name);
    }

}
