package me.cable.corners.manager;

import me.cable.corners.component.region.Platform;
import me.cable.corners.component.region.Venue;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class PlayerManager {

    private static final Map<Player, EditingNameEntry> editingName = new HashMap<>();

    public static void add(@NotNull Player player, @NotNull Venue venue, @NotNull Platform platform) {
        editingName.put(player, new EditingNameEntry(venue, platform));
    }

    public static void remove(@NotNull Player player) {
        editingName.remove(player);
    }

    public static @Nullable EditingNameEntry getEditingName(@NotNull Player player) {
        return editingName.get(player);
    }

    public record EditingNameEntry(Venue venue, Platform platform) implements Map.Entry<Venue, Platform> {

        @Override
        public @NotNull Venue getKey() {
            return venue;
        }

        @Override
        public @NotNull Platform getValue() {
            return platform;
        }

        @Override
        public @NotNull Platform setValue(Platform value) {
            throw new RuntimeException("Method should not be used");
        }
    }
}
