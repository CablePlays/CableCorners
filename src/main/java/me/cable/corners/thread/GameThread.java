package me.cable.corners.thread;

import me.cable.corners.CableCorners;
import me.cable.corners.api.CornersAPI;
import me.cable.corners.component.GameState;
import me.cable.corners.component.Message;
import me.cable.corners.component.region.Platform;
import me.cable.corners.component.region.Venue;
import me.cable.corners.handler.Messages;
import me.cable.corners.handler.VenueHandler;
import me.cable.corners.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameThread implements Runnable {

    private static final int MESSAGE_RANGE = 2; // range in which players will receive messages
    private static final int PLAYING_RANGE = 1; // range in which players are considered to be on platforms

    private final Messages messages;

    public GameThread(@NotNull CableCorners cableCorners) {
        messages = cableCorners.getMessages();
    }

    private @NotNull List<Player> getPlayers(@NotNull Venue venue, int expand) {
        List<Player> list = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (venue.contains(player, expand)) {
                list.add(player);
            }
        }

        return list;
    }

    @Override
    public void run() {
        for (Venue venue : VenueHandler.getActiveVenues()) {
            int venueId = venue.getId();

            switch (venue.getGameState()) {
                case COUNTDOWN -> {
                    if (venue.isTaskComplete()) {
                        venue.decreaseTime();
                    } else {
                        venue.setTime(venue.getCountdownDuration());
                        venue.markTaskCompleted();
                    }

                    Message message = messages.message("game.countdown")
                            .placeholder("{s}", (venue.getTime() == 1) ? "" : "s")
                            .placeholder("{seconds}", String.valueOf(venue.getTime()));
                    getPlayers(venue, MESSAGE_RANGE).forEach(message::send);

                    if (venue.getTime() <= 1) {
                        venue.setGameState(GameState.ELIMINATION);
                    }
                }
                case ELIMINATION -> {
                    if (venue.isTaskComplete()) {
                        venue.decreaseTime();
                    } else {
                        venue.setTime(venue.getEliminationDuration());

                        Platform platform = venue.selectPlatform();
                        int platformId = venue.getPlatformId(platform);
                        String name = platform.getName();

                        Message message = messages.message("game.destroy")
                                .placeholder("{platform}", Utils.format(name));
                        getPlayers(venue, MESSAGE_RANGE).forEach(message::send);

                        // save players in round
                        getPlayers(venue, PLAYING_RANGE).forEach(p -> venue.getAlivePlayers().add(p.getUniqueId()));

                        platform.remove();
                        CornersAPI.getListeners().forEach(l -> l.onPlatformDestroy(venueId, platformId));
                        venue.markTaskCompleted();
                    }
                    if (venue.getTime() <= 1) {
                        venue.setGameState(GameState.REPLACEMENT);
                        venue.setTime(venue.getReplacementDuration());
                    }
                }
                case REPLACEMENT -> {
                    if (venue.isTaskComplete()) {
                        venue.decreaseTime();
                    } else {
                        venue.placePlatforms();

                        venue.setTime(venue.getReplacementDuration());
                        venue.markTaskCompleted();

                        List<Player> survivingPlayers = new ArrayList<>();

                        for (UUID uuid : venue.getAlivePlayers()) {
                            Player player = Bukkit.getPlayer(uuid);

                            if (player != null && venue.contains(player, PLAYING_RANGE)) {
                                survivingPlayers.add(player);
                            }
                        }

                        CornersAPI.getListeners().forEach(l -> l.onPlatformReplace(venueId, survivingPlayers));
                        venue.getAlivePlayers().clear();
                    }
                    if (venue.getTime() <= 1) {
                        venue.setGameState(GameState.COUNTDOWN);
                    }
                }
            }
        }
    }
}
