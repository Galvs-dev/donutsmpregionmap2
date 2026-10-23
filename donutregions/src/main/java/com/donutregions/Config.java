package com.donutregions;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/** Tiny reader for simple "key: value" YAML files. */
public final class Config {
    public static boolean hudEnabled = true;
    public static int hudX = 6;
    public static int hudY = 6;
    public static boolean hudBackground = true;
    public static boolean showCoordinates = true;
    public static boolean netherScaling = true;

    public static void load() {
        Path file = FabricLoader.getInstance().getConfigDir().resolve("donutregions.yml");
        try {
            if (!Files.exists(file)) {
                try (InputStream in = Config.class.getResourceAsStream("/donutregions.yml")) {
                    if (in != null) Files.copy(in, file);
                }
            }
            if (!Files.exists(file)) return;

            for (String raw : Files.readAllLines(file)) {
                try {
                    String line = raw;
                    int hash = line.indexOf('#');
                    if (hash >= 0) line = line.substring(0, hash);
                    line = line.trim();
                    int colon = line.indexOf(':');
                    if (line.isEmpty() || colon < 0) continue;
                    String key = line.substring(0, colon).trim();
                    String val = line.substring(colon + 1).trim();
                    switch (key) {
                        case "hud_enabled" -> hudEnabled = Boolean.parseBoolean(val);
                        case "hud_x" -> hudX = Integer.parseInt(val);
                        case "hud_y" -> hudY = Integer.parseInt(val);
                        case "hud_background" -> hudBackground = Boolean.parseBoolean(val);
                        case "show_coordinates" -> showCoordinates = Boolean.parseBoolean(val);
                        case "nether_scaling" -> netherScaling = Boolean.parseBoolean(val);
                        default -> { }
                    }
                } catch (NumberFormatException ignored) {
                    // bad value on one line: keep the default for that option
                }
            }
        } catch (IOException e) {
            System.err.println("[donutregions] Could not read config: " + e.getMessage());
        }
    }

    private Config() {}
}
