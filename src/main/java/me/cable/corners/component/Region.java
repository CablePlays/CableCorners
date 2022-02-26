package me.cable.corners.component;

import jdk.vm.ci.code.Location;
import org.graalvm.compiler.graph.Node;
import org.jetbrains.annotations.NotNull;

public class Region {

    private final Location loc1;
    private final Location loc2;

    public Region(@NotNull Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }
}
