package me.cable.corners.util;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultValues {

    private final List<Group> values = List.of(
            new Group(Material.BLUE_CONCRETE, "&9&lBlue Platform"),
            new Group(Material.RED_CONCRETE, "&c&lRed Platform"),
            new Group(Material.LIME_CONCRETE, "&a&lGreen Platform"),
            new Group(Material.YELLOW_CONCRETE, "&e&lYellow Platform")
    );

    private Group current = values.get(0);
    private int i = 0;

    public void next() {
        if (i + 1 < values.size()) {
            current = values.get(++i);
        }
    }

    public @NotNull Material getCurrentMaterial() {
        return current.material();
    }

    public @NotNull String getCurrentName() {
        return current.name();
    }

    private record Group(@NotNull Material material, @NotNull String name) {

    }
}
