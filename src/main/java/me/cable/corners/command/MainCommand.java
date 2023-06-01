package me.cable.corners.command;

import me.cable.corners.CableCorners;
import me.cable.corners.component.Coords;
import me.cable.corners.component.Message;
import me.cable.corners.component.region.Venue;
import me.cable.corners.handler.Messages;
import me.cable.corners.handler.SavesHandler;
import me.cable.corners.handler.VenueHandler;
import me.cable.corners.menu.EditingMenu;
import me.cable.corners.menu.Menu;
import me.cable.corners.menu.SelectionMenu;
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
    private final SavesHandler savesHandler;

    public MainCommand(@NotNull CableCorners cableCorners) {
        this.cableCorners = cableCorners;

        messages = cableCorners.getMessages();
        savesHandler = cableCorners.getSavesHandler();
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
            case "create" -> {
                if (!(sender instanceof Player player)) {
                    messages.message("error.only-player").send(sender);
                    return true;
                }

                Block centre = player.getLocation().getBlock();
                Venue venue = new Venue(VenueHandler.getNextFreeId(), 3, 2, Coords.fromBlock(centre), centre.getWorld().getName(), null);
                VenueHandler.registerVenue(venue);

                message("create").send(sender);
            }
            case "edit" -> {
                if (!(sender instanceof Player player)) {
                    messages.message("error.only-player").send(sender);
                    return true;
                }

                for (Venue venue : VenueHandler.getVenues()) {
                    if (venue.contains(player, 5)) {
                        new EditingMenu(cableCorners, player, venue).open();
                        return true;
                    }
                }

                message("edit").send(sender);
            }
            case "help" -> message("help").placeholder("{command}", label).send(sender);
            case "load" -> {
                Menu.closeMenus(EditingMenu.class);
                Menu.closeMenus(SelectionMenu.class);

                VenueHandler.unregisterAndRemoveVenues();
                savesHandler.loadVenues();

                message("load").send(sender);
            }
            case "reload" -> {
                long millis = System.currentTimeMillis();
                messages.load();
                message("reload")
                        .placeholder("{milliseconds}", Long.toString(System.currentTimeMillis() - millis))
                        .send(sender);
            }
            case "save" -> {
                savesHandler.saveVenues();
                message("save").send(sender);
            }
            case "venues" -> {
                if (sender instanceof Player player) {
                    List<Venue> list = VenueHandler.getVenues();

                    if (list.isEmpty()) {
                        message("venues.no-venues").send(sender);
                    } else {
                        new SelectionMenu(cableCorners, player, list.get(0)).open();
                        message("venues.open").send(sender);
                    }
                } else {
                    messages.message("error.only-player").send(sender);
                }
            }
            default -> message("unknown-command").placeholder("{command}", label).send(sender);
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
            for (String a : List.of("create", "edit", "help", "load", "reload", "save", "venues")) {
                if (a.startsWith(args[0])) {
                    result.add(a);
                }
            }
        } else if (length == 2 && args[0].equals("venues")) {
            for (String a : List.of("load", "save")) {
                if (a.startsWith(args[1])) {
                    result.add(a);
                }
            }
        }

        return result;
    }
}
