package com.fulvio.dailyshop.config.directory;

import com.fulvio.dailyshop.ShopPlugin;

import java.io.File;

import static java.io.File.separator;

public enum Folder {

    ITEMS("items"),
    SHOPS("shops"),
    SHOP_DATA("data", separator, "shop"),
    USER_DATA("data", separator, "user");

    public static final File PLUGIN = new File("plugins" + separator + "DailyShop" + separator);

    private final String path;

    Folder(String... paths) {
        StringBuilder builder = new StringBuilder();

        for (String path : paths) builder.append(path);

        this.path = builder.toString();
    }

    public static void createAll(ShopPlugin plugin) {
        for (Folder folder : Folder.values()) {
            new File(PLUGIN, folder.getPath()).mkdirs();
        }
    }

    public static File getFile(DataFile file) {
        return new File(PLUGIN, file.getPath());
    }

    public File getSubFile(String name) {
        return new File(getFile(), name);
    }

    public File[] getSubFiles() {
        return this.getFile().listFiles();
    }

    public String getPath() {
        return path;
    }

    public File getFile() {
        return new File(PLUGIN, path);
    }

}
