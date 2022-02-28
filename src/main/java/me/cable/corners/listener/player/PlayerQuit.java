package me.cable.corners.listener.player;

import me.cable.corners.manager.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuit implements Listener {

    @EventHandler
    private void event(@NotNull PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerManager.remove(player);
    }
}
