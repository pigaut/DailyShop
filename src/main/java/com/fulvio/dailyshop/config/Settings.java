package com.fulvio.dailyshop.config;

import com.fulvio.dailyshop.config.directory.DataFile;
import com.fulvio.dailyshop.message.Message;
import com.fulvio.dailyshop.shop.menu.PurchaseMenu;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public enum Settings {
    DATA;

    private final Map<Message, String> messages = new HashMap<>();
    private boolean papi;
    private String defaultShop;
    private ItemStack outOfStockItem;
    private ItemMeta defaultMeta;
    private PurchaseMenu purchaseMenu;
    private boolean openPurchaseMenu;

    public boolean isPapi() {
        return papi;
    }

    public String getDefaultShop() {
        return defaultShop;
    }

    public ItemStack getOutOfStockItem() {
        return outOfStockItem;
    }

    public boolean isDefaultMeta() {
        return defaultMeta != null;
    }

    public ItemMeta getDefaultMeta() {
        return defaultMeta.clone();
    }

    public PurchaseMenu getPurchaseMenu() {
        return purchaseMenu;
    }

    public Map<Message, String> getMessages() {
        return messages;
    }

    public String getMessage(Message messageId) {
        return messages.get(messageId);
    }

    public boolean openPurchaseMenu() {
        return openPurchaseMenu;
    }

    public void loadConfig() {
        Config config = new Config(DataFile.CONFIG);

        papi = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

        defaultShop = config.getString("default-shop");

        outOfStockItem = config.getCustomItem("out-of-stock-item");

        defaultMeta = config.getCustomItem("default-meta") == null ? null : config.getCustomItem("default-meta").getItemMeta();

        purchaseMenu = new PurchaseMenu(new ShopConfig(DataFile.PURCHASE_MENU.getFile()));

        for (Message id : Message.values()) {
            messages.put(id, config.getColorString("messages." + id.toString(), ""));
        }

        this.openPurchaseMenu = config.getBoolean("purchase-menu", true);

    }

}
