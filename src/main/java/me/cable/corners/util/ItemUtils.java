package me.cable.corners.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ItemUtils {

    public static @NotNull ItemStack create(@NotNull Material material, @Nullable String name, @Nullable List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static @NotNull ItemStack create(@NotNull Material material, @Nullable String name) {
        return create(material, name, null);
    }
}
