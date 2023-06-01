package me.cable.corners.menu;

import me.cable.corners.CableCorners;
import me.cable.corners.component.Coords;
import me.cable.corners.component.region.Platform;
import me.cable.corners.component.region.Venue;
import me.cable.corners.handler.Messages;
import me.cable.corners.handler.VenueHandler;
import me.cable.corners.util.ItemUtils;
import me.cable.corners.util.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SelectionMenu extends Menu {

    private final Messages messages;

    private final Venue venue;

    private final int[][] materialSlots = {
            {10, 11, 19, 20},
            {12, 13, 21, 22},
            {30, 31, 39, 40},
            {28, 29, 37, 38}
    };

    public SelectionMenu(@NotNull CableCorners cableCorners, @NotNull Player player, Venue venue) {
        super(cableCorners, player);
        messages = cableCorners.getMessages();
        this.venue = venue;
    }

    public static void onVenueRemove(@NotNull Venue venue, int index) {
        index = Math.max(index - 1, 0);

        List<Venue> list = VenueHandler.getVenues();
        Venue newVenue = (list.size() > index) ? list.get(index) : null;

        for (SelectionMenu selectionMenu : getOpenMenus(SelectionMenu.class)) {
            if (selectionMenu.venue.equals(venue)) {
                if (newVenue == null) {
                    selectionMenu.close();
                } else {
                    new SelectionMenu(selectionMenu.cableCorners, selectionMenu.player, newVenue).open();
                }
            }
        }
    }

    public static void updateMenus(@NotNull Venue venue) {
        for (SelectionMenu selectionMenu : getOpenMenus(SelectionMenu.class)) {
            if (selectionMenu.venue.equals(venue)) {
                selectionMenu.open();
            }
        }
    }

    private @NotNull List<Venue> getVenues() {
        return VenueHandler.getVenuesOrdered();
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
    protected void render(@NotNull Inventory inventory) {

        /* Background */

        ItemStack backgroundItem = ItemUtils.create(Material.BLACK_STAINED_GLASS_PANE, "");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, backgroundItem);
        }

        /* Material Slots */

        for (int i = 0; i < 4; i++) {
            Platform platform = venue.getPlatforms().get(i);
            Material material = platform.getMaterial();
            String name = platform.getName();
            ItemStack item = ItemUtils.create(material, Utils.format(name));

            for (int slot : materialSlots[i]) {
                inventory.setItem(slot, item);
            }
        }

        /* Other Items */

        int index = getIndex();

        if (index >= 0) {
            {
                ItemStack previous;

                if (index > 0) {
                    previous = ItemUtils.create(Material.ARROW, ChatColor.WHITE + "" + ChatColor.BOLD + "Previous Page",
                            List.of(ChatColor.GRAY + "Click to go to the previous page."));
                    setTag(previous, "PREVIOUS");
                } else {
                    previous = ItemUtils.create(Material.GRAY_DYE, ChatColor.GRAY + "" + ChatColor.BOLD + "Previous Page");
                }

                inventory.setItem(15, previous);
            }
            {
                ItemStack next;

                if (hasNext()) {
                    next = ItemUtils.create(Material.ARROW, ChatColor.WHITE + "" + ChatColor.BOLD + "Next Page",
                            List.of(ChatColor.GRAY + "Click to go to the next page."));
                } else {
                    next = ItemUtils.create(Material.GRAY_DYE, ChatColor.GRAY + "" + ChatColor.BOLD + "Next Page");
                }

                setTag(next, "NEXT");
                inventory.setItem(16, next);
            }
        }
        {
            ItemStack edit = ItemUtils.create(Material.LIME_DYE, ChatColor.GREEN + "" + ChatColor.BOLD + "Edit",
                    List.of(ChatColor.GRAY + "Click to edit this venue."));
            setTag(edit, "EDIT");
            inventory.setItem(24, edit);
        }
        {
            ItemStack remove = ItemUtils.create(Material.RED_DYE, ChatColor.RED + "" + ChatColor.BOLD + "Remove",
                    List.of(ChatColor.GRAY + "Click to remove this venue."));
            setTag(remove, "REMOVE");
            inventory.setItem(25, remove);
        }
        {
            ItemStack info = ItemUtils.create(Material.ENDER_EYE, ChatColor.AQUA + "" + ChatColor.BOLD + "Page " + (index + 1),
                    List.of(ChatColor.GRAY + "You are viewing venue " + ChatColor.YELLOW + venue.getId() + ChatColor.GRAY + "."));
            inventory.setItem(42, info);
        }
        {
            ItemStack teleport = ItemUtils.create(Material.ENDER_PEARL, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Teleport",
                    List.of(ChatColor.GRAY + "Click to teleport to this venue."));
            setTag(teleport, "TELEPORT");
            inventory.setItem(43, teleport);
        }
    }

    private int getIndex() {
        return VenueHandler.getIndex(venue);
    }

    private int getTotal() {
        return VenueHandler.getVenues().size();
    }

    private boolean hasNext() {
        return (getTotal() > getIndex() + 1);
    }

    @Override
    protected void onClick(@NotNull InventoryClickEvent e, @Nullable String tag) {
        if (tag == null) {
            return;
        }

        switch (tag) {
            case "EDIT" -> new EditingMenu(cableCorners, player, venue).open();
            case "NEXT" -> {
                int index = getIndex();

                if (hasNext()) {
                    Venue venue = getVenues().get(index + 1);
                    new SelectionMenu(cableCorners, player, venue).open();
                }
            }
            case "PREVIOUS" -> {
                int index = getIndex();

                if (index > 0) {
                    Venue venue = getVenues().get(index - 1);
                    new SelectionMenu(cableCorners, player, venue).open();
                }
            }
            case "REMOVE" -> VenueHandler.unregisterAndRemoveVenue(venue);
            case "TELEPORT" -> {
                player.closeInventory();
                World world = venue.getWorld();

                if (world == null) {
                    messages.message("error.teleport-invalid-world")
                            .placeholder("{venue}", String.valueOf(venue.getId()))
                            .placeholder("{world}", venue.getWorldName())
                            .send(player);
                } else {
                    Coords centre = venue.getCenter();
                    Location playerLoc = player.getLocation();
                    Location location = new Location(world, centre.getX() + .5, centre.getY() + 1, centre.getZ() + .5, playerLoc.getYaw(), playerLoc.getPitch());
                    player.teleport(location);
                    Utils.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT);
                }
            }
        }
    }
}
