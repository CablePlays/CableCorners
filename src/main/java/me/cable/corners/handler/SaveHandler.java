package me.cable.corners.handler;

import me.cable.corners.CableCorners;
import me.cable.corners.component.region.Venue;
import me.cable.corners.manager.VenueManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public final class SaveHandler {

    private final CableCorners cableCorners;
    private final File file;

    public SaveHandler(CableCorners cableCorners) {
        this.cableCorners = cableCorners;

        File parent = cableCorners.getDataFolder();
        file = new File(parent, "venues.yml");

        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                cableCorners.getLogger().warning("Could not create venues file");
                e.printStackTrace();
            }
        }
    }

    public void loadVenues() {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        for (String key : yamlConfiguration.getKeys(false)) {
            ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection(key);

            if (configurationSection != null) {
                Venue venue = Venue.load(configurationSection);
                VenueManager.registerVenue(venue);
            }
        }
    }

    public void saveVenues() {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        for (Venue venue : VenueManager.getVenues()) {
            int id = venue.getId();
            ConfigurationSection cs = yamlConfiguration.createSection(String.valueOf(id));
            venue.save(cs);
        }

        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            cableCorners.getLogger().severe("Could not save venues");
            e.printStackTrace();
        }
    }

    public void reloadVenues() {

    }
}
