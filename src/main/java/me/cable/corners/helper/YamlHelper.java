package me.cable.corners.helper;

import me.cable.corners.CableCorners;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class YamlHelper {

    private final CableCorners cableCorners;

    public YamlHelper(@NotNull CableCorners cableCorners) {
        this.cableCorners = cableCorners;
    }

    public @NotNull YamlConfiguration load(@NotNull File file, boolean resource) {
        File dataFolder = cableCorners.getDataFolder();

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        if (!file.exists()) {
            if (resource) {
                cableCorners.saveResource(file.getName(), false);
            } else try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return YamlConfiguration.loadConfiguration(file);
    }
}
