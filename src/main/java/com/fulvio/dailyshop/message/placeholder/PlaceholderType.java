package com.fulvio.dailyshop.message.placeholder;

public enum PlaceholderType {

    SHOP_NAME("%shop_name%"),
    SHOP_PAGE("%shop_page%"),
    ENTRY_NAME("%entry_name%"),
    ENTRY_STOCK("%entry_stock%"),
    ENTRY_STOCK_PLAYER("%entry_stock_player%"),
    ENTRY_PURCHASED("%entry_purchased%"),
    ENTRY_COST("%entry_cost%"),

    ENTRY_AMOUNT("%entry_amount%"),
    ENTRY_COST_ALL("%entry_cost_all%");

    private final String id;

    private final String param;

    PlaceholderType(String id) {
        this.id = id;
        this.param = id.replace("%", "");
    }

    public String getId() {
        return id;
    }

    public String getParam() {
        return param;
    }

    public Placeholder of(Object value) {
        return new Placeholder(this, String.valueOf(value));
    }
}
