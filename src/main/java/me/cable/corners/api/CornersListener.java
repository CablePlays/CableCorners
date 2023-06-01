package me.cable.corners.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public abstract class CornersListener {

    /**
     * Registers this listener for the plugin to use.
     */
    public final void register() {
        CornersAPI.registerListener(this);
    }

    /**
     * Called when a platform is destroyed.
     *
     * @param venueId    the ID of the venue the platform is a part of
     * @param platformId the ID of the platform that was destroyed
     */
    public void onPlatformDestroy(int venueId, int platformId) {}

    /**
     * Called when the destroyed platform is replaced.
     *
     * @param venueId          the ID of the venue the platform is a part of
     * @param survivingPlayers the players who survived the round
     */
    public void onPlatformReplace(int venueId, @NotNull List<Player> survivingPlayers) {}
}
