package me.cable.corners.menu;

import me.cable.corners.CableCorners;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu implements InventoryHolder {

    private static final List<Menu> openMenus = new ArrayList<>();

    protected final CableCorners cableCorners;


    protected final Player player;
    private final NamespacedKey key;

    private boolean opening;

    protected Menu(@NotNull CableCorners cableCorners, @NotNull Player player) {
        this.cableCorners = cableCorners;
        this.player = player;
        key = new NamespacedKey(cableCorners, "key");
    }

    @SuppressWarnings("unchecked")
    public static <T extends Menu> List<T> getOpenMenus(@NotNull Class<T> clazz) {
        List<T> list = new ArrayList<>();

        for (Menu menu : openMenus) {
            if (menu.getClass().isAssignableFrom(clazz)) {
                list.add((T) menu);
            }
        }

        return list;
    }

    public static void closeMenus() {
        for (Menu menu : List.copyOf(openMenus)) {
            menu.close();
        }
    }

    public static void closeMenus(@NotNull Class<? extends Menu> clazz) {
        for (Menu menu : getOpenMenus(clazz)) {
            menu.close();
        }
    }

    public final void open() {
        Inventory inventory = getInventory();

        opening = true;
        player.openInventory(inventory);
        opening = false;

        if (!openMenus.contains(this)) {
            openMenus.add(this);
        }
    }

    public final void close() {
        if (player.getOpenInventory().getTopInventory().getHolder() == this) {
            player.closeInventory();
        }
    }

    protected void setTag(@NotNull ItemStack item, @NotNull String tag) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, tag);
        item.setItemMeta(meta);
    }

    protected @Nullable String getTag(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return (meta == null) ? null : meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    protected abstract @NotNull String title();

    protected abstract int rows();

    protected boolean preventClick() {
        return true;
    }

    protected void render(@NotNull Inventory inventory) {}

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, rows() * 9, title());
        render(inventory);
        return inventory;
    }

    public final void onInventoryClick(@NotNull InventoryClickEvent e) {
        if (preventClick()) {
            e.setCancelled(true);
        }

        onClick(e);
    }

    protected void onClick(@NotNull InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        String tag = (item == null) ? null : getTag(item);

        onClick(e, tag);
    }

    protected void onClick(@NotNull InventoryClickEvent e, @Nullable String tag) {

    }

    public final void onInventoryClose(@NotNull InventoryCloseEvent e) {
        if (!opening) {
            openMenus.remove(this);
        }

        onClose(e);
    }

    protected void onClose(@NotNull InventoryCloseEvent e) {

    }
}
