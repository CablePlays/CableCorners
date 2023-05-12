package me.cable.corners.component.region;

import me.cable.corners.component.AdvancedBlockFace;
import me.cable.corners.component.GameState;
import me.cable.corners.helper.DefaultValues;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Venue extends Region {

    private final static int PLATFORMS = 4;

    private final int id;
    private final List<Platform> platforms;

    private boolean active = false;

    private int size;
    private int space;
    private Coords centre;

    private int countdownDuration = 10;
    private int eliminationDuration = 5;
    private int replacementDuration = 5;

    private GameState gameState = GameState.START;
    private int time;
    private boolean taskComplete = false;

    public Venue(int id, int size, int space, @NotNull Coords centre, @NotNull String world, @Nullable List<Platform> platforms) {
        super(world);

        this.id = id;
        this.size = Math.max(size, 1);
        this.space = Math.max(space, 0);
        this.centre = centre;

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

        this.platforms = platforms;

        updateProperties();
        placePlatforms();
    }

    public static @NotNull Venue load(@NotNull ConfigurationSection configurationSection) {
        List<Platform> platforms = new ArrayList<>();
        String world = configurationSection.getString("world", "world");

        for (int i = 1; i <= PLATFORMS; i++) {
            String materialString = configurationSection.getString("platforms." + i + ".material");
            Material material = (materialString == null) ? null : Material.getMaterial(materialString);
            Platform platform = new Platform(world, material == null ? Material.BEDROCK : material);
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

        configurationSection.set("x", centre.getX());
        configurationSection.set("y", centre.getY());
        configurationSection.set("z", centre.getZ());
        configurationSection.set("world", world);

        configurationSection.set("durations.countdown-duration", countdownDuration);
        configurationSection.set("durations.elimination-duration", eliminationDuration);
        configurationSection.set("durations.replacement-duration", replacementDuration);

        for (Platform platform : platforms) {
            int i = platforms.indexOf(platform) + 1;
            ConfigurationSection a = configurationSection.createSection("platforms." + i);

            a.set("material", platform.getMaterial().toString());
            a.set("name", platform.getName());
        }
    }

    private void updateProperties() {
        setCoords(centre.add(space + size, 5, space + size), centre.subtract(space + size, 5, space + size));

        int move = space / 2 + 1;
        int two = move + size - 1;

        boolean odd = (space & 1) == 1;

        for (int i = 0; i < PLATFORMS; i++) {
            Platform platform = platforms.get(i);
            AdvancedBlockFace a = AdvancedBlockFace.values()[i];

            platform.setCoords(
                    centre.getRelative(a.blockFace1(), move - a.subtract1(odd)).getRelative(a.blockFace2(), move - a.subtract2(odd)),
                    centre.getRelative(a.blockFace1(), two - a.subtract1(odd)).getRelative(a.blockFace2(), two - a.subtract2(odd))
            );
        }
    }

    public @NotNull List<Platform> getPlatforms() {
        if (platforms.size() < 4) {
            throw new IllegalStateException("Venue has less than 4 platforms");
        }

        return platforms;
    }

    public @NotNull Platform selectPlatform() {
        return getPlatforms().get((int) (Math.random() * PLATFORMS));
    }

    public void placePlatforms() {
        platforms.forEach(Platform::fill);
    }

    public void removePlatforms() {
        platforms.forEach(Platform::remove);
    }

    public void setWorld(@NotNull String world) {
        super.setWorld(world, true);
        platforms.forEach(a -> a.setWorld(world, true));
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        updateProperties();
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
        updateProperties();
    }

    public @NotNull Coords getCentre() {
        return centre;
    }

    public void setCentre(@NotNull Coords centre) {
        this.centre = centre;
        updateProperties();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;

        if (active) {
            setTime(getCountdownDuration());
            setGameState(GameState.START);
        } else {
            placePlatforms();
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

    public void completeTask() {
        taskComplete = true;
    }
}
