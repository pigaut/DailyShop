package com.fulvio.dailyshop.user;

import com.fulvio.dailyshop.config.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PurchaseRegistry {

    private final Map<UUID, Integer> purchases = new HashMap<>();

    public PurchaseRegistry() {
    }

    public PurchaseRegistry(Config config) {
        for (String key : config.getKeys(false)) {
            if (key.equalsIgnoreCase("state")) continue;
            purchases.put(UUID.fromString(key), config.getInt(key));
        }
    }

    public Map<UUID, Integer> getPurchases() {
        return purchases;
    }

    public int getPurchases(UUID entryId) {
        return purchases.getOrDefault(entryId, 0);
    }

    public void add(UUID entryId, int amount) {
        purchases.put(entryId, purchases.getOrDefault(entryId, 0) + amount);
    }

}
