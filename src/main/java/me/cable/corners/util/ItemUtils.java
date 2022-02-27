package me.cable.corners.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ItemUtils {

    public static @NotNull ItemStack item(@NotNull Material material, @Nullable String name, @Nullable List<String> lore) {
        ItemStack itemStack = new ItemStack(material);

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public static @NotNull ItemStack item(@NotNull Material material, @NotNull String name) {
        return item(material, name, null);
    }

    public static @NotNull ItemStack item(@NotNull Material material) {
        return item(material, null, null);
    }

    public static @NotNull ItemStack emptyItem(@NotNull Material material) {
        return item(material, " ");
    }

    public static @NotNull ItemStack fillItem() {
        return emptyItem(Material.BLACK_STAINED_GLASS_PANE);
    }
}
