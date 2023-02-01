package com.fulvio.dailyshop.user;

import com.fulvio.dailyshop.ShopPlugin;
import com.fulvio.dailyshop.config.Config;
import com.fulvio.dailyshop.config.directory.Folder;
import com.fulvio.dailyshop.shop.ShopID;
import com.fulvio.dailyshop.shop.menu.ShopMenu;
import com.fulvio.dailyshop.user.task.UserSaveTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private final ShopPlugin plugin;

    private final Map<UUID, ShopUser> usersByUUID = new HashMap<>();

    private UserSaveTask userSaveTask;

    public UserManager(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    public ShopUser getUser(UUID playerId) {
        return usersByUUID.get(playerId);
    }

    public ShopUser getUser(HumanEntity player) {
        if (player == null) return null;
        return getUser(player.getUniqueId());
    }

    public Collection<ShopUser> getAllUsers() {
        return usersByUUID.values();
    }

    public void add(ShopUser user) {
        if (user == null) return;
        usersByUUID.put(user.getUniqueId(), user);
    }

    public void remove(Player player) {
        if (player == null) return;
        remove(player.getUniqueId());
    }

    public void remove(UUID playerId) {
        usersByUUID.remove(playerId);
    }

    public void cancelTasks() {
        this.userSaveTask.cancel();
    }

    public UserSaveTask getUserSaveTask() {
        return userSaveTask;
    }

    public void setUserSaveTask(UserSaveTask userSaveTask) {
        this.userSaveTask = userSaveTask;
    }

    public void loadUserData(ShopUser user) {
        final Config config = new Config(Folder.USER_DATA.getSubFile(user.getFileName()));

        if (!config.exists()) return;

        for (String key : config.getKeys(false)) {

            final ShopMenu shop = plugin.getShops().getShop(key);

            if (shop == null || !shop.getState().equals(config.getUUID(key + ".state"))) {
                config.set(key, null);
                continue;
            }

            user.getShopPurchases().put(shop.getShopId(), new PurchaseRegistry(config.getConfig(key)));

        }

        config.save();

    }

    public void unloadUserData(ShopUser user) {
        final Config config = new Config(Folder.USER_DATA.getSubFile(user.getFileName()));

        for (Map.Entry<ShopID, PurchaseRegistry> shops : user.getShopPurchases().entrySet()) {

            ConfigurationSection shopSection = config.createSection(shops.getKey().getName());

            shopSection.set("state", shops.getKey().getState().toString());

            for (Map.Entry<UUID, Integer> purchases : shops.getValue().getPurchases().entrySet())
                shopSection.set(purchases.getKey().toString(), purchases.getValue());

        }

        config.save();
    }

    public void loadUsers() {

        usersByUUID.clear();

        for (Player player : Bukkit.getOnlinePlayers()) {

            final ShopUser user = new ShopUser(player.getUniqueId());

            loadUserData(user);

            usersByUUID.put(user.getUniqueId(), user);

        }

    }


}
