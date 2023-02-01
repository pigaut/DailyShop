package com.fulvio.dailyshop.command;

import com.fulvio.dailyshop.DailyShopPlugin;
import com.fulvio.dailyshop.config.Config;
import com.fulvio.dailyshop.config.Settings;
import com.fulvio.dailyshop.config.directory.Folder;
import com.fulvio.dailyshop.message.Message;
import com.fulvio.dailyshop.user.ShopUser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCmdExecutor implements CommandExecutor {

    private final DailyShopPlugin plugin;

    public ShopCmdExecutor(DailyShopPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            if (args.length > 2 && args[0].equalsIgnoreCase("OPEN")) {
                ShopUser user = plugin.getUsers().getUser(Bukkit.getPlayer(args[1]));
                if (user == null) return false;
                user.openShop(plugin.getShops().getShop(args[2]));
            }
            sender.sendMessage("/DailyShop open <player> <shop>");
            return false;
        }

        if (!sender.hasPermission("dailyshop.user")) return false;

        ShopUser user = plugin.getUsers().getUser(((Player) sender));

        if (user == null) {
            sender.sendMessage("Data is loading");
            return false;
        }

        if (user.getOpenMenu() != null) {
            user.sendMessage("You already have a menu open");
            return false;
        }

        if (args.length < 1) {
            if (Settings.DATA.getDefaultShop() != null) {
                user.openShop(plugin.getShops().getShop(Settings.DATA.getDefaultShop()));
            } else {
                sender.sendMessage("/DailyShop <name>");
            }
            return true;
        }

        if ("RELOAD".equalsIgnoreCase(args[0])) {
            if (!sender.hasPermission("dailyshop.admin")) return false;
            plugin.unload();
            plugin.load();

            sender.sendMessage("Reloading plugin...");
            return true;
        }

        if ("ADDITEM".equalsIgnoreCase(args[0])) {
            if (!sender.hasPermission("dailyshop.admin")) return false;
            if (args.length < 3) {
                sender.sendMessage("usage: /dailyshop additem <name> <file>");
                return false;
            }

            Config config = new Config(Folder.ITEMS.getSubFile(args[2]));

            config.set(args[1], ((Player) sender).getInventory().getItemInMainHand());

            config.save();

            sender.sendMessage("Added " + args[1] + " to " + args[2] + ".yml");
            return true;
        }

        if (plugin.getShops().getShop(args[0]) == null) {
            user.sendMessage(Message.INVALID_SHOP);
            return false;
        }

        user.openShop(plugin.getShops().getShop(args[0]));

        return true;
    }

}
