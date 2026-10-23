package com.donutregions;

/** Data for the Donut SMP region map: 9x9 grid, each cell 50k x 50k blocks, spanning -225k..225k. */
public final class RegionMap {
    public static final int CELLS = 9;
    public static final int CELL_SIZE = 50_000;
    public static final int HALF = 225_000;

    public enum Region {
        OCEANIA('O', "Oceania", 0xFFF28C1B),
        NA_WEST('W', "NA West", 0xFF2E6DB4),
        NA_EAST('E', "NA East", 0xFF3EA6E8),
        ASIA('A', "Asia", 0xFFF2C14E),
        EU_CENTRAL('U', "EU Central", 0xFF9CCB5B),
        EU_WEST('V', "EU West", 0xFF0FA05A);

        public final char code;
        public final String displayName;
        public final int color;

        Region(char code, String displayName, int color) {
            this.code = code;
            this.displayName = displayName;
            this.color = color;
        }

        static Region fromCode(char c) {
            for (Region r : values()) if (r.code == c) return r;
            throw new IllegalArgumentException("Bad region code " + c);
        }
    }

    /** Region numbers, [row = Z from north to south][col = X from west to east]. */
    public static final int[][] IDS = {
        { 82, 100, 101, 102, 103, 104, 105, 106,  91},
        { 83,  44,  75,  42,  41,  40,  39,  38,  92},
        { 84,  45,  14,  13,  12,  11,  10,  37,  93},
        { 85,  46,  74,   3,   2,   1,  25,  36,  94},
        { 86,  47,  72,  71,   5,   4,  24,  35,  95},
        { 87,  51,  17,   9,   8,   7,  23,  34,  96},
        { 88,  54,  18,  61,  62,  21,  22,  33,  97},
        { 89,  26,  27,  28,  29,  30,  59,  32,  98},
        { 90, 107, 108, 109, 110, 111, 112, 113,  99},
    };

    private static final String[] LAYOUT = {
        "OWWWEEEEE",
        "OWWWEEEEE",
        "OWWWEEEEE",
        "OOOWEEEEE",
        "AAAAEEEEE",
        "AVVUUUUUE",
        "AVVUUUUUU",
        "UVUUUUUUU",
        "UVVVVVVVU",
    };

    public record Result(int id, Region region, int col, int row) {}

    public static Region regionAt(int col, int row) {
        return Region.fromCode(LAYOUT[row].charAt(col));
    }

    /** Returns null if the position is outside the -225k..225k square. */
    public static Result lookup(double x, double z) {
        if (x < -HALF || x > HALF || z < -HALF || z > HALF) return null;
        int col = Math.min(CELLS - 1, (int) Math.floor((x + HALF) / CELL_SIZE));
        int row = Math.min(CELLS - 1, (int) Math.floor((z + HALF) / CELL_SIZE));
        return new Result(IDS[row][col], regionAt(col, row), col, row);
    }

    private RegionMap() {}
}
