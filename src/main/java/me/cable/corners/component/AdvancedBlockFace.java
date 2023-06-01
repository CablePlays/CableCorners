package me.cable.corners.component;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public enum AdvancedBlockFace {
    SOUTH_EAST(BlockFace.SOUTH, BlockFace.EAST, false, false),
    SOUTH_WEST(BlockFace.SOUTH, BlockFace.WEST, false, true),
    NORTH_WEST(BlockFace.NORTH, BlockFace.WEST, true, true),
    NORTH_EAST(BlockFace.NORTH, BlockFace.EAST, true, false);

    private final BlockFace blockFace1;
    private final BlockFace blockFace2;

    // if the platform should move extra on even space
    private final boolean evenMove1;
    private final boolean evenMove2;

    AdvancedBlockFace(@NotNull BlockFace blockFace1, @NotNull BlockFace blockFace2, boolean evenMove1, boolean evenMove2) {
        this.blockFace1 = blockFace1;
        this.blockFace2 = blockFace2;
        this.evenMove1 = evenMove1;
        this.evenMove2 = evenMove2;
    }

    public BlockFace blockFace1() {
        return blockFace1;
    }

    public BlockFace blockFace2() {
        return blockFace2;
    }

    public boolean evenMove1() {
        return evenMove1;
    }

    public boolean evenMove2() {
        return evenMove2;
    }
}
