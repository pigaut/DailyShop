package com.fulvio.dailyshop.shop;

import com.fulvio.dailyshop.config.ShopConfig;
import com.fulvio.dailyshop.generator.Amount;
import com.fulvio.dailyshop.shop.action.MenuAction;
import com.fulvio.dailyshop.shop.entry.EntryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShopSettings {

    private final long refresh;

    private final Amount entryAmount;

    private final List<Integer> purchaseSlots;

    private final List<EntryType> entries;

    public ShopSettings(ShopConfig config) {
        this.refresh = config.getMinutesInTicks("refresh");
        this.entryAmount = config.getAmount("entry-amount");

        this.purchaseSlots = new ArrayList<>();

        for (Map.Entry<Integer, MenuAction> entry : config.getSlotActions().entrySet()) {
            if (entry.getValue() != MenuAction.BUY) continue;
            purchaseSlots.add(entry.getKey());
        }

        this.entries = new ArrayList<>();

        for (String entryId : config.getSection("entries").getKeys(false)) {
            entries.add(new EntryType(entryId, config.getConfig("entries." + entryId)));
        }
    }

    public int getPageEntries() {
        return purchaseSlots.size();
    }

    public long getRefresh() {
        return refresh;
    }

    public int getTotalItems() {
        return entryAmount.get();
    }

    public List<Integer> getPurchaseSlots() {
        return purchaseSlots;
    }

    public Integer getPurchaseSlot(int index) {
        return purchaseSlots.get(index);
    }

    public EntryType getEntryType(String id) {
        for (EntryType entryType : entries) {
            if (entryType.getName().equals(id)) return entryType;
        }
        return null;
    }

    public EntryType getRandomItem() {
        return entries.get(new Random().nextInt(entries.size()));
    }

    public boolean allLimitsReached() {
        for (EntryType entry : entries) {
            if (entry.isOverLimit()) continue;
            return false;
        }
        return true;
    }

    public void reset() {
        for (EntryType item : entries) item.resetCount();
    }

}
