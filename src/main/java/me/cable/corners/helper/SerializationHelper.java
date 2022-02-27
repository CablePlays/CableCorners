package me.cable.corners.helper;

import me.cable.corners.CableCorners;
import me.cable.corners.component.region.Venue;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializationHelper {

    private final CableCorners cableCorners;

    public SerializationHelper(@NotNull CableCorners cableCorners) {
        this.cableCorners = cableCorners;
    }

    private @NotNull File getFile() throws IOException {
        File parent = cableCorners.getDataFolder();
        File file = new File(parent, "venues.save");

        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    public void write(@NotNull List<Venue> objects) {
        if (objects.isEmpty()) {
            return;
        }

        try {
            FileOutputStream file = new FileOutputStream(getFile(), false);
            ObjectOutputStream out = new ObjectOutputStream(file);

            for (Object object : objects) {
                out.writeObject(object);
            }

            out.close();
            file.close();
        } catch (IOException ex) {
            cableCorners.getLogger().severe("Could not save data");
            ex.printStackTrace();
        }
    }

    public @NotNull List<Venue> read() {
        List<Venue> list = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(getFile());
            ObjectInputStream in = new ObjectInputStream(file);

            try {
                while (true) {
                    Object object = in.readObject();
                    Venue venue = (Venue) object;

                    list.add(venue);
                }
            } catch (EOFException ignored) {
                // ignored
            }

            in.close();
            file.close();
        } catch (IOException | ClassNotFoundException ex) {
            cableCorners.getLogger().severe("Could not load data");
            ex.printStackTrace();
        }

        return list;
    }
}
