package me.cable.corners.menu;

import me.cable.corners.CableCorners;
import me.cable.corners.component.region.Coords;
import me.cable.corners.component.region.Platform;
import me.cable.corners.component.region.Venue;
import me.cable.corners.manager.PlayerManager;
import me.cable.corners.manager.VenueManager;
import me.cable.corners.util.ItemUtils;
import me.cable.corners.util.Utils;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EditingMenu extends AbstractMenu {

    private final Venue venue;
    private final int[] materialSlots = {27, 28, 37, 36};

    private final boolean back;

    public EditingMenu(@NotNull Player player, @NotNull Venue venue, boolean back, @NotNull CableCorners cableCorners) {
        super(player, cableCorners);
        this.venue = venue;
        this.back = back;
    }

    public static void updateMenus(@NotNull Venue venue) {
        for (AbstractMenu abstractMenu : getOpenMenus()) {
            if (abstractMenu instanceof EditingMenu editingMenu && editingMenu.venue.equals(venue)) {
                Inventory inventory = editingMenu.getOpenInventory();

                if (inventory != null) {
                    editingMenu.update(inventory);
                }
            }
        }
    }

    public static void closeOfVenue(@NotNull Venue venue) {
        for (AbstractMenu abstractMenu : getOpenMenus()) {
            if (abstractMenu instanceof EditingMenu editingMenu) {
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
        if (back) {
            ItemStack back = ItemUtils.item(Material.ARROW, ChatColor.WHITE + "" + ChatColor.BOLD + "Back",
                    List.of(ChatColor.GRAY + "Click to go back to the selection menu."));
            setTag(back, "BACK");
            inventory.setItem(0, back);
        }
        {
            ItemStack status = ItemUtils.item(Material.COAL_ORE, ChatColor.GOLD + "" + ChatColor.BOLD + "Action Status", List.of(
                    ChatColor.GRAY + "If a venue is active then its game",
                    ChatColor.GRAY + "will cycle and it will be playable."
            ));
            setTag(status, "ACTIVE");
            inventory.setItem(10, status);
        }
        {
            ItemStack active;

            if (venue.isActive()) {
                active = ItemUtils.item(Material.LIME_DYE, ChatColor.GREEN + "" + ChatColor.BOLD + "Active",
                        List.of(ChatColor.GRAY + "Click to make this venue inactive."));
            } else {
                active = ItemUtils.item(Material.GRAY_DYE, ChatColor.GRAY + "" + ChatColor.BOLD + "Inactive",
                        List.of(ChatColor.GRAY + "Click to make this venue active."));
            }

            setTag(active, "ACTIVE");
            inventory.setItem(11, active);
        }
        {
            ItemStack remove = ItemUtils.item(Material.RED_DYE, ChatColor.RED + "" + ChatColor.BOLD + "Remove",
                    List.of(ChatColor.GRAY + "Click to remove this venue."));
            setTag(remove, "REMOVE");
            inventory.setItem(13, remove);
        }

        for (int i = 0; i < 4; i++) {
            Platform platform = venue.getPlatforms().get(i);
            Material material = platform.getMaterial();
            String name = platform.getName();

            ItemStack item = ItemUtils.item(material, Utils.format(name), List.of(
                    ChatColor.YELLOW + "Click" + ChatColor.GRAY + " while holding an item to set",
                    ChatColor.GRAY + "this platform's material.",
                    ChatColor.YELLOW + "Right-click" + ChatColor.GRAY + " to rename this platform."
            ));

            setTag(item, "PLATFORM_ICON_" + i);
            inventory.setItem(materialSlots[i], item);
        }

        {
            Coords coords = venue.getCentre();
            ItemStack centre = ItemUtils.item(Material.CHAIN_COMMAND_BLOCK, ChatColor.AQUA + "" + ChatColor.BOLD + "Venue Centre", List.of(
                    ChatColor.YELLOW + "X: " + coords.getX() + ", Y: " + coords.getY() + ", Z: " + coords.getZ(),
                    ChatColor.GRAY + "Click to set the centre of this venue."
            ));
            setTag(centre, "CENTRE");
            inventory.setItem(30, centre);
        }
        {
            ItemStack world = ItemUtils.item(Material.REPEATING_COMMAND_BLOCK,
                    ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Venue World",
                    List.of(
                            ChatColor.YELLOW + "World: \"" + venue.getWorld() + "\"",
                            ChatColor.GRAY + "Click to set the world this venue is in."
                    ));
            setTag(world, "WORLD");
            inventory.setItem(31, world);
        }
        {
            ItemStack worldValidity = ItemUtils.item(Material.BEDROCK, ChatColor.GOLD + "" + ChatColor.BOLD + "World Validity", List.of(
                    ChatColor.GRAY + "Indicates if this venue's world is",
                    ChatColor.GRAY + "valid. If the world is invalid then",
                    ChatColor.GRAY + "the venue will not be usable."
            ));
            inventory.setItem(39, worldValidity);
        }
        {
            ItemStack worldValidity;

            if (venue.hasValidWorld()) {
                worldValidity = ItemUtils.item(Material.LIME_DYE, ChatColor.GREEN + "" + ChatColor.BOLD + "Valid",
                        List.of(ChatColor.GRAY + "World is valid and venue will be spawned in."));
            } else {
                worldValidity = ItemUtils.item(Material.GRAY_DYE, ChatColor.GRAY + "" + ChatColor.BOLD + "Invalid",
                        List.of(ChatColor.GRAY + "World is invalid and venue will not be spawned in."));
            }

            inventory.setItem(40, worldValidity);
        }
        {
            int i = 0;

            for (IncrementGroup incrementGroup : List.of(
                    new IncrementGroup(
                            "PLATFORM_SIZE",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Platform Size (" + venue.getSize() + ")",
                            List.of(
                                    ChatColor.GRAY + "Size of each platform.",
                                    ChatColor.GRAY + "e.g. " + ChatColor.YELLOW + venue.getSize() + "x" + venue.getSize()
                            )
                    ),
                    new IncrementGroup(
                            "PLATFORM_SPACE",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Platform Space (" + venue.getSpace() + ")",
                            List.of(ChatColor.GRAY + "Number of blocks between each platform.")
                    ),
                    new IncrementGroup(
                            "COUNTDOWN_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Countdown Duration (" + venue.getCountdownDuration() + ")",
                            List.of(ChatColor.GRAY + "Length of the countdown in seconds.")
                    ),
                    new IncrementGroup(
                            "ELIMINATION_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Elimination Duration (" + venue.getEliminationDuration() + ")",
                            List.of(ChatColor.GRAY + "Length of the elimination period in seconds.")
                    ),
                    new IncrementGroup(
                            "REPLACEMENT_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Replacement Duration (" + venue.getReplacementDuration() + ")",
                            List.of(ChatColor.GRAY + "Length of the replacement period in seconds.")
                    )
            )) {
                ItemStack increase = ItemUtils.item(Material.LIME_STAINED_GLASS, ChatColor.GREEN + "" + ChatColor.BOLD + "Increase",
                        List.of(ChatColor.GRAY + "Click to increase."));
                setTag(increase, incrementGroup.tag() + "_INCREASE");
                inventory.setItem(i + 6, increase);

                ItemStack decrease = ItemUtils.item(Material.RED_STAINED_GLASS, ChatColor.RED + "" + ChatColor.BOLD + "Decrease",
                        List.of(ChatColor.GRAY + "Click to decrease."));
                setTag(decrease, incrementGroup.tag() + "_DECREASE");
                inventory.setItem(i + 8, decrease);

                ItemStack info = ItemUtils.item(Material.OAK_SIGN, incrementGroup.name(), incrementGroup.lore());
                inventory.setItem(i + 7, info);

                i += (i == 9) ? 18 : 9;
            }
        }
    }

    @Override
    protected void onClick(@NotNull InventoryClickEvent e, @Nullable String tag) {
        if (e.getView().getTopInventory().equals(e.getClickedInventory())) {
            e.setCancelled(true);
        }
        if (tag == null) return;
        if (!(e.getWhoClicked() instanceof Player player)) return;

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
                    }

                    player.setItemOnCursor(null);
                    updateMenus(venue);
                    SelectionMenu.updateMenus(venue);
                }
            } else {
                PlayerManager.add(player, venue, platform);
                player.closeInventory();
                player.sendMessage(ChatColor.BLUE + "Type the new name for platform " + (index + 1) + ". Type \"cancel\" to cancel.");
            }
        } else switch (tag) {
            case "ACTIVE" -> {
                venue.setActive(!venue.isActive());
                updateMenus(venue);
            }
            case "BACK" -> new SelectionMenu(player, venue, cableCorners).open();
            case "CENTRE" -> {
                Block block = player.getLocation().getBlock();
                venue.setCentre(Coords.fromBlock(block));
                player.sendMessage(ChatColor.BLUE + "Set the centre of the venue to your current location.");
                updateMenus(venue);
            }
            case "COUNTDOWN_DURATION_DECREASE" -> {
                int val = venue.getCountdownDuration();

                if (val > 0) {
                    venue.setCountdownDuration(val - 1);
                    updateMenus(venue);
                }
            }
            case "COUNTDOWN_DURATION_INCREASE" -> {
                venue.setCountdownDuration(venue.getCountdownDuration() + 1);
                updateMenus(venue);
            }
            case "ELIMINATION_DURATION_DECREASE" -> {
                int val = venue.getEliminationDuration();

                if (val > 0) {
                    venue.setEliminationDuration(val - 1);
                    updateMenus(venue);
                }
            }
            case "ELIMINATION_DURATION_INCREASE" -> {
                venue.setEliminationDuration(venue.getEliminationDuration() + 1);
                updateMenus(venue);
            }
            case "PLATFORM_SIZE_DECREASE" -> {
                int val = venue.getSize();

                if (val > 1) {
                    venue.setSize(val - 1);
                    updateMenus(venue);
                }
            }
            case "PLATFORM_SIZE_INCREASE" -> {
                venue.setSize(venue.getSize() + 1);
                updateMenus(venue);
            }
            case "PLATFORM_SPACE_DECREASE" -> {
                int val = venue.getSpace();

                if (val > 0) {
                    venue.setSpace(val - 1);
                    updateMenus(venue);
                }
            }
            case "PLATFORM_SPACE_INCREASE" -> {
                venue.setSpace(venue.getSpace() + 1);
                updateMenus(venue);
            }
            case "REMOVE" -> VenueManager.unregisterAndRemoveVenue(venue);
            case "REPLACEMENT_DURATION_DECREASE" -> {
                int val = venue.getReplacementDuration();

                if (val > 0) {
                    venue.setReplacementDuration(val - 1);
                    updateMenus(venue);
                }
            }
            case "REPLACEMENT_DURATION_INCREASE" -> {
                venue.setReplacementDuration(venue.getReplacementDuration() + 1);
                updateMenus(venue);
            }
            case "WORLD" -> {
                World world = player.getWorld();
                venue.setWorld(world.getName());
                player.sendMessage(ChatColor.BLUE + "Set the world of the venue to your current world.");
                updateMenus(venue);
            }
        }
    }

    private record IncrementGroup(@NotNull String tag, @NotNull String name, @NotNull List<String> lore) {

    }
}
