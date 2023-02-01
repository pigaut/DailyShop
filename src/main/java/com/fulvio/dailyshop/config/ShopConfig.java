package com.fulvio.dailyshop.config;

import com.fulvio.dailyshop.shop.action.MenuAction;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopConfig extends Config {

    public ShopConfig(File file) {
        super(file);
    }

    public String getMenuName() {
        return getColorString("name");
    }

    public Integer getMenuSize() {
        return getMenuArrangement().size();
    }

    public ItemStack[] getMenuContents() {

        Map<String, ItemStack> mappedItems = getMappedItems();
        List<String> arrangement = getMenuArrangement();

        ItemStack[] contents = new ItemStack[arrangement.size()];

        for (int i = 0; i < arrangement.size(); i++) {

            String key = arrangement.get(i);

            if ("0".equalsIgnoreCase(key)) {
                contents[i] = null;
            }

            contents[i] = mappedItems.get(key);

        }

        return contents;
    }


    public Map<String, ItemStack> getMappedItems() {

        Map<String, ItemStack> mappedItems = new HashMap<>();

        for (String key : getSection("items").getKeys(false)) {
            mappedItems.put(key, getCustomItem("items." + key));
        }

        return mappedItems;
    }

    public Map<String, MenuAction> getButtons() {
        Map<String, MenuAction> buttons = new HashMap<>();

        for (String key : getSection("buttons").getKeys(false)) {
            buttons.put(key, getAction(key));
        }

        return buttons;
    }

    public Map<Integer, MenuAction> getSlotActions() {
        Map<String, MenuAction> buttons = getButtons();
        List<String> arrangement = getMenuArrangement();

        Map<Integer, MenuAction> actions = new HashMap<>();

        for (int i = 0; i < arrangement.size(); i++) {
            actions.put(i, buttons.get(arrangement.get(i)));
        }

        return actions;
    }

    public MenuAction getAction(String id) {
        return MenuAction.valueOf(config.getString("buttons." + id).toUpperCase().replace(" ", "_"));
    }

    public List<String> getMenuArrangement() {
        List<String> menuArrangement = new ArrayList<>();

        for (String row : config.getStringList("menu")) {
            for (String slot : row.split(" ")) {
                if (menuArrangement.size() > 53) break;
                menuArrangement.add(slot);
            }
        }

        return menuArrangement;
    }

}
