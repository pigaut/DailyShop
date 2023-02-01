package com.fulvio.dailyshop.help;

import com.fulvio.dailyshop.shop.entry.EntryType;

import java.util.Comparator;

public class ItemComparator implements Comparator<EntryType> {

    public int compare(EntryType item1, EntryType item2) {
        if (item1 == null) return +1;
        if (item2 == null) return -1;
        return Integer.compare(item1.getCost(), item2.getCost());
    }

}
