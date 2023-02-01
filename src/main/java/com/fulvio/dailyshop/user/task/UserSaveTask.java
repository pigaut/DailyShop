package com.fulvio.dailyshop.user.task;

import com.fulvio.dailyshop.ShopPlugin;
import com.fulvio.dailyshop.user.ShopUser;
import org.bukkit.scheduler.BukkitRunnable;

public class UserSaveTask extends BukkitRunnable {

    private ShopPlugin plugin;

    public UserSaveTask(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (ShopUser user : plugin.getUsers().getAllUsers()) {
            plugin.getUsers().unloadUserData(user);
        }
    }

}
