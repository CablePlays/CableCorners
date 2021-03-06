package me.cable.corners;

import me.cable.corners.command.MainCommand;
import me.cable.corners.component.region.Venue;
import me.cable.corners.handler.Messages;
import me.cable.corners.handler.SaveHandler;
import me.cable.corners.handler.Settings;
import me.cable.corners.listener.inventory.InventoryClick;
import me.cable.corners.listener.inventory.InventoryClose;
import me.cable.corners.listener.player.AsyncPlayerChat;
import me.cable.corners.listener.player.PlayerQuit;
import me.cable.corners.manager.VenueManager;
import me.cable.corners.thread.GameThread;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CableCorners extends JavaPlugin {

<<<<<<< HEAD
<<<<<<< HEAD
    // TODO make help message nice
    // TODO make version compatible
    // TODO add more default platform layouts
    // TODO add custom jump height and speed when in a venue
    // TODO add TNT spawning

=======
    private Settings settings;
>>>>>>> parent of 3d5a745 (Visual updates)
=======
    private Settings settings;
>>>>>>> parent of 3d5a745 (Visual updates)
    private Messages messages;

    private SaveHandler saveHandler;

    @Override
    public void onEnable() {
        initializeHandles();
        registerListeners();
        registerCommands();
        startThreads();

        saveHandler.loadVenues();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        VenueManager.getVenues().forEach(Venue::removePlatforms);
        saveHandler.saveVenues();
    }

    private void initializeHandles() {
        settings = new Settings(this);
        messages = new Messages(this);
        saveHandler = new SaveHandler(this);
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Inventory

        pluginManager.registerEvents(new InventoryClick(), this);
        pluginManager.registerEvents(new InventoryClose(), this);

        // Player

        pluginManager.registerEvents(new AsyncPlayerChat(), this);
        pluginManager.registerEvents(new PlayerQuit(), this);
    }

    private void registerCommands() {
        new MainCommand(this).register("cablecorners");
    }

    private void startThreads() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new GameThread(this), 0, 20);
    }

    public Settings getSettings() {
        return settings;
    }

    public Messages getMessages() {
        return messages;
    }

    public SaveHandler getSaveHandler() {
        return saveHandler;
    }
}
