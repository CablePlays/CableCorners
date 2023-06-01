package me.cable.corners.component.region;

import me.cable.corners.component.AdvancedBlockFace;
import me.cable.corners.component.Coords;
import me.cable.corners.component.GameState;
import me.cable.corners.util.DefaultValues;
import me.cable.corners.util.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Venue extends Region {

    private final static int PLATFORMS = 4;

    private final int id;
    private final List<Platform> platforms;
    private final List<UUID> alivePlayers = new ArrayList<>();

    private boolean active = false;

    private int size;
    private int space;
    private Coords center;

    private int countdownDuration = 10;
    private int eliminationDuration = 5;
    private int replacementDuration = 3;

    private GameState gameState = GameState.START;
    private int time;
    private boolean taskComplete = false;

    public Venue(int id, int size, int space, @NotNull Coords center, @NotNull String world, @Nullable List<Platform> platforms) {
        super(world);

        this.id = id;
        this.size = Math.max(size, 1);
        this.space = Math.max(space, 0);
        this.center = center;

        if (platforms == null) {
            DefaultValues defaultValues = new DefaultValues();
            platforms = new ArrayList<>();

            for (int i = 0; i < PLATFORMS; i++) {
                Platform platform = new Platform(world, defaultValues.getCurrentMaterial());
                platform.setName(defaultValues.getCurrentName());
                platforms.add(platform);
                defaultValues.next();
            }
        }

        if (platforms.size() != 4) {
            throw new IllegalArgumentException("List does not have 4 platforms");
        }

        this.platforms = List.copyOf(platforms);

        updateSizes();
        placePlatforms();
    }

    public static @NotNull Venue load(@NotNull ConfigurationSection configurationSection) {
        List<Platform> platforms = new ArrayList<>();
        String world = configurationSection.getString("world", "world");

        for (int i = 0; i < PLATFORMS; i++) {
            Material material = Utils.materialFromString(
                    configurationSection.getString("platforms." + i + ".material"), Material.BEDROCK);
            Platform platform = new Platform(world, material);
            platform.setName(configurationSection.getString("platforms." + i + ".name"));
            platforms.add(platform);
        }

        Venue venue = new Venue(
                Integer.parseInt(configurationSection.getName()),
                configurationSection.getInt("platform-size"),
                configurationSection.getInt("platform-space"),
                new Coords(
                        configurationSection.getInt("x"),
                        configurationSection.getInt("y"),
                        configurationSection.getInt("z")
                ),
                world,
                platforms
        );

        venue.setActive(configurationSection.getBoolean("active"));

        venue.setCountdownDuration(configurationSection.getInt("durations.countdown-duration"));
        venue.setEliminationDuration(configurationSection.getInt("durations.elimination-duration"));
        venue.setReplacementDuration(configurationSection.getInt("durations.replacement-duration"));

        return venue;
    }

    public void save(@NotNull ConfigurationSection configurationSection) {
        configurationSection.set("active", active);
        configurationSection.set("platform-size", size);
        configurationSection.set("platform-space", space);

        configurationSection.set("x", center.getX());
        configurationSection.set("y", center.getY());
        configurationSection.set("z", center.getZ());
        configurationSection.set("world", world);

        configurationSection.set("durations.countdown-duration", countdownDuration);
        configurationSection.set("durations.elimination-duration", eliminationDuration);
        configurationSection.set("durations.replacement-duration", replacementDuration);

        for (Platform platform : platforms) {
            int i = platforms.indexOf(platform);
            ConfigurationSection a = configurationSection.createSection("platforms." + i);

            a.set("material", platform.getMaterial().toString());
            a.set("name", platform.getName());
        }
    }

    private void updateSizes() {
        int innerMove = (space + 1) / 2;
        int outerMove = innerMove + size - 1;
        boolean odd = (space & 1) == 1;

        setCoords(center.add(outerMove, 10, outerMove),
                center.subtract(outerMove + (odd ? 0 : 1), 0, outerMove + (odd ? 0 : 1)));

        for (int i = 0; i < PLATFORMS; i++) {
            Platform platform = platforms.get(i);
            AdvancedBlockFace a = AdvancedBlockFace.values()[i];

            Coords corner1;
            Coords corner2;

            if (odd) {
                corner1 = center.getRelative(a.blockFace1(), innerMove).getRelative(a.blockFace2(), innerMove);
                corner2 = center.getRelative(a.blockFace1(), outerMove).getRelative(a.blockFace2(), outerMove);
            } else {
                corner1 = center.getRelative(a.blockFace1(), innerMove + (a.evenMove1() ? 1 : 0))
                        .getRelative(a.blockFace2(), innerMove + (a.evenMove2() ? 1 : 0));
                corner2 = center.getRelative(a.blockFace1(), outerMove + (a.evenMove1() ? 1 : 0))
                        .getRelative(a.blockFace2(), outerMove + (a.evenMove2() ? 1 : 0));
            }

            platform.setCoords(corner1, corner2);
        }
    }

    public @NotNull List<Platform> getPlatforms() {
        return platforms;
    }

    public @NotNull List<UUID> getAlivePlayers() {
        return alivePlayers;
    }

    public @NotNull Platform selectPlatform() {
        return getPlatforms().get((int) (Math.random() * PLATFORMS));
    }

    public int getPlatformId(@NotNull Platform platform) {
        return getPlatforms().indexOf(platform);
    }

    public void placePlatforms() {
        platforms.forEach(Platform::fill);
    }

    public void removePlatforms() {
        platforms.forEach(Platform::remove);
    }

    @Override
    public void setWorldName(@NotNull String world) {
        super.setWorldName(world);
        platforms.forEach(a -> a.setWorldName(world));
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        updateSizes();
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
        updateSizes();
    }

    public @NotNull Coords getCenter() {
        return center;
    }

    public void setCenter(@NotNull Coords center) {
        this.center = center;
        updateSizes();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        placePlatforms();

        if (active) {
            setTime(getCountdownDuration());
            setGameState(GameState.START);
        }
    }

    public int getCountdownDuration() {
        return countdownDuration;
    }

    public void setCountdownDuration(int countdownDuration) {
        this.countdownDuration = countdownDuration;
    }

    public int getEliminationDuration() {
        return eliminationDuration;
    }

    public void setEliminationDuration(int eliminationDuration) {
        this.eliminationDuration = eliminationDuration;
    }

    public int getReplacementDuration() {
        return replacementDuration;
    }

    public void setReplacementDuration(int replacementDuration) {
        this.replacementDuration = replacementDuration;
    }

    public @NotNull GameState getGameState() {
        return gameState;
    }

    public void setGameState(@NotNull GameState gameState) {
        this.gameState = gameState;
        taskComplete = false;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void decreaseTime() {
        time--;
    }

    public boolean isTaskComplete() {
        return taskComplete;
    }

    public void markTaskCompleted() {
        taskComplete = true;
    }
}
