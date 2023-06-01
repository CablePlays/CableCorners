package me.cable.corners.component.region;

import me.cable.corners.component.Coords;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceableRegion extends Region {

    private @Nullable Material placedMaterial;

    public PlaceableRegion(@NotNull Coords c1, @NotNull Coords c2, @NotNull String world) {
        super(c1, c2, world);
    }

    public PlaceableRegion(@NotNull String world) {
        super(world);
    }

    @Override
    public void setCoords(@NotNull Coords c1, @NotNull Coords c2) {
        if (placedMaterial == null) {
            super.setCoords(c1, c2);
        } else {
            fillRaw(Material.AIR);
            super.setCoords(c1, c2);
            fillRaw(placedMaterial);
        }
    }

    @Override
    public void setWorldName(@NotNull String world) {
        if (placedMaterial == null) {
            super.setWorldName(world);
        } else {
            fillRaw(Material.AIR);
            super.setWorldName(world);
            fillRaw(placedMaterial);
        }
    }

    private void fillRaw(@NotNull Material material) {
        World world = getWorld();
        if (world == null) return;

        int xs = min.getX(), ys = min.getY(), zs = min.getZ();
        int xl = max.getX(), yl = max.getY(), zl = max.getZ();

        for (int x = xs; x <= xl; x++) {
            for (int y = ys; y <= yl; y++) {
                for (int z = zs; z <= zl; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(material);
                }
            }
        }
    }

    public void fill(@NotNull Material material) {
        placedMaterial = material;
        fillRaw(material);
    }

    public void remove() {
        if (placedMaterial != null) {
            placedMaterial = null;
            fillRaw(Material.AIR);
        }
    }

    public boolean isPlaced() {
        return (placedMaterial != null);
    }
}
