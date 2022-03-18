package me.cable.corners.manager;

import me.cable.corners.component.region.Venue;
<<<<<<< HEAD
<<<<<<< HEAD
import me.cable.corners.menu.EditingMenu;
import me.cable.corners.menu.SelectionMenu;
import me.cable.corners.util.Utils;
=======
>>>>>>> parent of 3d5a745 (Visual updates)
=======
>>>>>>> parent of 3d5a745 (Visual updates)
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.Map.Entry;

public final class VenueManager {

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
        return Utils.listCopyOf(venues.values());
    }

    public static @NotNull List<Venue> getVenuesOrdered() {
        List<Venue> list = new ArrayList<>();

        for (Iterator<Entry<Integer, Venue>> it = venues.entrySet().stream().sorted(Entry.comparingByKey()).iterator(); it.hasNext(); ) {
            Entry<Integer, Venue> entry = it.next();
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
    }

    public static int getIndex(@NotNull Venue venue) {
        return getVenuesOrdered().indexOf(venue);
    }
}
