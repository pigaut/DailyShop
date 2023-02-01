package com.fulvio.dailyshop.config.directory;

import com.fulvio.dailyshop.ShopPlugin;

import java.io.File;

import static java.io.File.separator;

public enum DataFile {

    CONFIG("config.yml"),
    PURCHASE_MENU("purchase_menu.yml"),
    DECORATIVE_ITEMS("items", separator, "decorative.yml"),
    DEFAULT_ITEMS("items", separator, "default.yml"),
    DAILY_SHOP("shops" + separator + "daily_shop.yml"),
    WEEKLY_SHOP("shops" + separator + "weekly_shop.yml");

    private final String path;

    DataFile(String... paths) {
        StringBuilder builder = new StringBuilder();

        for (String path : paths) builder.append(path);

        this.path = builder.toString();
    }

    public static void createAll(ShopPlugin plugin) {
        for (DataFile file : values()) {
            if (!new File(Folder.PLUGIN, file.getPath()).exists())
                plugin.saveResource(file.getPath(), false);
        }
    }

    public File getFile() {
        return new File(Folder.PLUGIN, path);
    }

    public String getPath() {
        return path;
    }
}
