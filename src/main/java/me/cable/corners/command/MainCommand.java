package me.cable.corners.command;

import me.cable.corners.CableCorners;
import me.cable.corners.component.Message;
import me.cable.corners.component.region.Coords;
import me.cable.corners.component.region.Venue;
import me.cable.corners.handler.Messages;
import me.cable.corners.handler.SaveHandler;
import me.cable.corners.manager.VenueManager;
import me.cable.corners.menu.AbstractMenu;
import me.cable.corners.menu.EditingMenu;
import me.cable.corners.menu.SelectionMenu;
import me.cable.corners.util.Utils;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final CableCorners cableCorners;
    private final Messages messages;
    private final SaveHandler saveHandler;

    public MainCommand(@NotNull CableCorners cableCorners) {
        this.cableCorners = cableCorners;

        messages = cableCorners.getMessages();
        saveHandler = cableCorners.getSaveHandler();
    }

    public void register(@NotNull String string) {
        PluginCommand pluginCommand = cableCorners.getCommand(string);

        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int length = args.length;

        if (length == 0) {
            PluginDescriptionFile pluginDescriptionFile = cableCorners.getDescription();

            Message message = message("information");
            message.placeholder("{author}", pluginDescriptionFile.getAuthors().isEmpty() ? "N/A" : pluginDescriptionFile.getAuthors().get(0));
            message.placeholder("{command}", label);
            message.placeholder("{name}", pluginDescriptionFile.getName());
            message.placeholder("{version}", pluginDescriptionFile.getVersion());
            message.send(sender);
            return true;
        }

        switch (args[0]) {
            case "create": {
                if (!(sender instanceof Player)) {
                    messages.message("error.only-player").send(sender);
                    return true;
                }

                Player player = (Player) sender;

                Block centre = player.getLocation().getBlock();
                Venue venue = new Venue(VenueManager.getNextFreeId(), 3, 3, Coords.fromBlock(centre), centre.getWorld().getName(), null);
                VenueManager.registerVenue(venue);

                message("create").send(sender);
                break;
            }
            case "edit": {
                if (!(sender instanceof Player)) {
                    messages.message("error.only-player").send(sender);
                    return true;
                }

                Player player = (Player) sender;

                for (Venue venue : VenueManager.getVenues()) {
                    if (venue.contains(player)) {
                        new EditingMenu(player, venue, false, cableCorners).open();
                        return true;
                    }
                }

                message("edit").send(sender);
                break;
            }
            case "help": {
                message("help").placeholder("{command}", label).send(sender);
                break;
            }
            case "load": {
                AbstractMenu.closeMenus(EditingMenu.class);
                AbstractMenu.closeMenus(SelectionMenu.class);

                VenueManager.unregisterAndRemoveVenues();
                saveHandler.loadVenues();

                message("load").send(sender);
                break;
            }
            case "reload": {
                messages.load();
                message("reload").send(sender);
                break;
            }
            case "save": {
                saveHandler.saveVenues();
                message("save").send(sender);
                break;
            }
            case "venues": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    List<Venue> list = VenueManager.getVenues();

                    if (list.isEmpty()) {
                        message("venues.no-venues").send(sender);
                    } else {
                        new SelectionMenu(player, list.get(0), cableCorners).open();
                        message("venues.open").send(sender);
                    }
                } else {
                    messages.message("error.only-player").send(sender);
                }

                break;
            }
            default: {
                message("unknown-command").placeholder("{command}", label).send(sender);
                break;
            }
        }

        return true;
    }

    private @NotNull Message message(@NotNull String path) {
        return messages.message("command.cablecorners." + path);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> result = new ArrayList<>();
        int length = args.length;

        if (length == 1) {
            for (String a : Utils.listOf("create", "edit", "help", "load", "reload", "save", "venues")) {
                if (a.startsWith(args[0])) {
                    result.add(a);
                }
            }
        } else if (length == 2 && args[0].equals("venues")) {
            for (String a : Utils.listOf("load", "save")) {
                if (a.startsWith(args[1])) {
                    result.add(a);
                }
            }
        }

        return result;
    }
}
