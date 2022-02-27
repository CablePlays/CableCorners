package me.cable.corners.handler;

import me.cable.corners.CableCorners;
import me.cable.corners.component.Message;
import me.cable.corners.helper.YamlHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public final class Settings {

    private final CableCorners cableCorners;

    private final File file;
    private FileConfiguration fileConfiguration;

    public Settings(@NotNull CableCorners cableCorners) {
        this.cableCorners = cableCorners;
        file = new File(cableCorners.getDataFolder(), "config.yml");
        load();
    }

    public void load() {
        YamlHelper yamlHelper = new YamlHelper(cableCorners);
        fileConfiguration = yamlHelper.load(file, true);
    }

    private @NotNull FileConfiguration config() {
        if (fileConfiguration == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " has not been loaded yet");
        }

        return fileConfiguration;
    }

    public int integer(@NotNull String path) {
        return config().getInt(path);
    }

    public @NotNull List<String> stringList(@NotNull String path) {
        return config().getStringList(path);
    }

    public @NotNull Message message(@NotNull String path) {
        return new Message(stringList(path));
    }
}
