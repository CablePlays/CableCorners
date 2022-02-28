package me.cable.corners.listener.inventory;

import me.cable.corners.menu.AbstractMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class InventoryClose implements Listener {

    @EventHandler
    private void event(@NotNull InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();

        if (inventory.getHolder() instanceof AbstractMenu abstractMenu) {
            abstractMenu.onInventoryClose(e);
        }
    }
}
