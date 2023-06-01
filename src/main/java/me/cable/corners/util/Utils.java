package me.cable.corners.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Utils {

    public static @NotNull String format(@NotNull String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void playSound(@NotNull Player player, @NotNull Sound sound) {
        player.playSound(player.getLocation(), sound, 1, 1);
    }

    public static @NotNull Material materialFromString(@Nullable String string, @NotNull Material def) {
        Material material = (string == null) ? null : Material.getMaterial(string);
        return (material == null) ? def : material;
    }
}
