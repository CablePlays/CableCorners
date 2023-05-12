package me.cable.corners.listener.player;

import me.cable.corners.component.region.Platform;
import me.cable.corners.component.region.Venue;
import me.cable.corners.manager.PlayerManager;
import me.cable.corners.menu.EditingMenu;
import me.cable.corners.menu.SelectionMenu;
import me.cable.corners.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class AsyncPlayerChat implements Listener {

    @EventHandler
    private void event(@NotNull AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        PlayerManager.EditingNameEntry editingNameEntry = PlayerManager.getEditingName(player);

        if (editingNameEntry != null) {
            String text = e.getMessage();
            e.setCancelled(true);

            if (text.equals("cancel")) {
                player.sendMessage(ChatColor.BLUE + "Platform renaming cancelled.");
            } else {
                Venue venue = editingNameEntry.venue();
                Platform platform = editingNameEntry.platform();

                platform.setName(text);
                player.sendMessage(ChatColor.BLUE + "Platform has been renamed to \"" + Utils.format(text) + ChatColor.RESET + ChatColor.BLUE + "\"");

                EditingMenu.updateMenus(venue);
                SelectionMenu.updateMenus(venue);
            }

            PlayerManager.remove(player);
        }
    }
}
