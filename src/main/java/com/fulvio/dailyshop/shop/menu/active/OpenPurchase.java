package com.fulvio.dailyshop.shop.menu.active;

import com.fulvio.dailyshop.config.Settings;
import com.fulvio.dailyshop.message.placeholder.Placeholders;
import com.fulvio.dailyshop.shop.action.MenuAction;
import com.fulvio.dailyshop.shop.action.Purchase;
import com.fulvio.dailyshop.shop.entry.ShopEntry;
import com.fulvio.dailyshop.shop.menu.PurchaseMenu;
import com.fulvio.dailyshop.shop.menu.ShopMenu;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

public class OpenPurchase implements OpenMenu {

    private final PurchaseMenu template = Settings.DATA.getPurchaseMenu();

    private final OpenShop openShop;

    private final ShopEntry entry;

    private Inventory inventory;

    public OpenPurchase(OpenShop openShop, ShopEntry entry) {
        this.openShop = openShop;
        this.entry = entry;
    }

    public ShopMenu getShop() {
        return openShop.getShop();
    }

    public OpenShop getOpenShop() {
        return openShop;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void update() {
        this.inventory = Bukkit.createInventory(null, template.getSize(), Placeholders.set(template.getDisplayName(), openShop.getUser(), openShop.getUser().getPlaceholders(entry)));
        inventory.setContents(template.getContents(openShop.getUser(), entry));
    }

    @Override
    public void click(ClickType click, int slot) {

        final MenuAction action = template.getSlotAction(slot);

        if (action == MenuAction.CLOSE) {
            openShop.getUser().closeInventory();
        }

        if (action == MenuAction.BACK) {
            openShop.getUser().openMenu(openShop);
            return;
        }
        
        if (action == MenuAction.BUY) {
            new Purchase(openShop, entry, template.getPurchaseAmount(slot)).execute();
            return;
        }
    }

}
