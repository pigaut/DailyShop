package com.fulvio.dailyshop.message.placeholder;

import com.fulvio.dailyshop.ShopPlugin;
import com.fulvio.dailyshop.config.Settings;
import com.fulvio.dailyshop.shop.menu.active.OpenMenu;
import com.fulvio.dailyshop.user.ShopUser;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {

    private final ShopPlugin plugin;

    public Placeholders(ShopPlugin plugin) {
        this.plugin = plugin;
    }

    public static String set(ShopUser user, String text) {
        if (Settings.DATA.isPapi()) {
            text = PlaceholderAPI.setPlaceholders(user.getPlayer(), text);
        }

        return text;
    }

    public static String set(String text, ShopUser user, Placeholder[] placeholders) {
        for (Placeholder placeholder : placeholders) {
            if (placeholder == null) continue;
            text = placeholder.set(text);
        }

        if (Settings.DATA.isPapi())
            text = PlaceholderAPI.setPlaceholders(user.getPlayer(), text);

        return text;
    }

    @Override
    public @NotNull String getIdentifier() {
        return null;
    }

    @Override
    public @NotNull String getAuthor() {
        return null;
    }

    @Override
    public @NotNull String getVersion() {
        return null;
    }

    public String onPlaceholderRequest(Player player, String param) {

        ShopUser user = plugin.getUsers().getUser(player);

        if (user == null) return null;

        OpenMenu openMenu = user.getOpenMenu();

        if (openMenu == null) return null;

        if (PlaceholderType.SHOP_NAME.getParam().equalsIgnoreCase(param)) {
            return openMenu.getShop().getName();
        }

        if (PlaceholderType.SHOP_PAGE.getParam().equalsIgnoreCase(param)) {
            return String.valueOf(openMenu.getOpenShop().getShopPage().page());
        }

        return null;
    }

}
