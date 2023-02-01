package com.fulvio.dailyshop;

import com.fulvio.dailyshop.command.ShopCmdExecutor;
import com.fulvio.dailyshop.config.Config;
import com.fulvio.dailyshop.config.Settings;
import com.fulvio.dailyshop.config.Validator;
import com.fulvio.dailyshop.config.directory.DataFile;
import com.fulvio.dailyshop.config.directory.Folder;
import com.fulvio.dailyshop.config.item.ItemManager;
import com.fulvio.dailyshop.listener.InventoryEventListener;
import com.fulvio.dailyshop.listener.PlayerEventListener;
import com.fulvio.dailyshop.shop.ShopManager;
import com.fulvio.dailyshop.shop.task.ShopSaveTask;
import com.fulvio.dailyshop.user.ShopUser;
import com.fulvio.dailyshop.user.UserManager;
import com.fulvio.dailyshop.user.task.UserSaveTask;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class DailyShopPlugin extends JavaPlugin implements ShopPlugin {

    private static ShopPlugin plugin;

    private final UserManager userHandler = new UserManager(this);
    private final ShopManager shopHandler = new ShopManager();
    private final ItemManager itemHandler = new ItemManager();

    private final Settings settings = Settings.DATA;

    private Economy economy = null;

    public static ShopPlugin get() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        if (!setupEconomy()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Validator.setPlugin(this);

        load();

        getCommand("DailyShop").setExecutor(new ShopCmdExecutor(this));

        getServer().getPluginManager().registerEvents(new InventoryEventListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
    }

    @Override
    public void onDisable() {
        unload();
    }

    public void load() {
        Folder.createAll(this);

        final Config config = new Config(DataFile.CONFIG);

        if (config.getBoolean("generate-examples", true))
            DataFile.createAll(this);

        itemHandler.loadItems();
        settings.loadConfig();

        shopHandler.loadShops();
        shopHandler.setShopSaveTask(new ShopSaveTask(this));
        shopHandler.getShopSaveTask().runTaskTimerAsynchronously(this, 40L, config.getMinutesInTicks("auto-save"));

        userHandler.loadUsers();
        userHandler.setUserSaveTask(new UserSaveTask(this));
        userHandler.getUserSaveTask().runTaskTimerAsynchronously(this, 40L, config.getMinutesInTicks("auto-save"));
    }

    public void unload() {
        shopHandler.cancelTasks();
        userHandler.cancelTasks();

        saveShopData();
        saveUserData();

        for (ShopUser user : getUsers().getAllUsers()) {
            user.closeInventory();
        }
    }

    public UserManager getUsers() {
        return userHandler;
    }

    public ShopManager getShops() {
        return shopHandler;
    }

    public ItemManager getItems() {
        return itemHandler;
    }

    public Economy getEconomy() {
        return economy;
    }

    public void saveShopData() {
        shopHandler.getShopSaveTask().run();
    }

    public void saveUserData() {
        userHandler.getUserSaveTask().run();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }


}
