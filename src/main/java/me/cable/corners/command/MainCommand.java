package me.cable.corners.command;

import me.cable.corners.CableCorners;
import me.cable.corners.component.Message;
import me.cable.corners.component.region.Coords;
import me.cable.corners.component.region.Venue;
import me.cable.corners.handler.Messages;
import me.cable.corners.handler.SaveHandler;
import me.cable.corners.handler.Settings;
import me.cable.corners.manager.VenueManager;
import me.cable.corners.menu.AbstractMenu;
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
    private final Settings settings;
    private final Messages messages;
    private final SaveHandler saveHandler;

    public MainCommand(@NotNull CableCorners cableCorners) {
        this.cableCorners = cableCorners;

        settings = cableCorners.getSettings();
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
            case "create" -> {
                if (!(sender instanceof Player player)) {
                    messages.message("error.only-player").send(sender);
                    return true;
                }

                Block centre = player.getLocation().getBlock();
                Venue venue = new Venue(VenueManager.getNextFreeId(), 4, 1, Coords.fromBlock(centre), centre.getWorld().getName(), null);
                VenueManager.registerVenue(venue);

                venue.setActive(true); // TODO remove

                message("create").send(sender);
            }
            case "help" -> message("help").placeholder("{command}", label).send(sender);
            case "menu" -> {
                if (sender instanceof Player player) {
                    List<Venue> list = VenueManager.getVenues();

                    if (list.isEmpty()) {
                        message("menu.no-venues").send(sender);
                    } else {
                        new SelectionMenu(player, list.get(0), cableCorners).open();
                        message("menu.open").send(sender);
                    }
                } else {
                    messages.message("error.only-player").send(sender);
                }
            }
            case "reload" -> {
                settings.load();
                messages.load();

                message("reload").send(sender);
            }
            case "venues" -> {
                if (args.length < 2) {
                    message("venues.help").placeholder("{command}", label).send(sender);
                } else if (args[1].equals("load")) {
                    VenueManager.unregisterAndRemoveVenues();
                    saveHandler.loadVenues();
                    AbstractMenu.closeMenus(SelectionMenu.class);
                    message("venues.load").send(sender);
                } else if (args[1].equals("save")) {
                    saveHandler.saveVenues();
                    message("venues.save").send(sender);
                } else {
                    message("venues.help").placeholder("{command}", label).send(sender);
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
            for (String a : List.of("create", "help", "menu", "reload", "venues")) {
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
