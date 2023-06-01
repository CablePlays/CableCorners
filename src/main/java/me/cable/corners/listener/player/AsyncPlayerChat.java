package me.cable.corners.listener.player;

import me.cable.corners.CableCorners;
import me.cable.corners.component.region.Platform;
import me.cable.corners.component.region.Venue;
import me.cable.corners.handler.PlayerHandler;
import me.cable.corners.menu.EditingMenu;
import me.cable.corners.menu.SelectionMenu;
import me.cable.corners.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class AsyncPlayerChat implements Listener {

    private final CableCorners cableCorners;

    public AsyncPlayerChat(CableCorners cableCorners) {
        this.cableCorners = cableCorners;
    }

    @EventHandler
    private void event(@NotNull AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        PlayerHandler.EditingNameEntry editingNameEntry = PlayerHandler.getEditingName(player);

        if (editingNameEntry == null) {
            return;
        }

        String text = e.getMessage();
        Venue venue = editingNameEntry.venue();

        e.setCancelled(true);

        if (text.equals("cancel")) {
            player.sendMessage(ChatColor.BLUE + "Platform renaming cancelled.");
        } else {
            Platform platform = editingNameEntry.platform();

            platform.setName(text);
            player.sendMessage(ChatColor.BLUE + "Platform has been renamed to \"" + Utils.format(text) + ChatColor.RESET + ChatColor.BLUE + "\"");

            EditingMenu.updateWithVenue(venue);
            SelectionMenu.updateMenus(venue);
        }

        PlayerHandler.remove(player);
        Bukkit.getScheduler().runTask(cableCorners, () -> new EditingMenu(cableCorners, player, venue).open());
    }
}
