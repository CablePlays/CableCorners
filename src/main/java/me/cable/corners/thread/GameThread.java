package me.cable.corners.thread;

import me.cable.corners.CableCorners;
import me.cable.corners.component.GameState;
import me.cable.corners.component.Message;
import me.cable.corners.component.region.Platform;
import me.cable.corners.component.region.Venue;
import me.cable.corners.handler.Settings;
import me.cable.corners.manager.VenueManager;
import org.jetbrains.annotations.NotNull;

public class GameThread implements Runnable {

    private final Settings settings;

    public GameThread(@NotNull CableCorners cableCorners) {
        settings = cableCorners.getSettings();
    }

    @Override
    public void run() {
        for (Venue venue : VenueManager.getActiveVenues()) {
            switch (venue.getGameState()) {
                case COUNTDOWN -> {
                    if (venue.isTaskComplete()) {
                        venue.decreaseTime();
                    } else {
                        venue.setTime(venue.getCountdownDuration());
                        venue.completeTask();
                    }

                    Message message = new Message(settings.stringList("countdown-message"));
                    message.placeholder("{s}", (venue.getTime() == 1) ? "" : "s");
                    message.placeholder("{seconds}", String.valueOf(venue.getTime()));
                    venue.getPlayers().forEach(message::send);

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
                        String name = platform.getName();

                        Message message = new Message(settings.stringList("destroy-message"));
                        message.placeholder("{platform}", name);

                        platform.remove();
                        venue.getPlayers().forEach(message::send);
                        venue.completeTask();
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
                        venue.setTime(venue.getReplacementDuration());
                        venue.placePlatforms();
                        venue.completeTask();
                    }
                    if (venue.getTime() <= 1) {
                        venue.setGameState(GameState.COUNTDOWN);
                    }
                }
            }
        }
    }
}
