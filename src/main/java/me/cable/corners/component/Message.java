package me.cable.corners.component;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Message {

    private final List<String> strings;
    private final Map<String, String> placeholders = new HashMap<>();

    public Message(@NotNull List<String> strings) {
        this.strings = strings;
    }

    public @NotNull Message placeholder(@Nullable String what, @Nullable String with) {
        if (what != null && with != null) {
            placeholders.put(what, with);
        }

        return this;
    }

    public void send(@NotNull CommandSender commandSender) {
        for (String string : strings) {
            for (Entry<String, String> entry : placeholders.entrySet()) {
                string = string.replace(entry.getKey(), entry.getValue());
            }

            string = ChatColor.translateAlternateColorCodes('&', string);
            commandSender.sendMessage(string);
        }
    }
}
