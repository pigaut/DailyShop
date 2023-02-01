package com.fulvio.dailyshop.listener;

import com.fulvio.dailyshop.ShopPlugin;
import com.fulvio.dailyshop.user.ShopUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {

    private final ShopPlugin plugin;

    public PlayerEventListener(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ShopUser user = new ShopUser(event.getPlayer().getUniqueId());

        plugin.getUsers().loadUserData(user);

        plugin.getUsers().add(user);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ShopUser user = plugin.getUsers().getUser(event.getPlayer());

        plugin.getUsers().unloadUserData(user);

        plugin.getUsers().remove(user.getUniqueId());

    }
}
