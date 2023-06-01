package me.cable.corners.api;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class CornersAPI {

    private static final @NotNull List<CornersListener> listeners = new ArrayList<>();

    public static void registerListener(@NotNull CornersListener cornersListener) {
        listeners.add(cornersListener);
    }

    public static @NotNull List<CornersListener> getListeners() {
        return List.copyOf(listeners);
    }
}
