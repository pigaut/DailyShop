package com.fulvio.dailyshop.shop.menu;

import com.fulvio.dailyshop.DailyShopPlugin;
import com.fulvio.dailyshop.config.ShopConfig;
import com.fulvio.dailyshop.config.ShopData;
import com.fulvio.dailyshop.config.directory.Folder;
import com.fulvio.dailyshop.shop.ShopID;
import com.fulvio.dailyshop.shop.ShopPage;
import com.fulvio.dailyshop.shop.ShopSettings;
import com.fulvio.dailyshop.shop.action.MenuAction;
import com.fulvio.dailyshop.shop.task.ShopResetTask;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShopMenu extends MenuTemplate {

    private final ShopID shopId;
    private final List<ShopPage> pages = new ArrayList<>();
    private final Map<Integer, MenuAction> slotActions;
    private final String permission;
    private final ShopSettings settings;
    private final BukkitRunnable resetTask;
    private LocalDateTime lastReset;

    public ShopMenu(String name, ShopConfig config) {
        super(config);
        this.slotActions = config.getSlotActions();
        this.permission = config.getString("permission");
        this.settings = new ShopSettings(config);
        this.resetTask = new ShopResetTask(this);

        final ShopData shopData = new ShopData(Folder.SHOP_DATA.getSubFile(name + ".yml"));

        if (shopData.exists()) {
            this.shopId = new ShopID(name, shopData.getUUID("state"));
            this.lastReset = shopData.getDate("last-reset");
            this.pages.addAll(shopData.getPages(this));
        } else {
            this.shopId = new ShopID(name);
        }

        this.resetTask.runTaskTimerAsynchronously(DailyShopPlugin.get(), getNextReset(), settings.getRefresh());
    }

    public ShopID getShopId() {
        return shopId;
    }

    public String getName() {
        return shopId.getName();
    }

    public String getFileName() {
        return getName() + ".yml";
    }

    public UUID getState() {
        return shopId.getState();
    }

    public BukkitRunnable getResetTask() {
        return resetTask;
    }

    public void newState() {
        shopId.newState();
        pages.clear();
        settings.reset();
        lastReset = LocalDateTime.now();
    }

    public void addPage(ShopPage page) {
        pages.add(page);
    }

    public void clearPages() {
        pages.clear();
    }

    public List<ShopPage> getPages() {
        return pages;
    }

    public ShopPage getPage(int index) {
        if (index >= pages.size()) return null;
        return pages.get(index);
    }

    public String getPermission() {
        return permission;
    }

    public ShopSettings getSettings() {
        return settings;
    }

    public Map<Integer, MenuAction> getSlotActions() {
        return slotActions;
    }

    public long getNextReset() {
        if (lastReset == null) return 0L;

        long timePassed = Duration.between(lastReset, LocalDateTime.now()).toSeconds();
        if (timePassed < 0) return 0L;

        return timePassed < 0 ? 0L : settings.getRefresh() - timePassed;
    }

    public LocalDateTime getLastReset() {
        return lastReset;
    }

    public void setLastReset(LocalDateTime lastReset) {
        this.lastReset = lastReset;
    }

}
