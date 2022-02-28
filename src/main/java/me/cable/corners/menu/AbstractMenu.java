package me.cable.corners.menu;

import me.cable.corners.CableCorners;
import me.cable.corners.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
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

public abstract class AbstractMenu implements InventoryHolder {

    private static final List<AbstractMenu> openMenus = new ArrayList<>();

    protected final Player player;
    protected final CableCorners cableCorners;

    private final NamespacedKey key;
    private @Nullable Inventory openInventory;

    protected AbstractMenu(@NotNull Player player, @NotNull CableCorners cableCorners) {
        this.player = player;
        this.cableCorners = cableCorners;

        key = new NamespacedKey(cableCorners, "key");
    }

    public static @NotNull List<AbstractMenu> getOpenMenus() {
        return List.copyOf(openMenus);
    }

    public static void closeMenus(Class<? extends AbstractMenu> clazz) {
        for (AbstractMenu abstractMenu : getOpenMenus()) {
            if (abstractMenu.getClass().isAssignableFrom(clazz)) {
                abstractMenu.player.closeInventory();
            }
        }
    }

    public void open() {
        if (openInventory == null) {
            openInventory = getInventory();
            openMenus.add(this);
        }

        player.openInventory(openInventory);
    }

    public void close() {
        if (openInventory != null) {
            for (HumanEntity humanEntity : List.copyOf(openInventory.getViewers())) {
                humanEntity.closeInventory();
            }
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

    public @Nullable Inventory getOpenInventory() {
        return openInventory;
    }

    public boolean isOpen() {
        return (getOpenInventory() != null);
    }

    protected abstract @NotNull String title();

    protected abstract int rows();

    protected boolean fill() {
        return false;
    }

    protected boolean preventClick() {
        return false;
    }

    protected void apply(@NotNull Inventory inventory) {
        // override
    }

    protected void fill(@NotNull Inventory inventory) {
        ItemStack item = ItemUtils.fillItem();

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, item);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, rows() * 9, title());

        if (fill()) {
            fill(inventory);
        }

        apply(inventory);
        return inventory;
    }

    public final void onInventoryClick(@NotNull InventoryClickEvent inventoryClickEvent) {
        if (preventClick()) {
            inventoryClickEvent.setCancelled(true);
        }

        onClick(inventoryClickEvent);

        if (inventoryClickEvent.getWhoClicked() instanceof Player player) {
            ItemStack item = inventoryClickEvent.getCurrentItem();

            if (item != null) {
                String tag = getTag(item);

                if (tag != null) {
                    onClick(inventoryClickEvent, tag);
                }
            }
        }
    }

    public final void onInventoryClose(@NotNull InventoryCloseEvent inventoryCloseEvent) {
        if (inventoryCloseEvent.getInventory().getViewers().size() <= 1) {
            openInventory = null;
            openMenus.remove(this);
        }

        onClose(inventoryCloseEvent);
    }

    protected void onClick(@NotNull InventoryClickEvent inventoryClickEvent) {
        // override
    }

    protected void onClick(@NotNull InventoryClickEvent inventoryClickEvent, @NotNull String tag) {
        // override
    }

    protected void onClose(@NotNull InventoryCloseEvent inventoryCloseEvent) {
        // override
    }
}
