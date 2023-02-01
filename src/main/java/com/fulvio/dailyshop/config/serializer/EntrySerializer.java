package com.fulvio.dailyshop.config.serializer;

import com.fulvio.dailyshop.shop.ShopPage;
import com.fulvio.dailyshop.shop.entry.ShopEntry;
import com.fulvio.dailyshop.shop.menu.ShopMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EntrySerializer {

    public static List<String> serialize(ShopMenu shop) {

        List<String> entries = new ArrayList<>();

        for (ShopPage page : shop.getPages()) {

            StringBuilder builder;

            for (ShopEntry entry : page.entries()) {

                if (entry == null) {
                    entries.add("null");
                    continue;
                }

                builder = new StringBuilder();

                builder.append("%uuid%#").append(entry.getUniqueId()).append("#");
                builder.append("%type%#").append(entry.getType().getName()).append("#");
                builder.append("%stock%#").append(entry.getStock()).append("#");
                builder.append("%cost%#").append(entry.getCost()).append("#");

                entries.add(builder.toString());
            }
        }

        return entries;
    }

    public static Map<String, String> deserialize(String serializedEntry) {

        if (serializedEntry.equalsIgnoreCase("null")) return null;

        Map<String, String> entryData = new HashMap<>();

        boolean read = false;

        String field = null;

        StringBuilder builder = new StringBuilder();

        for (char character : serializedEntry.toCharArray()) {

            if (character == '%') {

                if (read) {
                    field = builder.toString();
                    builder.setLength(0);
                }

                read = !read;
                continue;
            }

            if (character == '#') {

                if (read) {
                    entryData.put(field, builder.toString());
                    builder.setLength(0);
                    field = null;
                }

                read = !read;
                continue;

            }

            if (read) {
                builder.append(character);
            }

        }

        return entryData;
    }


}
