package me.cable.corners.handler;

import me.cable.corners.CableCorners;
import me.cable.corners.component.Message;
import me.cable.corners.helper.YamlHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Messages {

    private final CableCorners cableCorners;

    private final File file;
    private FileConfiguration fileConfiguration;

    public Messages(@NotNull CableCorners cableCorners) {
        this.cableCorners = cableCorners;
        file = new File(cableCorners.getDataFolder(), "messages.yml");
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

    private @NotNull Map<String, String> placeholders() {
        ConfigurationSection configurationSection = config().getConfigurationSection("placeholders");
        Map<String, String> map = new HashMap<>();

        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                String val = configurationSection.getString(key);
                key = "{" + key + "}";

                if (val != null) {
                    map.put(key, val);
                }
            }
        }

        return map;
    }

    private @NotNull List<String> stringList(@NotNull String path) {
        return config().getStringList(path);
    }

    public @NotNull Message message(@NotNull String path) {
        Message message = new Message(stringList(path));
        placeholders().forEach(message::placeholder);
        return message;
    }
}
