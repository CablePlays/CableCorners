package me.cable.corners.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class Utils {

    public static @NotNull String format(@NotNull String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
<<<<<<< HEAD

    public static void playSound(@NotNull Player player, @NotNull Sound sound) {
        player.playSound(player.getLocation(), sound, 1, 1);
    }

    @SafeVarargs
    public static <T> @NotNull List<T> listOf(T... args) {
        return Arrays.asList(args);
    }

    public static <T> @NotNull List<T> listCopyOf(@NotNull Collection<T> list) {
        return new ArrayList<>(list);
    }
=======
>>>>>>> parent of 3d5a745 (Visual updates)
}
