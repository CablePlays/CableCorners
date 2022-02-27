package me.cable.corners.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public final class Utils {

    public static @NotNull String format(@NotNull String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
