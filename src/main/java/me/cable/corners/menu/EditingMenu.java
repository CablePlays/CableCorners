package me.cable.corners.menu;

import me.cable.corners.CableCorners;
import me.cable.corners.component.region.Coords;
import me.cable.corners.component.region.Platform;
import me.cable.corners.component.region.Venue;
import me.cable.corners.manager.PlayerManager;
import me.cable.corners.manager.VenueManager;
import me.cable.corners.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EditingMenu extends AbstractMenu {

    private final Venue venue;
    private final int[] materialSlots = {27, 28, 37, 36};

    public EditingMenu(@NotNull Player player, @NotNull Venue venue, @NotNull CableCorners cableCorners) {
        super(player, cableCorners);
        this.venue = venue;
    }

    public static void updateMenus(@NotNull Venue venue) {
        for (AbstractMenu abstractMenu : getOpenMenus()) {
            if (abstractMenu instanceof EditingMenu) {
                EditingMenu editingMenu = (EditingMenu) abstractMenu;

                if (editingMenu.venue.equals(venue)) {
                    Inventory inventory = editingMenu.getOpenInventory();

                    if (inventory != null) {
                        editingMenu.update(inventory);
                    }
                }
            }
        }
    }

    private static void closeOfVenue(@NotNull Venue venue) {
        for (AbstractMenu abstractMenu : getOpenMenus()) {
            if (abstractMenu instanceof EditingMenu) {
                EditingMenu editingMenu = (EditingMenu) abstractMenu;

                if (editingMenu.venue.equals(venue)) {
                    editingMenu.close();
                }
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
    protected boolean fill() {
        return true;
    }

    @Override
    protected void apply(@NotNull Inventory inventory) {
        update(inventory);
    }

    private void update(@NotNull Inventory inventory) {
        {
            ItemStack back = ItemUtils.item(Material.ARROW, ChatColor.WHITE + "" + ChatColor.BOLD + "Back",
                    Utils.listOf(ChatColor.GRAY + "Click to go back to the selection menu."));
            setTag(back, "BACK");
            inventory.setItem(0, back);
        }
        {
<<<<<<< HEAD
            ItemStack status = ItemUtils.item(Material.COAL_ORE, ChatColor.GOLD + "" + ChatColor.BOLD + "Action Status", Utils.listOf(
                    ChatColor.GRAY + "If a venue is active then its game",
                    ChatColor.GRAY + "will cycle and it will be playable."
            ));
=======
            ItemStack status = ItemUtils.item(Material.COAL_ORE, ChatColor.GOLD + "" + ChatColor.BOLD + "Status",
                    List.of(ChatColor.GRAY + "If a venue is active then it is playable."));
>>>>>>> parent of 3d5a745 (Visual updates)
            setTag(status, "ACTIVE");
            inventory.setItem(10, status);
        }
        {
            ItemStack active;

            if (venue.isActive()) {
                active = ItemUtils.item(Material.LIME_DYE, ChatColor.GREEN + "" + ChatColor.BOLD + "Active",
                        Utils.listOf(ChatColor.GRAY + "Click to make this venue inactive."));
            } else {
                active = ItemUtils.item(Material.GRAY_DYE, ChatColor.GRAY + "" + ChatColor.BOLD + "Inactive",
                        Utils.listOf(ChatColor.GRAY + "Click to make this venue active."));
            }

            setTag(active, "ACTIVE");
            inventory.setItem(11, active);
        }
        {
            ItemStack remove = ItemUtils.item(Material.RED_DYE, ChatColor.RED + "" + ChatColor.BOLD + "Remove",
                    Utils.listOf(ChatColor.GRAY + "Click to remove this venue."));
            setTag(remove, "REMOVE");
            inventory.setItem(13, remove);
        }

        for (int i = 0; i < 4; i++) {
            Platform platform = venue.getPlatforms().get(i);
            Material material = platform.getMaterial();
            String name = platform.getName();

<<<<<<< HEAD
            ItemStack item = ItemUtils.item(material, Utils.format(name), Utils.listOf(
                    ChatColor.YELLOW + "Click" + ChatColor.GRAY + " while holding an item to set",
                    ChatColor.GRAY + "this platform's material.",
                    ChatColor.YELLOW + "Right-click" + ChatColor.GRAY + " to rename this platform."
=======
            ItemStack item = ItemUtils.item(material, name, List.of(
                    ChatColor.WHITE + "Click" + ChatColor.GRAY + " with an item on your cursor to change this platform's material.",
                    ChatColor.WHITE + "Right-click" + ChatColor.GRAY + " to change this platform's name."
>>>>>>> parent of 3d5a745 (Visual updates)
            ));

            setTag(item, "PLATFORM_NUMBER_" + i);
            inventory.setItem(materialSlots[i], item);
        }

        {
            Coords coords = venue.getCentre();
            ItemStack centre = ItemUtils.item(Material.CHAIN_COMMAND_BLOCK, ChatColor.AQUA + "" + ChatColor.BOLD + "Venue Centre", Utils.listOf(
                    ChatColor.YELLOW + "X: " + coords.getX() + ", Y: " + coords.getY() + ", Z: " + coords.getZ(),
                    ChatColor.GRAY + "Click to set the centre of this venue."
            ));
            setTag(centre, "CENTRE");
            inventory.setItem(30, centre);
        }
        {
            ItemStack world = ItemUtils.item(Material.REPEATING_COMMAND_BLOCK,
                    ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Venue World",
<<<<<<< HEAD
                    Utils.listOf(
                            ChatColor.YELLOW + "World: \"" + venue.getWorld() + "\"",
=======
                    List.of(
                            ChatColor.YELLOW + "World: " + venue.getWorld(),
>>>>>>> parent of 3d5a745 (Visual updates)
                            ChatColor.GRAY + "Click to set the world this venue is in."
                    ));
            setTag(world, "WORLD");
            inventory.setItem(31, world);
        }
        {
<<<<<<< HEAD
            ItemStack worldValidity = ItemUtils.item(Material.BEDROCK, ChatColor.GOLD + "" + ChatColor.BOLD + "World Validity", Utils.listOf(
                    ChatColor.GRAY + "Indicates if this venue's world is",
                    ChatColor.GRAY + "valid. If the world is invalid then",
                    ChatColor.GRAY + "the venue will not be usable."
=======
            ItemStack worldValidity = ItemUtils.item(Material.BEDROCK, ChatColor.GOLD + "" + ChatColor.BOLD + "World Validity", List.of(
                    ChatColor.GRAY + "Here you can see if the world of this venue is valid.",
                    ChatColor.GRAY + "If a world is invalid then the venue will not be spawned anywhere."
>>>>>>> parent of 3d5a745 (Visual updates)
            ));
            inventory.setItem(39, worldValidity);
        }
        {
            ItemStack worldValidity;

            if (venue.hasValidWorld()) {
                worldValidity = ItemUtils.item(Material.LIME_DYE, ChatColor.GREEN + "" + ChatColor.BOLD + "Valid",
                        Utils.listOf(ChatColor.GRAY + "World is valid and venue will be spawned in."));
            } else {
                worldValidity = ItemUtils.item(Material.GRAY_DYE, ChatColor.GRAY + "" + ChatColor.BOLD + "Invalid",
                        Utils.listOf(ChatColor.GRAY + "World is invalid and venue will not be spawned in."));
            }

            inventory.setItem(40, worldValidity);
        }
        {
            int i = 0;

            for (IncrementGroup incrementGroup : Utils.listOf(
                    new IncrementGroup(
                            "PLATFORM_SIZE",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Platform Size (" + venue.getSize() + ")",
<<<<<<< HEAD
                            Utils.listOf(
                                    ChatColor.GRAY + "Size of each platform.",
                                    ChatColor.GRAY + "e.g. " + ChatColor.YELLOW + venue.getSize() + "x" + venue.getSize()
                            )
=======
                            List.of(ChatColor.GRAY + "The size each of the platforms will be.")
>>>>>>> parent of 3d5a745 (Visual updates)
                    ),
                    new IncrementGroup(
                            "PLATFORM_SPACE",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Platform Space (" + venue.getSpace() + ")",
<<<<<<< HEAD
                            Utils.listOf(ChatColor.GRAY + "Number of blocks between each platform.")
=======
                            List.of(ChatColor.GRAY + "The space between each of the platforms vertically and horizontally.")
>>>>>>> parent of 3d5a745 (Visual updates)
                    ),
                    new IncrementGroup(
                            "COUNTDOWN_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Countdown Duration (" + venue.getCountdownDuration() + ")",
<<<<<<< HEAD
                            Utils.listOf(ChatColor.GRAY + "Length of the countdown in seconds.")
                    ),
                    new IncrementGroup(
                            "ELIMINATION_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Elimination Duration (" + venue.getEliminationDuration() + ")",
                            Utils.listOf(ChatColor.GRAY + "Length of the elimination period in seconds.")
                    ),
                    new IncrementGroup(
                            "REPLACEMENT_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Replacement Duration (" + venue.getReplacementDuration() + ")",
                            Utils.listOf(ChatColor.GRAY + "Length of the replacement period in seconds.")
=======
                            List.of(ChatColor.GRAY + "How long the countdown will last (seconds).")
                    ),
                    new IncrementGroup(
                            "ELIMINATION_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Platform Size (" + venue.getEliminationDuration() + ")",
                            List.of(ChatColor.GRAY + "How long the elimination period will last (seconds).")
                    ),
                    new IncrementGroup(
                            "REPLACEMENT_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Platform Size (" + venue.getReplacementDuration() + ")",
                            List.of(ChatColor.GRAY + "How long the replacement period will last (seconds).")
>>>>>>> parent of 3d5a745 (Visual updates)
                    )
            )) {
                ItemStack increase = ItemUtils.item(Material.LIME_STAINED_GLASS, ChatColor.GREEN + "" + ChatColor.BOLD + "Increase",
                        Utils.listOf(ChatColor.GRAY + "Click to increase."));
                setTag(increase, incrementGroup.tag + "_INCREASE");
                inventory.setItem(i + 6, increase);

                ItemStack decrease = ItemUtils.item(Material.RED_STAINED_GLASS, ChatColor.RED + "" + ChatColor.BOLD + "Decrease",
                        Utils.listOf(ChatColor.GRAY + "Click to decrease."));
                setTag(decrease, incrementGroup.tag + "_DECREASE");
                inventory.setItem(i + 8, decrease);

                ItemStack info = ItemUtils.item(Material.OAK_SIGN, incrementGroup.name, incrementGroup.lore);
                inventory.setItem(i + 7, info);

                i += (i == 9) ? 18 : 9;
            }
        }
    }

    @Override
    protected void onClick(@NotNull InventoryClickEvent e, @NotNull String tag) {
        if (e.getView().getTopInventory().equals(e.getClickedInventory())) {
            e.setCancelled(true);
        }
<<<<<<< HEAD
        if (tag == null) return;
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
=======
>>>>>>> parent of 3d5a745 (Visual updates)

        if (!(e.getWhoClicked() instanceof Player player)) return;

        if (tag.startsWith("PLATFORM_ICON_")) {
            String[] parts = tag.split("_");
            int index = Integer.parseInt(parts[1]);
            Platform platform = venue.getPlatforms().get(index);
            ClickType click = e.getClick();

            if (click == ClickType.LEFT) {
                ItemStack cursorItem = e.getCursor();

                if (cursorItem != null) {
                    Material material = cursorItem.getType();

                    if (!material.isAir()) {
                        platform.setMaterial(material);
                    }

                    player.setItemOnCursor(null);
                    updateMenus(venue);
                    SelectionMenu.updateMenus(venue);
                }
            } else if (click == ClickType.RIGHT) {
                PlayerManager.add(player, venue, platform);
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "Type the new name for platform " + (index + 1) + ". Type \"cancel\" to cancel.");
            }
        } else switch (tag) {
            case "ACTIVE": {
                venue.setActive(!venue.isActive());
                updateMenus(venue);
                break;
            }
            case "BACK": {
                new SelectionMenu(player, venue, cableCorners).open();
                break;
            }
            case "CENTRE": {
                Block block = player.getLocation().getBlock();
                venue.setCentre(Coords.fromBlock(block));
                player.sendMessage(ChatColor.BLUE + "Set the centre of the venue to your current location.");
                updateMenus(venue);
                break;
            }
            case "COUNTDOWN_DURATION_DECREASE": {
                int val = venue.getCountdownDuration();

                if (val > 0) {
                    venue.setCountdownDuration(val - 1);
                    updateMenus(venue);
                }

                break;
            }
            case "COUNTDOWN_DURATION_INCREASE": {
                venue.setCountdownDuration(venue.getCountdownDuration() + 1);
                updateMenus(venue);
                break;
            }
            case "ELIMINATION_DURATION_DECREASE": {
                int val = venue.getEliminationDuration();

                if (val > 0) {
                    venue.setEliminationDuration(val - 1);
                    updateMenus(venue);
                }

                break;
            }
            case "ELIMINATION_DURATION_INCREASE": {
                venue.setEliminationDuration(venue.getEliminationDuration() + 1);
                updateMenus(venue);
                break;
            }
            case "PLATFORM_SIZE_DECREASE": {
                int val = venue.getSize();

                if (val > 1) {
                    venue.setSize(val - 1);
                    updateMenus(venue);
                }
                break;
            }
            case "PLATFORM_SIZE_INCREASE": {
                venue.setSize(venue.getSize() + 1);
                updateMenus(venue);
                break;
            }
            case "PLATFORM_SPACE_DECREASE": {
                int val = venue.getSpace();

                if (val > 0) {
                    venue.setSpace(val - 1);
                    updateMenus(venue);
                }

                break;
            }
            case "PLATFORM_SPACE_INCREASE": {
                venue.setSpace(venue.getSpace() + 1);
                updateMenus(venue);
                break;
            }
            case "REMOVE": {
                VenueManager.unregisterAndRemoveVenue(venue);
                break;
            }
<<<<<<< HEAD
            case "REPLACEMENT_DURATION_DECREASE": {
=======
            case "REMOVE" -> {
                int index = VenueManager.getIndex(venue);
                VenueManager.unregisterAndRemoveVenue(venue);
                SelectionMenu.onVenueRemove(venue, index);
                closeOfVenue(venue);
            }
            case "REPLACEMENT_DURATION_DECREASE" -> {
>>>>>>> parent of 3d5a745 (Visual updates)
                int val = venue.getReplacementDuration();

                if (val > 0) {
                    venue.setReplacementDuration(val - 1);
                    updateMenus(venue);
                }

                break;
            }
            case "REPLACEMENT_DURATION_INCREASE": {
                venue.setReplacementDuration(venue.getReplacementDuration() + 1);
                updateMenus(venue);
                break;
            }
            case "WORLD": {
                World world = player.getWorld();
                venue.setWorld(world.getName());
                player.sendMessage(ChatColor.BLUE + "Set the world of the venue to your current world.");
                updateMenus(venue);
                break;
            }
        }
    }

    private static class IncrementGroup {

        private final String tag;
        private final String name;
        private final List<String> lore;

        private IncrementGroup(@NotNull String tag, @NotNull String name, @NotNull List<String> lore) {
            this.tag = tag;
            this.name = name;
            this.lore = lore;
        }
    }
}
