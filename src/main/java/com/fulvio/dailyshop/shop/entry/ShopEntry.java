package com.fulvio.dailyshop.shop.entry;

import com.fulvio.dailyshop.config.Settings;
import com.fulvio.dailyshop.help.Util;
import com.fulvio.dailyshop.shop.ShopID;
import com.fulvio.dailyshop.user.ShopUser;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class ShopEntry {

    private final ShopID shopID;

    private final UUID entryId;

    private final EntryType type;

    private final int slot;

    private final int stock;

    private final int cost;

    public ShopEntry(ShopID shopID, EntryType type, int slot) {
        this.shopID = shopID;
        this.entryId = UUID.randomUUID();
        this.type = type;
        this.slot = slot;
        this.stock = type.getAmount();
        this.cost = type.getCost();
    }

    public ShopEntry(ShopID shopID, EntryType type, int slot, Map<String, String> entryData) {
        this.shopID = shopID;
        this.entryId = UUID.fromString(entryData.get("uuid"));
        this.type = type;
        this.slot = slot;
        this.stock = Util.getNumber(entryData.get("stock"));
        this.cost = Util.getNumber(entryData.get("cost"));
    }

    public ShopID getShopID() {
        return shopID;
    }

    public UUID getUniqueId() {
        return entryId;
    }

    public EntryType getType() {
        return type;
    }

    public int getSlot() {
        return slot;
    }

    public int getStock() {
        return stock;
    }

    public int getStock(ShopUser user) {
        if (stock == -1) return 999;
        return stock - user.getPurchasedAmount(this);
    }

    public int getCost() {
        return cost;
    }

    public ItemStack getItem(int amount) {
        final ItemStack item = type.getItem().clone();
        item.setAmount(amount);
        return item;
    }

    public ItemStack getDisplayItem() {
        return type.getDisplay().clone();
    }

    public ItemStack getDisplayItem(ShopUser user) {
        return getDisplayItem(getStock(user));
    }

    public ItemStack getDisplayItem(int amount) {
        if (amount < 1) return Settings.DATA.getOutOfStockItem();

        final ItemStack item = getDisplayItem();

        item.setAmount(amount);
        return item;
    }

    public void grant(Player player, int amount) {
        if (type.isItemSet())
            player.getInventory().addItem(getItem(amount));

        if (type.getCommands() == null) return;

        final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        for (String cmd : type.getCommands())
            Bukkit.getServer().dispatchCommand(console, cmd.replace("%amount%", amount + "").replace("%player%", player.getName()));
    }

}
