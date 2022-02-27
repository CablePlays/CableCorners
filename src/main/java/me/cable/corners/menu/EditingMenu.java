package me.cable.corners.menu;

import me.cable.corners.CableCorners;
import me.cable.corners.component.region.Platform;
import me.cable.corners.component.region.Venue;
import me.cable.corners.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EditingMenu extends AbstractMenu {

    private final Venue venue;

    private final int[] materialSlots = {27, 28, 36, 37};

    protected EditingMenu(@NotNull Player player, @NotNull Venue venue, @NotNull CableCorners cableCorners) {
        super(player, cableCorners);
        this.venue = venue;
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
    protected boolean preventClick() {
        return true;
    }

    @Override
    protected void apply(@NotNull Inventory inventory) {
        update(inventory);
    }

    private void update(@NotNull Inventory inventory) {
        {
            ItemStack back = ItemUtils.item(Material.ARROW, ChatColor.WHITE + "" + ChatColor.BOLD + "Back",
                    List.of(ChatColor.GRAY + "Click to go back to the selection menu."));
            setTag(back, "BACK");
            inventory.setItem(0, back);
        }
        {
            ItemStack status = ItemUtils.item(Material.COAL_ORE, ChatColor.GOLD + "" + ChatColor.BOLD + "Status",
                    List.of(ChatColor.GRAY + "If active, a venue will become playable."));
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

            ItemStack item = ItemUtils.item(material, name, List.of(
                    ChatColor.WHITE + "Click" + ChatColor.GRAY + " with an item on your cursor to change this platform's material.",
                    ChatColor.WHITE + "Right-click" + ChatColor.GRAY + " to change this platform's name."
            ));

            setTag(item, "PLATFORM_" + i);
            inventory.setItem(materialSlots[i], item);
        }

        {
            ItemStack centre = ItemUtils.item(Material.CHAIN_COMMAND_BLOCK, ChatColor.AQUA + "" + ChatColor.BOLD + "Venue Centre",
                    List.of(ChatColor.GRAY + "Click to set the centre of this venue."));
            setTag(centre, "CENTRE");
            inventory.setItem(30, centre);
        }
        {
            ItemStack world = ItemUtils.item(Material.REPEATING_COMMAND_BLOCK, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Venue World",
                    List.of(ChatColor.GRAY + "Click to set the world this venue is in."));
            setTag(world, "WORLD");
            inventory.setItem(31, world);
        }
        {
            ItemStack worldValidity = ItemUtils.item(Material.BEDROCK, ChatColor.GOLD + "" + ChatColor.BOLD + "World Validity", List.of(
                    ChatColor.GRAY + "Here you can see if the world of this venue is valid.",
                    ChatColor.GRAY + "If a world is invalid then the venue will not be spawned anywhere."
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
                            List.of(ChatColor.GRAY + "The size each of the platforms will be.")
                    ),
                    new IncrementGroup(
                            "PLATFORM_SPACE",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Platform Space (" + venue.getSpace() + ")",
                            List.of(ChatColor.GRAY + "The space between each of the platforms vertically and horizontally.")
                    ),
                    new IncrementGroup(
                            "COUNTDOWN_DURATION",
                            ChatColor.AQUA + "" + ChatColor.BOLD + "Countdown Duration (" + venue.getCountdownDuration() + ")",
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

    private record IncrementGroup(@NotNull String tag, @NotNull String name, @NotNull List<String> lore) {

    }
}
