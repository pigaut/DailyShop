package com.fulvio.dailyshop.config;

import com.fulvio.dailyshop.DailyShopPlugin;
import com.fulvio.dailyshop.config.directory.DataFile;
import com.fulvio.dailyshop.config.item.ItemBuilder;
import com.fulvio.dailyshop.generator.Amount;
import com.fulvio.dailyshop.generator.Range;
import com.fulvio.dailyshop.help.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Config {

    protected final File file;

    protected final ConfigurationSection config;

    public Config(DataFile dataFile) {
        this.file = dataFile.getFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            DailyShopPlugin.get().saveResource(dataFile.getPath(), false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public Config(File file) {
        this.file = file;
        if (!file.exists()) create();
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public Config(File file, ConfigurationSection config) {
        this.file = file;
        if (!file.exists()) create();
        this.config = config;
    }

    public boolean exists() {
        return file.exists() && file.length() > 0;
    }

    public void create() {
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            if (!file.exists())
                file.createNewFile();
            if (config instanceof FileConfiguration)
                ((FileConfiguration) config).save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Config getConfig(String path) {
        return new Config(file, getSection(path));
    }

    public ConfigurationSection getSection(String path) {
        if (!config.isConfigurationSection(path))
            return new YamlConfiguration();

        return config.getConfigurationSection(path);
    }

    public ConfigurationSection createSection(String path) {
        return config.createSection(path);
    }

    public boolean isSet(String path) {
        return config.isSet(path);
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    public Boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public Boolean getBoolean(String path, Boolean defaultValue) {
        return config.getBoolean(path, defaultValue);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public String getString(String path, String defaultValue) {
        return config.getString(path, defaultValue);
    }

    public String getColorString(String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path, "NOT_SET"));
    }

    public String getColorString(String path, String defaultValue) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path, defaultValue));
    }

    public Integer getInt(String path) {
        return config.getInt(path);
    }

    public Integer getInt(String path, Integer defaultValue) {
        return config.getInt(path, defaultValue);
    }

    public Long getLong(String path) {
        return config.getLong(path);
    }

    public Long getLong(String path, Long defaultValue) {
        return config.getLong(path, defaultValue);
    }

    public Amount getAmount(String path) {
        if (config.isInt(path)) return new Amount(config.getInt(path));

        String[] split = config.getString(path, "1-3").split("-");

        if (split.length < 2) return new Amount(1);

        return new Range(Util.getNumber(split[0]), Util.getNumber(split[1]));
    }

    public long getMinutesInTicks(String path) {
        return (getLong(path, 5L) * 60) * 20;
    }

    public LocalDateTime getDate(String path) {
        return !config.isSet(path) ? null : LocalDateTime.parse(getString(path));
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public boolean isMaterial(String path) {
        try {
            Material.valueOf(getString(path, "").toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public Material getMaterial(String path) {
        try {
            return Material.valueOf(getString(path, "DIRT").toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            Validator.validate(false, file.getName());
        }
        return Material.DIRT;
    }

    public ItemStack getItemStack(String path) {
        if (config.isItemStack(path)) {
            return config.getItemStack(path);
        }

        return new ItemBuilder(getConfig(path)).build();
    }

    public ItemStack getCustomItem(String path) {
        if (DailyShopPlugin.get().getItems().getItem(getString(path)) == null) {
            if (isMaterial(path)) return new ItemStack(getMaterial(path));
        }
        return DailyShopPlugin.get().getItems().getItem(getString(path));
    }

    public ItemStack getCustomItem(String path, ItemStack defaultValue) {
        if (getCustomItem(path) != null) return getCustomItem(path);
        if (defaultValue != null) return defaultValue;
        if (isMaterial(path)) return new ItemStack(getMaterial(path));
        return new ItemStack(Material.DIRT);
    }

    public UUID getUUID(String path, UUID randomUUID) {
        return UUID.fromString(getString(path, randomUUID.toString()));
    }

    public UUID getUUID(String path) {
        return UUID.fromString(getString(path, null));
    }


}
