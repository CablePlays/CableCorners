package me.cable.corners.handler;

import me.cable.corners.component.region.Venue;
import me.cable.corners.menu.EditingMenu;
import me.cable.corners.menu.SelectionMenu;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class VenueHandler {

    private static final Map<Integer, Venue> venues = new HashMap<>();

    public static @NotNull List<Venue> getActiveVenues() {
        List<Venue> list = new ArrayList<>();

        for (Venue venue : venues.values()) {
            if (venue.isActive()) {
                list.add(venue);
            }
        }

        return list;
    }

    public static int getNextFreeId() {
        for (int i = 0; true; i++) {
            if (!venues.containsKey(i)) {
                return i;
            }
        }
    }

    public static @NotNull List<Venue> getVenues() {
        return List.copyOf(venues.values());
    }

    public static @NotNull List<Venue> getVenuesOrdered() {
        List<Venue> list = new ArrayList<>();

        for (Entry<Integer, Venue> entry : venues.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
            list.add(entry.getValue());
        }

        return list;
    }

    public static void registerVenue(@NotNull Venue venue) {
        venues.put(venue.getId(), venue);
    }

    public static void unregisterAndRemoveVenues() {
        List<Venue> list = getVenues();
        venues.clear();
        list.forEach(Venue::removePlatforms);
    }

    public static void unregisterAndRemoveVenue(@NotNull Venue venue) {
        venues.remove(venue.getId());
        venue.removePlatforms();

        int index = getIndex(venue);

        EditingMenu.closeWithVenue(venue);
        SelectionMenu.onVenueRemove(venue, index);
    }

    public static int getIndex(@NotNull Venue venue) {
        return getVenuesOrdered().indexOf(venue);
    }
}
