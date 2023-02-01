package com.fulvio.dailyshop.shop;

import java.util.Objects;
import java.util.UUID;

public class ShopID {

    private final String name;

    private UUID state;

    public ShopID(String name, UUID state) {
        this.name = name;
        this.state = state;
    }

    public ShopID(String name) {
        this(name, UUID.randomUUID());
    }

    public String getName() {
        return name;
    }

    public UUID getState() {
        return state;
    }

    public void newState() {
        this.state = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopID shopState = (ShopID) o;
        return Objects.equals(name, shopState.name) && Objects.equals(state, shopState.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, state);
    }
}
