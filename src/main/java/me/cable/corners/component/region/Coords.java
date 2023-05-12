package me.cable.corners.component.region;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Coords implements Serializable {

    private final int x;
    private final int y;
    private final int z;

    public Coords(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coords() {
        this(0, 0, 0);
    }

    public static @NotNull Coords fromBlock(@NotNull Block block) {
        return new Coords(block.getX(), block.getY(), block.getZ());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public @NotNull Coords add(int x, int y, int z) {
        return new Coords(this.x + x, this.y + y, this.z + z);
    }

    public @NotNull Coords subtract(int x, int y, int z) {
        return new Coords(this.x - x, this.y - y, this.z - z);
    }

    public @NotNull Coords getRelative(@NotNull BlockFace blockFace, int a) {
        return switch (blockFace) {
            case NORTH -> add(0, 0, -a);
            case SOUTH -> add(0, 0, a);
            case EAST -> add(a, 0, 0);
            case WEST -> add(-a, 0, 0);
            case NORTH_EAST -> add(a, 0, -a);
            case NORTH_WEST -> add(-a, 0, -a);
            case SOUTH_EAST -> add(a, 0, a);
            case SOUTH_WEST -> add(-a, 0, a);
            default -> this;
        };
    }
}
