package com.fulvio.dailyshop.config.item;

import com.fulvio.dailyshop.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private Material type = Material.DIRT;
    private int amount = 1;

    private String name = null;
    private List<String> lore = null;
    private boolean enchanted = false;
    private int modelData = -1;

    public ItemBuilder(Config config) {
        if (config.isSet("type"))
            this.type = config.getMaterial("type");
        this.amount = config.getInt("amount", 1);
        this.name = config.getColorString("name");
        this.lore = config.getStringList("lore");
        this.lore.replaceAll(string -> ChatColor.translateAlternateColorCodes('&', string));
        this.enchanted = config.getBoolean("enchanted", false);
        this.modelData = config.getInt("model-data", -1);
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(type, amount);

        ItemMeta meta = item.getItemMeta();

        if (name != null) meta.setDisplayName(name);
        if (lore != null) meta.setLore(lore);
        if (modelData != -1) meta.setCustomModelData(modelData);
        if (enchanted) {
            meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }

}
