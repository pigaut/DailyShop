package com.fulvio.dailyshop.config;

import com.fulvio.dailyshop.ShopPlugin;

import java.util.logging.Level;

public class Validator {

    private static ShopPlugin plugin;

    public static void setPlugin(ShopPlugin plugin) {
        Validator.plugin = plugin;
    }

    public static void validate(boolean condition, String fileName) throws ConfigurationException {
        if (condition) return;
        plugin.getLogger().log(Level.SEVERE, "There was something wrong at " + fileName);
        plugin.getServer().getPluginManager().disablePlugin(plugin);
        throw new ConfigurationException();
    }

}
