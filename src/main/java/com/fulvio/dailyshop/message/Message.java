package com.fulvio.dailyshop.message;

import com.fulvio.dailyshop.config.Settings;

public enum Message {

    INVALID_SHOP,
    OPEN_MENU,
    CLOSE_MENU,
    SHOP_REFRESHING,
    PURCHASE,
    NO_STOCK,
    NO_MONEY,
    NO_SPACE;

    public String getMessage() {
        return Settings.DATA.getMessage(this);
    }

}
