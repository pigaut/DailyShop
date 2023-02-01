package com.fulvio.dailyshop.config;

import com.fulvio.dailyshop.config.serializer.EntrySerializer;
import com.fulvio.dailyshop.shop.ShopPage;
import com.fulvio.dailyshop.shop.entry.EntryType;
import com.fulvio.dailyshop.shop.entry.ShopEntry;
import com.fulvio.dailyshop.shop.menu.ShopMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopData extends Config {

    public ShopData(File file) {
        super(file);
    }

    public List<ShopPage> getPages(ShopMenu shop) {
        List<ShopPage> pages = new ArrayList<>();

        List<String> entries = config.getStringList("entries");

        final int entriesPerPage = shop.getSettings().getPageEntries();
        final int pageCount = ((entries.size() + entriesPerPage) / entriesPerPage) - 1;

        for (int page = 0; page < pageCount; page++) {

            List<ShopEntry> pageEntries = new ArrayList<>();

            for (int slot = 0; slot < entriesPerPage; slot++) {

                int index = (page * entriesPerPage) + slot;

                Map<String, String> entryData = EntrySerializer.deserialize(entries.get(index));

                if (entryData == null) {
                    pageEntries.add(null);
                    continue;
                }

                EntryType type = shop.getSettings().getEntryType(entryData.get("type"));

                if (type == null) {
                    pageEntries.add(null);
                    continue;
                }

                pageEntries.add(new ShopEntry(shop.getShopId(), type, shop.getSettings().getPurchaseSlot(slot), entryData));
            }
            pages.add(new ShopPage(shop, page, pageEntries));
        }

        return pages;
    }

}
