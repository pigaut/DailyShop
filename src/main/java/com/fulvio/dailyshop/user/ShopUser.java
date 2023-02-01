package com.fulvio.dailyshop.user;

import com.fulvio.dailyshop.message.Message;
import com.fulvio.dailyshop.message.placeholder.Placeholder;
import com.fulvio.dailyshop.message.placeholder.PlaceholderType;
import com.fulvio.dailyshop.message.placeholder.Placeholders;
import com.fulvio.dailyshop.shop.ShopID;
import com.fulvio.dailyshop.shop.ShopPage;
import com.fulvio.dailyshop.shop.entry.ShopEntry;
import com.fulvio.dailyshop.shop.menu.ShopMenu;
import com.fulvio.dailyshop.shop.menu.active.OpenMenu;
import com.fulvio.dailyshop.shop.menu.active.OpenShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShopUser {

    private final UUID playerId;

    private final Map<ShopID, PurchaseRegistry> shopPurchases = new HashMap<>();

    private OpenMenu openMenu = null;

    public ShopUser(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getUniqueId() {
        return playerId;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerId);
    }

    public void sendMessage(String message) {
        if (message.length() < 1) return;
        this.getPlayer().sendMessage(message);
    }

    public void sendMessage(Message id) {
        if (id.getMessage() == null) return;
        this.sendMessage(Placeholders.set(id.getMessage(), this, getPlaceholders()));
    }

    public void sendMessage(Message id, Placeholder[] placeholders) {
        if (id.getMessage() == null) return;
        this.sendMessage(Placeholders.set(id.getMessage(), this, placeholders));
    }

    public Placeholder[] getPlaceholders() {
        return getPlaceholders(null);
    }

    public Placeholder[] getPlaceholders(ShopEntry entry) {
        return getPlaceholders(entry, null);
    }

    public Placeholder[] getPlaceholders(ShopEntry entry, Integer amount) {
        Placeholder[] placeholders = new Placeholder[9];

        if (entry != null) {
            placeholders[0] = PlaceholderType.ENTRY_NAME.of(entry.getType().getName());
            placeholders[1] = PlaceholderType.ENTRY_PURCHASED.of(getPurchasedAmount(entry));
            placeholders[2] = PlaceholderType.ENTRY_STOCK.of(entry.getStock() == -1 ? 999 : entry.getStock());
            placeholders[3] = PlaceholderType.ENTRY_STOCK_PLAYER.of(entry.getStock(this));
            placeholders[4] = PlaceholderType.ENTRY_COST.of(entry.getCost());
        }

        if (amount != null) {
            placeholders[5] = PlaceholderType.ENTRY_COST_ALL.of(entry.getCost() * amount);
            placeholders[6] = PlaceholderType.ENTRY_AMOUNT.of(amount);
        }

        if (openMenu != null) {
            placeholders[7] = PlaceholderType.SHOP_NAME.of(openMenu.getShop().getName());
            placeholders[8] = PlaceholderType.SHOP_PAGE.of(openMenu.getOpenShop().getShopPage().page() + 1);
        }

        return placeholders;
    }

    public String getFileName() {
        return playerId + ".yml";
    }

    public Map<ShopID, PurchaseRegistry> getShopPurchases() {
        return shopPurchases;
    }

    public OpenMenu getOpenMenu() {
        return openMenu;
    }

    public void setOpenMenu(OpenMenu openMenu) {
        if (openMenu != null) {
            openMenu.update();
            return;
        }
        this.openMenu = null;
    }

    public int getPurchasedAmount(ShopEntry entry) {
        return shopPurchases.containsKey(entry.getShopID()) ? shopPurchases.get(entry.getShopID()).getPurchases(entry.getUniqueId()) : 0;
    }

    public void registerPurchase(ShopID shopId, ShopEntry entry, int amount) {
        final PurchaseRegistry purchaseRegistry = shopPurchases.getOrDefault(shopId, new PurchaseRegistry());
        purchaseRegistry.add(entry.getUniqueId(), amount);
        shopPurchases.putIfAbsent(shopId, purchaseRegistry);
    }

    public void resetPurchases(ShopID shopID) {
        shopPurchases.put(shopID, new PurchaseRegistry());
    }

    public void closeInventory() {
        this.getPlayer().closeInventory();
    }

    public void closeInventorySilent() {
        this.openMenu = null;
        this.getPlayer().closeInventory();
    }

    public void updateInventory() {
        if (openMenu == null) return;
        openMenu.update();
        openInventory(openMenu);
    }

    public void openInventory(OpenMenu menu) {
        if (menu == null) return;
        this.openMenu = null;
        this.getPlayer().openInventory(menu.getInventory());
        this.openMenu = menu;
    }

    public void openShop(ShopMenu shop) {
        if (shop == null) return;
        if (openMenu != null) closeInventory();
        this.openShop(shop.getPage(0));
    }

    public void openShop(ShopPage page) {
        openShopSilent(page);
        sendMessage(Message.OPEN_MENU);
    }

    public void openShopSilent(ShopPage page) {
        if (page == null) return;
        openMenu(new OpenShop(this, page));
    }

    public void openMenu(OpenMenu openMenu) {
        if (openMenu == null) return;
        this.openMenu = openMenu;
        openMenu.update();
        openInventory(openMenu);
    }

}
