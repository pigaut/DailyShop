package com.fulvio.dailyshop.shop.menu.active;

import com.fulvio.dailyshop.config.Settings;
import com.fulvio.dailyshop.shop.ShopPage;
import com.fulvio.dailyshop.shop.action.MenuAction;
import com.fulvio.dailyshop.shop.action.Purchase;
import com.fulvio.dailyshop.shop.entry.ShopEntry;
import com.fulvio.dailyshop.shop.menu.ShopMenu;
import com.fulvio.dailyshop.user.ShopUser;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

public class OpenShop implements OpenMenu {

    private final ShopUser user;

    private final ShopPage shopPage;

    private Inventory inventory;

    public OpenShop(ShopUser user, ShopPage shopPage) {
        this.user = user;
        this.shopPage = shopPage;
    }

    public ShopUser getUser() {
        return user;
    }

    public ShopMenu getShop() {
        return shopPage.shop();
    }

    public OpenShop getOpenShop() {
        return this;
    }

    public ShopPage getShopPage() {
        return shopPage;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void update() {
        this.inventory = shopPage.createInventory(user);
        this.inventory.setContents(shopPage.getContents(user));
    }

    @Override
    public void click(ClickType click, int slot) {

        final MenuAction action = shopPage.getSlotAction(slot);

        if (action == MenuAction.CLOSE) {
            user.closeInventory();
        }

        if (action == MenuAction.NEXT) {
            user.openShopSilent(shopPage.next());
        }

        if (action == MenuAction.BACK) {
            user.openShopSilent(shopPage.previous());
        }

        if (action != MenuAction.BUY) return;

        final ShopEntry entry = shopPage.getEntryAt(slot);

        if (entry == null) return;

        if (click == ClickType.LEFT || click == ClickType.SHIFT_LEFT)
            new Purchase(this, entry, 1).execute();

        if (Settings.DATA.openPurchaseMenu() && click == ClickType.RIGHT || click == ClickType.SHIFT_RIGHT)
            user.openMenu(new OpenPurchase(this, entry));

    }

}
