package com.fulvio.dailyshop.listener;

import com.fulvio.dailyshop.ShopPlugin;
import com.fulvio.dailyshop.config.Settings;
import com.fulvio.dailyshop.message.Message;
import com.fulvio.dailyshop.user.ShopUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryEventListener implements Listener {

    private final ShopPlugin plugin;

    private final Settings settings = Settings.DATA;

    public InventoryEventListener(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        final ShopUser user = plugin.getUsers().getUser(event.getWhoClicked());

        if (!hasMenuOpen(user)) return;

        final ClickType clickType = event.getClick();

        if (clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT || clickType == ClickType.DOUBLE_CLICK)
            event.setCancelled(true);

        int slot = event.getRawSlot();

        if (slot < event.getInventory().getSize()) event.setCancelled(true);

        user.getOpenMenu().click(clickType, slot);

    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {

        final ShopUser user = plugin.getUsers().getUser(event.getWhoClicked());

        if (!hasMenuOpen(user)) return;

        for (int slot : event.getRawSlots()) {
            if (slot < event.getInventory().getSize())
                event.setCancelled(true);
        }
    }

    public boolean hasMenuOpen(ShopUser player) {
        if (player == null) return false;
        return player.getOpenMenu() != null;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        final ShopUser user = plugin.getUsers().getUser(event.getPlayer());

        if (!hasMenuOpen(user)) return;

        user.sendMessage(Message.CLOSE_MENU);
        user.setOpenMenu(null);
    }

}
