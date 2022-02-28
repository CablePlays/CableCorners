package me.cable.corners.component.region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Region {

    protected Coords coords1;
    protected Coords coords2;
    protected String world;

    private @Nullable Material placedMaterial;

    public Region(@NotNull Coords coords1, @NotNull Coords coords2, @NotNull String world) {
        this.coords1 = coords1;
        this.coords2 = coords2;
        this.world = world;
    }

    public Region(@NotNull String world) {
        this(new Coords(), new Coords(), world);
    }

    public boolean isPlaced() {
        return (placedMaterial != null);
    }

    public boolean hasValidWorld() {
        return (getActualWorld() != null);
    }

    public boolean contains(@NotNull Location location) {
        World world = getActualWorld();
        if (world == null) return false;

        double xl = location.getX();
        double yl = location.getY();
        double zl = location.getZ();

        return (xl >= Math.min(coords1.getX(), coords2.getX()) && xl < Math.max(coords1.getX(), coords2.getX()) + 1
                && yl >= Math.min(coords1.getY(), coords2.getY()) && yl < Math.max(coords1.getY(), coords2.getY()) + 1
                && zl >= Math.min(coords1.getZ(), coords2.getZ()) && zl < Math.max(coords1.getZ(), coords2.getZ()) + 1
                && world.equals(location.getWorld())
        );
    }

    public boolean contains(@NotNull Player player) {
        return contains(player.getLocation());
    }

    public @NotNull List<Player> getPlayers() {
        List<Player> list = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (contains(player)) {
                list.add(player);
            }
        }

        return list;
    }

    private void fillInternal(@NotNull Material material) {
        World world = getActualWorld();
        if (world == null) return;

        int xs = Math.min(coords1.getX(), coords2.getX());
        int ys = Math.min(coords1.getY(), coords2.getY());
        int zs = Math.min(coords1.getZ(), coords2.getZ());

        int xl = Math.max(coords1.getX(), coords2.getX());
        int yl = Math.max(coords1.getY(), coords2.getY());
        int zl = Math.max(coords1.getZ(), coords2.getZ());

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
        fillInternal(material);
    }

    public void remove() {
        placedMaterial = null;
        fillInternal(Material.AIR);
    }

    public void setCoords(@Nullable Coords coords1, @Nullable Coords coords2) {
        if (placedMaterial == null) {
            if (coords1 != null) {
                this.coords1 = coords1;
            }
            if (coords2 != null) {
                this.coords2 = coords2;
            }
        } else {
            fillInternal(Material.AIR);

            if (coords1 != null) {
                this.coords1 = coords1;
            }
            if (coords2 != null) {
                this.coords2 = coords2;
            }

            fillInternal(placedMaterial);
        }
    }

    public @NotNull String getWorld() {
        return world;
    }

    public @Nullable World getActualWorld() {
        return Bukkit.getWorld(world);
    }

    public void setWorld(@NotNull String world, boolean update) {
        if (placedMaterial == null || !update) {
            this.world = world;
        } else {
            fillInternal(Material.AIR);
            this.world = world;
            fillInternal(placedMaterial);
        }
    }
}
