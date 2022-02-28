package me.cable.corners.component.region;

import me.cable.corners.util.Utils;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Platform extends Region {

    private static final String DEFAULT_NAME = "N/A";

    private Material material;
    private String name = DEFAULT_NAME;

    public Platform(@NotNull String world, @NotNull Material material) {
        super(world);
        this.material = material;
    }

    public void fill() {
        fill(getMaterial());
    }

    public @NotNull Material getMaterial() {
        return material;
    }

    public void setMaterial(@NotNull Material material) {
        this.material = material;

        if (isPlaced()) {
            fill();
        }
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = (name == null) ? DEFAULT_NAME : name;
    }
}
