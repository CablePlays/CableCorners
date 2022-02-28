package me.cable.corners.menu;

import me.cable.corners.CableCorners;
import me.cable.corners.component.region.Platform;
import me.cable.corners.component.region.Venue;
import me.cable.corners.manager.VenueManager;
import me.cable.corners.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SelectionMenu extends AbstractMenu {

    private final Venue venue;

    private final int[][] materialSlots = {
            {10, 11, 19, 20},
            {12, 13, 21, 22},
            {30, 31, 39, 40},
            {28, 29, 37, 38}
    };

    public SelectionMenu(@NotNull Player player, Venue venue, @NotNull CableCorners cableCorners) {
        super(player, cableCorners);
        this.venue = venue;
    }

    public static void onVenueRemove(@NotNull Venue venue, int index) {
        index = Math.max(index - 1, 0);

        List<Venue> list = VenueManager.getVenues();
        Venue newVenue = (list.size() > index) ? list.get(index) : null;

        for (AbstractMenu abstractMenu : getOpenMenus()) {
            if (abstractMenu instanceof SelectionMenu selectionMenu && selectionMenu.venue.equals(venue)) {
                if (newVenue == null) {
                    selectionMenu.close();
                } else {
                    new SelectionMenu(selectionMenu.player, newVenue, abstractMenu.cableCorners).open();
                }
            }
        }
    }

    public static void updateMenus(@NotNull Venue venue) {
        for (AbstractMenu abstractMenu : getOpenMenus()) {
            if (abstractMenu instanceof SelectionMenu selectionMenu && selectionMenu.venue.equals(venue)) {
                new SelectionMenu(selectionMenu.player, venue, selectionMenu.cableCorners).open();
            }
        }
    }

    private @NotNull List<Venue> getVenues() {
        return VenueManager.getVenuesOrdered();
    }

    @Override
    protected @NotNull String title() {
        return ChatColor.BLUE + "" + ChatColor.BOLD + "Selection Menu";
    }

    @Override
    protected int rows() {
        return 6;
    }

    @Override
    protected boolean fill() {
        return true;
    }

    @Override
    protected boolean preventClick() {
        return true;
    }

    @Override
    protected void apply(@NotNull Inventory inventory) {
        for (int i = 0; i < 4; i++) {
            Platform platform = venue.getPlatforms().get(i);
            Material material = platform.getMaterial();
            String name = platform.getName();
            ItemStack item = ItemUtils.item(material, name);

            for (int slot : materialSlots[i]) {
                inventory.setItem(slot, item);
            }
        }

        int index = getIndex();

        if (index >= 0) {
            {
                ItemStack previous;

                if (index > 0) {
                    previous = ItemUtils.item(Material.ARROW, ChatColor.WHITE + "" + ChatColor.BOLD + "Previous Page",
                            List.of(ChatColor.GRAY + "Click to go to the previous page."));
                    setTag(previous, "PREVIOUS");
                } else {
                    previous = ItemUtils.item(Material.GRAY_DYE, ChatColor.GRAY + "" + ChatColor.BOLD + "Previous Page");
                }

                inventory.setItem(24, previous);
            }
            {
                ItemStack next;

                if (hasNext()) {
                    next = ItemUtils.item(Material.ARROW, ChatColor.WHITE + "" + ChatColor.BOLD + "Next Page",
                            List.of(ChatColor.GRAY + "Click to go to the next page."));
                } else {
                    next = ItemUtils.item(Material.GRAY_DYE, ChatColor.GRAY + "" + ChatColor.BOLD + "Next Page");
                }

                setTag(next, "NEXT");
                inventory.setItem(25, next);
            }
        }
        {
            ItemStack edit = ItemUtils.item(Material.LIME_DYE, ChatColor.GREEN + "" + ChatColor.BOLD + "Edit",
                    List.of(ChatColor.GRAY + "Click to edit this venue."));
            setTag(edit, "EDIT");
            inventory.setItem(33, edit);
        }
        {
            ItemStack remove = ItemUtils.item(Material.RED_DYE, ChatColor.RED + "" + ChatColor.BOLD + "Remove",
                    List.of(ChatColor.GRAY + "Click to remove this venue."));
            setTag(remove, "REMOVE");
            inventory.setItem(34, remove);
        }
    }

    private int getIndex() {
        return VenueManager.getIndex(venue);
    }

    private int getTotal() {
        return VenueManager.getVenues().size();
    }

    private boolean hasNext() {
        return (getTotal() > getIndex() + 1);
    }

    @Override
    protected void onClick(@NotNull InventoryClickEvent e, @NotNull String tag) {
        if (!(e.getWhoClicked() instanceof Player player)) return;

        switch (tag) {
            case "PREVIOUS" -> {
                int index = getIndex();

                if (index > 0) {
                    Venue venue = getVenues().get(index - 1);
                    new SelectionMenu(player, venue, cableCorners).open();
                }
            }
            case "NEXT" -> {
                int index = getIndex();

                if (hasNext()) {
                    Venue venue = getVenues().get(index + 1);
                    new SelectionMenu(player, venue, cableCorners).open();
                }
            }
            case "EDIT" -> new EditingMenu(player, venue, cableCorners).open();
            case "REMOVE" -> {
                int index = getIndex();
                VenueManager.unregisterAndRemoveVenue(venue);
                onVenueRemove(venue, index);
            }
        }
    }
}
