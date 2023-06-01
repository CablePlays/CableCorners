package me.cable.corners.component.region;

import me.cable.corners.component.Coords;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Region {

    protected @NotNull Coords min = new Coords();
    protected @NotNull Coords max = new Coords();
    protected @NotNull String world;

    public Region(@NotNull Coords c1, @NotNull Coords c2, @NotNull String world) {
        setCoords(c1, c2);
        this.world = world;
    }

    public Region(@NotNull String world) {
        this.world = world;
    }

    public boolean contains(@NotNull Location location, int expand) {
        World world = getWorld();
        if (world == null) return false;

        double x = location.getX(), y = location.getY(), z = location.getZ();

        return (world.equals(location.getWorld())
                && x >= min.getX() - expand && x < max.getX() + 1 + expand
                && y >= min.getY() && y < max.getY() + 1
                && z >= min.getZ() - expand && z < max.getZ() + 1 + expand
        );
    }

    public boolean contains(@NotNull Player player, int expand) {
        return contains(player.getLocation(), expand);
    }

    public void setCoords(@NotNull Coords c1, @NotNull Coords c2) {
        min = new Coords(Math.min(c1.getX(), c2.getX()), Math.min(c1.getY(), c2.getY()), Math.min(c1.getZ(), c2.getZ()));
        max = new Coords(Math.max(c1.getX(), c2.getX()), Math.max(c1.getY(), c2.getY()), Math.max(c1.getZ(), c2.getZ()));
    }

    public @NotNull String getWorldName() {
        return world;
    }

    public void setWorldName(@NotNull String world) {
        this.world = world;
    }

    public @Nullable World getWorld() {
        return Bukkit.getWorld(world);
    }

    public void setWorld(@NotNull World world) {
        setWorldName(world.getName());
    }
}
