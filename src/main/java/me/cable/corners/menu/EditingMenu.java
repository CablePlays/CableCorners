package me.cable.corners.menu;

import me.cable.corners.CableCorners;
import me.cable.corners.component.Coords;
import me.cable.corners.component.region.Platform;
import me.cable.corners.component.region.Venue;
import me.cable.corners.handler.PlayerHandler;
import me.cable.corners.handler.VenueHandler;
import me.cable.corners.util.ItemUtils;
import me.cable.corners.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EditingMenu extends Menu {

    private final Venue venue;
    private final int[] materialSlots = {27, 28, 37, 36};

    public EditingMenu(@NotNull CableCorners cableCorners, @NotNull Player player, @NotNull Venue venue) {
        super(cableCorners, player);
        this.venue = venue;
    }

    public static void updateWithVenue(@NotNull Venue venue) {
        for (EditingMenu editingMenu : getOpenMenus(EditingMenu.class)) {
            if (editingMenu.venue.equals(venue)) {
                editingMenu.open();
            }
        }
    }

    public static void closeWithVenue(@NotNull Venue venue) {
        for (EditingMenu editingMenu : getOpenMenus(EditingMenu.class)) {
            if (editingMenu.venue.equals(venue)) {
                editingMenu.close();
            }
        }
    }

    @Override
    protected @NotNull String title() {
        return ChatColor.BLUE + "" + ChatColor.BOLD + "Editing Menu";
    }

    @Override
    protected int rows() {
        return 6;
    }

    @Override
    protected boolean preventClick() {
        return false;
    }

    @Override
    protected void render(@NotNull Inventory inventory) {

        /* Background */

        ItemStack backgroundItem = ItemUtils.create(Material.BLACK_STAINED_GLASS_PANE, "");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, backgroundItem);
        }

        /* Other Items */

        {
            ItemStack back = ItemUtils.create(Material.ARROW, ChatColor.WHITE + "" + ChatColor.BOLD + "Selection Menu",
                    List.of(ChatColor.GRAY + "Click to go back to the selection menu."));
            setTag(back, "SELECTION_MENU");
            inventory.setItem(0, back);
        }
        {
            ItemStack status = ItemUtils.create(Material.COAL_ORE, ChatColor.GOLD + "" + ChatColor.BOLD + "Action Status", List.of(
                    ChatColor.GRAY + "If a venue is active then its game",
                    ChatColor.GRAY + "will cycle and it will be playable."
            ));
            setTag(status, "ACTIVE");
            inventory.setItem(10, status);
        }
        {
            ItemStack active;

            if (venue.isActive()) {
                active = ItemUtils.create(Material.LIME_DYE, ChatColor.GREEN + "" + ChatColor.BOLD + "Active",
                        List.of(ChatColor.GRAY + "Click to make this venue inactive."));
            } else {
                active = ItemUtils.create(Material.GRAY_DYE, ChatColor.GRAY + "" + ChatColor.BOLD + "Inactive",
                        List.of(ChatColor.GRAY + "Click to make this venue active."));
            }

            setTag(active, "ACTIVE");
            inventory.setItem(11, active);
        }
        {
            ItemStack remove = ItemUtils.create(Material.RED_DYE, ChatColor.RED + "" + ChatColor.BOLD + "Remove",
                    List.of(ChatColor.GRAY + "Click to remove this venue."));
            setTag(remove, "REMOVE");
            inventory.setItem(13, remove);
        }

        for (int i = 0; i < 4; i++) {
            Platform platform = venue.getPlatforms().get(i);
            Material material = platform.getMaterial();
            String name = platform.getName();

            ItemStack item = ItemUtils.create(material, Utils.format(name), List.of(
                    ChatColor.YELLOW + "Click" + ChatColor.GRAY + " while holding an item to set",
                    ChatColor.GRAY + "this platform's material.",
                    ChatColor.YELLOW + "Right-click" + ChatColor.GRAY + " to rename this platform."
            ));

            setTag(item, "PLATFORM_ICON_" + i);
            inventory.setItem(materialSlots[i], item);
        }

        {
            Coords coords = venue.getCenter();
            ItemStack centre = ItemUtils.create(Material.CHAIN_COMMAND_BLOCK, ChatColor.AQUA + "" + ChatColor.BOLD + "Venue Centre", List.of(
                    ChatColor.YELLOW + "X: " + coords.getX() + ", Y: " + coords.getY() + ", Z: " + coords.getZ(),
                    ChatColor.GRAY + "Click to set the centre of this venue."
            ));
            setTag(centre, "CENTRE");
            inventory.setItem(30, centre);
        }
        {
            ItemStack world = ItemUtils.create(Material.REPEATING_COMMAND_BLOCK,
                    ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Venue World",
                    List.of(
                            ChatColor.YELLOW + "World: \"" + venue.getWorldName() + "\"",
                            ChatColor.GRAY + "Click to set the world this venue is in."
                    ));
            setTag(world, "WORLD");
            inventory.setItem(31, world);
        }
        {
            ItemStack worldValidity = ItemUtils.create(Material.BEDROCK, ChatColor.GOLD + "" + ChatColor.BOLD + "World Validity", List.of(
                    ChatColor.GRAY + "Indicates if this venue's world is",
                    ChatColor.GRAY + "valid. If the world is invalid then",
                    ChatColor.GRAY + "the venue will not be usable."
            ));
            inventory.setItem(39, worldValidity);
        }
        {
            ItemStack worldValidity;

            if (venue.getWorld() == null) {
                worldValidity = ItemUtils.create(Material.GRAY_DYE, ChatColor.RED + "" + ChatColor.BOLD + "Invalid",
                        List.of(ChatColor.GRAY + "World is invalid and the venue will not be spawned."));
            } else {
                worldValidity = ItemUtils.create(Material.LIME_DYE, ChatColor.GREEN + "" + ChatColor.BOLD + "Valid",
                        List.of(ChatColor.GRAY + "World is valid and the venue will be spawned."));
            }

            inventory.setItem(40, worldValidity);
        }
        {
            int i = 0;

            for (IncrementType incrementType : List.of(
                    new IncrementType(
                            "PLATFORM_SIZE",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Platform Size (" + venue.getSize() + ")",
                            List.of(
                                    ChatColor.GRAY + "Size of each platform.",
                                    ChatColor.GRAY + "Current: " + ChatColor.YELLOW + venue.getSize() + "x" + venue.getSize()
                            )
                    ),
                    new IncrementType(
                            "PLATFORM_SPACE",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Platform Space (" + venue.getSpace() + ")",
                            List.of(ChatColor.GRAY + "Number of blocks between each platform.")
                    ),
                    new IncrementType(
                            "COUNTDOWN_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Countdown Duration (" + venue.getCountdownDuration() + ")",
                            List.of(ChatColor.GRAY + "Length of the countdown in seconds.")
                    ),
                    new IncrementType(
                            "ELIMINATION_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Elimination Duration (" + venue.getEliminationDuration() + ")",
                            List.of(ChatColor.GRAY + "Length of the elimination period in seconds.")
                    ),
                    new IncrementType(
                            "REPLACEMENT_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Replacement Duration (" + venue.getReplacementDuration() + ")",
                            List.of(ChatColor.GRAY + "Length of the replacement period in seconds.")
                    )
            )) {
                ItemStack info = ItemUtils.create(Material.OAK_SIGN, incrementType.name(), incrementType.lore());
                inventory.setItem(i + 7, info);

                ItemStack increase = ItemUtils.create(Material.LIME_STAINED_GLASS, ChatColor.GREEN + "" + ChatColor.BOLD + "Increase",
                        List.of(ChatColor.GRAY + "Click to increase."));
                setTag(increase, incrementType.tag() + "_INCREASE");
                inventory.setItem(i + 8, increase);

                ItemStack decrease = ItemUtils.create(Material.RED_STAINED_GLASS, ChatColor.RED + "" + ChatColor.BOLD + "Decrease",
                        List.of(ChatColor.GRAY + "Click to decrease."));
                setTag(decrease, incrementType.tag() + "_DECREASE");
                inventory.setItem(i + 6, decrease);

                i += (i == 9) ? 18 : 9;
            }
        }
    }

    @Override
    protected void onClick(@NotNull InventoryClickEvent e, @Nullable String tag) {
        if (player.getOpenInventory().getTopInventory().equals(e.getClickedInventory())) {
            e.setCancelled(true);
        }
        if (tag == null) {
            return;
        }

        ClickType clickType = e.getClick();
        if (clickType != ClickType.LEFT && clickType != ClickType.RIGHT) return;

        if (tag.startsWith("PLATFORM_ICON_")) {
            String[] parts = tag.split("_");
            int index = Integer.parseInt(parts[2]);
            Platform platform = venue.getPlatforms().get(index);

            if (clickType == ClickType.LEFT) {
                ItemStack cursorItem = e.getCursor();

                if (cursorItem != null) {
                    Material material = cursorItem.getType();

                    if (!material.isAir()) {
                        platform.setMaterial(material);
                        player.setItemOnCursor(null);
                        updateWithVenue(venue);
                        SelectionMenu.updateMenus(venue);
                    }
                }
            } else {
                PlayerHandler.add(player, venue, platform);
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "Type the new name for platform " + (index + 1) + ". Type \"cancel\" to cancel.");
            }
        } else switch (tag) {
            case "ACTIVE" -> {
                venue.setActive(!venue.isActive());
                updateWithVenue(venue);
            }
            case "CENTRE" -> {
                Block block = player.getLocation().getBlock();
                venue.setCenter(Coords.fromBlock(block));
                player.sendMessage(ChatColor.BLUE + "Set the centre of the venue to your current location.");
                updateWithVenue(venue);
            }
            case "COUNTDOWN_DURATION_DECREASE" -> {
                int val = venue.getCountdownDuration();

                if (val > 0) {
                    venue.setCountdownDuration(val - 1);
                    updateWithVenue(venue);
                }
            }
            case "COUNTDOWN_DURATION_INCREASE" -> {
                venue.setCountdownDuration(venue.getCountdownDuration() + 1);
                updateWithVenue(venue);
            }
            case "ELIMINATION_DURATION_DECREASE" -> {
                int val = venue.getEliminationDuration();

                if (val > 0) {
                    venue.setEliminationDuration(val - 1);
                    updateWithVenue(venue);
                }
            }
            case "ELIMINATION_DURATION_INCREASE" -> {
                venue.setEliminationDuration(venue.getEliminationDuration() + 1);
                updateWithVenue(venue);
            }
            case "PLATFORM_SIZE_DECREASE" -> {
                int val = venue.getSize();

                if (val > 1) {
                    venue.setSize(val - 1);
                    updateWithVenue(venue);
                }
            }
            case "PLATFORM_SIZE_INCREASE" -> {
                venue.setSize(venue.getSize() + 1);
                updateWithVenue(venue);
            }
            case "PLATFORM_SPACE_DECREASE" -> {
                int val = venue.getSpace();

                if (val > 0) {
                    venue.setSpace(val - 1);
                    updateWithVenue(venue);
                }
            }
            case "PLATFORM_SPACE_INCREASE" -> {
                venue.setSpace(venue.getSpace() + 1);
                updateWithVenue(venue);
            }
            case "REMOVE" -> VenueHandler.unregisterAndRemoveVenue(venue);
            case "REPLACEMENT_DURATION_DECREASE" -> {
                int val = venue.getReplacementDuration();

                if (val > 0) {
                    venue.setReplacementDuration(val - 1);
                    updateWithVenue(venue);
                }
            }
            case "REPLACEMENT_DURATION_INCREASE" -> {
                venue.setReplacementDuration(venue.getReplacementDuration() + 1);
                updateWithVenue(venue);
            }
            case "SELECTION_MENU" -> new SelectionMenu(cableCorners, player, venue).open();
            case "WORLD" -> {
                venue.setWorld(player.getWorld());
                player.sendMessage(ChatColor.BLUE + "Set the world of the venue to your current world.");
                updateWithVenue(venue);
            }
        }
    }

    private record IncrementType(@NotNull String tag, @NotNull String name, @NotNull List<String> lore) {

    }
}
