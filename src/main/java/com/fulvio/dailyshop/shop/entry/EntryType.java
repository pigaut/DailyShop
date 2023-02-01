package com.fulvio.dailyshop.shop.entry;

import com.fulvio.dailyshop.config.Config;
import com.fulvio.dailyshop.generator.Amount;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class EntryType {

    private final String name;

    private final ItemStack display;

    private final Amount amount;

    private final ItemStack item;

    private final List<String> commands;

    private final int cost;

    private final int chance;

    private final int limit;

    private int count = 0;

    public EntryType(String name, Config config) {
        this.name = name;
        this.display = config.getCustomItem("display", config.getCustomItem("item"));
        this.amount = config.getAmount("stock");
        this.cost = config.getInt("cost", 100);
        this.chance = config.getInt("chance", 100);
        this.limit = config.getInt("limit", -1);
        this.item = config.getCustomItem("item");
        this.commands = config.getStringList("commands");
    }

    public String getName() {
        return name;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public int getAmount() {
        return amount.get();
    }

    public int getCost() {
        return cost;
    }

    public int getChance() {
        return chance;
    }

    public int getLimit() {
        return limit;
    }

    public boolean isItemSet() {
        return item != null;
    }

    public ItemStack getItem() {
        return item;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean runChance() {
        Random random = new Random();
        return random.nextInt(100) + 1 <= chance;
    }

    public void increaseCount() {
        this.count++;
    }

    public void resetCount() {
        this.count = 0;
    }

    public boolean isOverLimit() {
        return limit != -1 ? count >= limit : false;
    }

}
