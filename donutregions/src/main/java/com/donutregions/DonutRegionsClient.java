package com.donutregions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class DonutRegionsClient implements ClientModInitializer {
    public static KeyBinding openMapKey;
    public static KeyBinding toggleHudKey;

    @Override
    public void onInitializeClient() {
        Config.load();

        openMapKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.donutregions.map", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "category.donutregions"));
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.donutregions.toggle_hud", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.donutregions"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMapKey.wasPressed()) {
                if (client.currentScreen == null && client.player != null) {
                    client.setScreen(new RegionMapScreen());
                }
            }
            while (toggleHudKey.wasPressed()) {
                Config.hudEnabled = !Config.hudEnabled;
            }
        });

        HudRenderCallback.EVENT.register(DonutRegionsClient::renderHud);
    }

    /** Player X/Z in overworld-equivalent coordinates (Nether x8 if enabled in the config). */
    public static double[] playerXZ(MinecraftClient mc) {
        boolean nether = mc.world != null && mc.world.getRegistryKey() == World.NETHER;
        double scale = (nether && Config.netherScaling) ? 8.0 : 1.0;
        return new double[]{mc.player.getX() * scale, mc.player.getZ() * scale};
    }

    private static void renderHud(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!Config.hudEnabled || mc.player == null || mc.options.hudHidden) return;

        double[] p = playerXZ(mc);
        RegionMap.Result r = RegionMap.lookup(p[0], p[1]);

        String line1 = r == null ? "Outside region map" : "Region " + r.id() + " - " + r.region().displayName;
        String line2 = String.format("X: %d  Z: %d", (int) Math.floor(p[0]), (int) Math.floor(p[1]));
        int color = r == null ? 0xFFAAAAAA : r.region().color;

        int x = Config.hudX, y = Config.hudY;
        int w = mc.textRenderer.getWidth(line1);
        if (Config.showCoordinates) w = Math.max(w, mc.textRenderer.getWidth(line2));
        int h = Config.showCoordinates ? 24 : 12;

        if (Config.hudBackground) {
            ctx.fill(x - 2, y - 2, x + w + 8, y + h, 0x90000000);
            ctx.fill(x - 2, y - 2, x, y + h, color);
        }
        ctx.drawTextWithShadow(mc.textRenderer, line1, x + 4, y, color);
        if (Config.showCoordinates) {
            ctx.drawTextWithShadow(mc.textRenderer, line2, x + 4, y + 12, 0xFFFFFFFF);
        }
    }
}
