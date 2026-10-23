package com.donutregions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class RegionMapScreen extends Screen {
    public RegionMapScreen() {
        super(Text.literal("Donut SMP Region Map"));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (DonutRegionsClient.openMapKey.matchesKey(keyCode, scanCode)) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        int n = RegionMap.CELLS;
        int cell = Math.max(12, Math.min(this.width - 100, this.height - 80) / n);
        int size = cell * n;
        int left = (this.width - size) / 2;
        int top = 28;

        double[] p = DonutRegionsClient.playerXZ(mc);
        RegionMap.Result me = RegionMap.lookup(p[0], p[1]);

        String info = (me == null ? "Outside region map" : "Region " + me.id() + " - " + me.region().displayName)
                + String.format("   (X: %d, Z: %d)", (int) Math.floor(p[0]), (int) Math.floor(p[1]));
        ctx.drawCenteredTextWithShadow(this.textRenderer, info, this.width / 2, 10, 0xFFFFFFFF);

        // Cells
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int x = left + col * cell;
                int y = top + row * cell;
                ctx.fill(x, y, x + cell, y + cell, RegionMap.regionAt(col, row).color);
                ctx.drawCenteredTextWithShadow(this.textRenderer, String.valueOf(RegionMap.IDS[row][col]),
                        x + cell / 2, y + cell / 2 - 4, 0xFFFFFFFF);
            }
        }

        // Grid lines
        for (int i = 0; i <= n; i++) {
            ctx.fill(left + i * cell, top, left + i * cell + 1, top + size + 1, 0xFF000000);
            ctx.fill(left, top + i * cell, left + size + 1, top + i * cell + 1, 0xFF000000);
        }

        // Axis labels
        for (int i = 0; i <= n; i++) {
            String s = (-225 + 50 * i) + "k";
            ctx.drawCenteredTextWithShadow(this.textRenderer, s, left + i * cell, top + size + 4, 0xFFCCCCCC);
            ctx.drawTextWithShadow(this.textRenderer, s, left - 4 - this.textRenderer.getWidth(s),
                    top + i * cell - 4, 0xFFCCCCCC);
        }
        ctx.drawTextWithShadow(this.textRenderer, "X-", left - 24, top + size + 4, 0xFFFF5555);
        ctx.drawTextWithShadow(this.textRenderer, "X+", left + size + 6, top + size + 4, 0xFF55FF55);
        ctx.drawTextWithShadow(this.textRenderer, "Z-", left - 24, top - 12, 0xFFFF5555);
        ctx.drawTextWithShadow(this.textRenderer, "Z+", left - 24, top + size + 12, 0xFF55FF55);

        // Legend
        int lx = left;
        int ly = top + size + 20;
        for (RegionMap.Region r : RegionMap.Region.values()) {
            ctx.fill(lx, ly, lx + 8, ly + 8, r.color);
            ctx.drawTextWithShadow(this.textRenderer, r.displayName, lx + 11, ly, 0xFFFFFFFF);
            lx += 11 + this.textRenderer.getWidth(r.displayName) + 12;
        }

        // Highlight current cell + player marker
        if (me != null) {
            int cx = left + me.col() * cell;
            int cy = top + me.row() * cell;
            ctx.fill(cx, cy, cx + cell, cy + 2, 0xFFFFFFFF);
            ctx.fill(cx, cy + cell - 1, cx + cell, cy + cell + 1, 0xFFFFFFFF);
            ctx.fill(cx, cy, cx + 2, cy + cell, 0xFFFFFFFF);
            ctx.fill(cx + cell - 1, cy, cx + cell + 1, cy + cell, 0xFFFFFFFF);

            int px = left + (int) ((p[0] + RegionMap.HALF) / (2.0 * RegionMap.HALF) * size);
            int py = top + (int) ((p[1] + RegionMap.HALF) / (2.0 * RegionMap.HALF) * size);
            ctx.fill(px - 3, py - 3, px + 4, py + 4, 0xFF000000);
            ctx.fill(px - 2, py - 2, px + 3, py + 3, 0xFFFF2222);
        }
    }
}
