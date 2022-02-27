package me.cable.corners.component;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public enum AdvancedBlockFace {
    SOUTH_EAST(BlockFace.SOUTH, BlockFace.EAST, 1, 1),
    SOUTH_WEST(BlockFace.SOUTH, BlockFace.WEST, 1, 0),
    NORTH_WEST(BlockFace.NORTH, BlockFace.WEST, 0, 0),
    NORTH_EAST(BlockFace.NORTH, BlockFace.EAST, 0, 1);

    private final BlockFace blockFace1;
    private final BlockFace blockFace2;
    private final int subtract1;
    private final int subtract2;

    AdvancedBlockFace(@NotNull BlockFace blockFace1, @NotNull BlockFace blockFace2, int subtract1, int subtract2) {
        this.blockFace1 = blockFace1;
        this.blockFace2 = blockFace2;
        this.subtract1 = subtract1;
        this.subtract2 = subtract2;
    }

    public BlockFace blockFace1() {
        return blockFace1;
    }

    public BlockFace blockFace2() {
        return blockFace2;
    }

    public int subtract1(boolean odd) {
        return (odd ? 0 : subtract1);
    }

    public int subtract2(boolean odd) {
        return (odd ? 0 : subtract2);
    }
}
