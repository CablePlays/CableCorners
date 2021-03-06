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

    public static class EditingNameEntry {

        private final Venue venue;
        private final Platform platform;

        public EditingNameEntry(@NotNull Venue venue, @NotNull Platform platform) {
            this.venue = venue;
            this.platform = platform;
        }

        public @NotNull Venue getVenue() {
            return venue;
        }

        public @NotNull Platform getPlatform() {
            return platform;
        }
    }
}
