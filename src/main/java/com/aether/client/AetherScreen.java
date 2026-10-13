package com.aether.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public final class AetherScreen extends Screen {
    private String category = "All";
    private ModuleEntry selected;
    private double anim = 0;
    private double cameraX = 0, cameraY = 0;
    private int scroll = 0;
    private int panelX, panelY;
    private boolean dragging;
    private int dragX, dragY;
    private final List<Star> stars = new ArrayList<>();

    public AetherScreen() {
        super(Text.literal("Aether Cosmic Dimension"));
        for (int i = 0; i < 180; i++) stars.add(new Star(Math.random(), Math.random(), .25 + Math.random() * .9, Math.random() * 6.28));
    }

    @Override protected void init() {
        panelX = Math.max(20, width / 2 - 235);
        panelY = Math.max(20, height / 2 - 170);
    }

    @Override public void tick() { anim = Math.min(1, anim + .045); }

    @Override public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        int w = width, h = height;
        int alpha = (int)(205 * anim);
        ctx.fill(0, 0, w, h, 0xFF03040D);
        // Starfield with subtle mouse parallax.
        for (Star s : stars) {
            int x = (int)((s.x * w + cameraX * s.depth * 22 + w) % w);
            int y = (int)((s.y * h + cameraY * s.depth * 16 + h) % h);
            int shade = (int)(110 + 120 * (0.5 + 0.5 * Math.sin(anim * 5 + s.phase)));
            int color = ((shade & 255) << 24) | 0x00A8EFFF;
            ctx.fill(x, y, x + (s.depth > .8 ? 2 : 1), y + (s.depth > .8 ? 2 : 1), color);
        }
        // Nebula-like broad translucent bands and horizon grid.
        ctx.fill(0, h / 3, w, h / 3 + 2, 0x173F36FF);
        ctx.fill(0, h / 3 + 3, w, h / 3 + 5, 0x11236DCE);
        int horizon = h / 2 + 35;
        for (int i = 0; i < 12; i++) {
            int yy = horizon + i * i * 2;
            if (yy < h) ctx.fill(w / 2 - (yy - horizon) * 3, yy, w / 2 + (yy - horizon) * 3, yy + 1, 0x343A55FF);
        }
        int panelW = Math.min(470, w - 30), panelH = Math.min(360, h - 30);
        int x = Math.min(panelX, w - panelW - 10), y = Math.min(panelY, h - panelH - 10);
        // Main glass panel, layered to fake rounded corners without custom textures.
        ctx.fill(x + 4, y, x + panelW - 4, y + panelH, 0xE80B1022);
        ctx.fill(x, y + 4, x + panelW, y + panelH - 4, 0xE80B1022);
        ctx.fill(x + 1, y + 1, x + panelW - 1, y + panelH - 1, 0xFF0C1226);
        ctx.fill(x + 1, y + 1, x + panelW - 1, y + 3, 0xFF39DDF5);
        ctx.drawText(textRenderer, "AETHER  /  COSMIC DIMENSION", x + 16, y + 13, 0xFFB9F7FF, false);
        ctx.drawText(textRenderer, "3D SPACE INTERFACE  •  " + ModuleRegistry.MODULES.size() + " MODULES", x + 16, y + 27, 0xFF858FB7, false);
        ctx.fill(x + 12, y + 44, x + panelW - 12, y + 45, 0x6639DDF5);

        int sideW = 100;
        int sy = y + 55;
        for (String cat : ModuleRegistry.CATEGORIES) {
            boolean active = category.equals(cat);
            ctx.fill(x + 10, sy - 3, x + sideW, sy + 14, active ? 0xFF16475C : 0xFF111A30);
            ctx.drawText(textRenderer, cat, x + 16, sy, active ? 0xFFB9FAFF : 0xFF8995B8, false);
            sy += 22;
        }
        int listX = x + sideW + 18;
        int listW = panelW - sideW - 35;
        ctx.drawText(textRenderer, selected == null ? "MODULE CONSTELLATION" : "MODULE SETTINGS  /  " + selected.name.toUpperCase(), listX, y + 55, 0xFF9FEFFF, false);
        if (selected != null) {
            ctx.drawText(textRenderer, selected.description, listX, y + 76, 0xFFB7C3E4, false);
            ctx.drawText(textRenderer, "Left-click: toggle setting", listX, y + 101, 0xFF8995B8, false);
            ctx.drawText(textRenderer, "Right-click or Escape: back", listX, y + 116, 0xFF8995B8, false);
            ctx.fill(listX, y + 143, listX + listW, y + 144, 0x6639DDF5);
            ctx.drawText(textRenderer, "Enabled", listX, y + 158, 0xFFB9FAFF, false);
            ctx.drawText(textRenderer, selected.enabled ? "ON" : "OFF", listX + listW - 30, y + 158, selected.enabled ? 0xFF55F2BD : 0xFFFF8198, false);
            ctx.drawText(textRenderer, "Intensity", listX, y + 184, 0xFFB9FAFF, false);
            ctx.fill(listX, y + 201, listX + listW, y + 205, 0xFF283450);
            ctx.fill(listX, y + 201, listX + (int)(listW * selected.amount / 100.0), y + 205, 0xFF39DDF5);
            ctx.drawText(textRenderer, "Option: " + (selected.option ? "On" : "Off"), listX, y + 222, 0xFFB7C3E4, false);
            ctx.drawText(textRenderer, "Mode: " + selected.mode, listX, y + 242, 0xFFB7C3E4, false);
            ctx.drawText(textRenderer, "Click intensity bar to adjust", listX, y + 267, 0xFF7785AA, false);
        } else {
            List<ModuleEntry> visible = new ArrayList<>();
            for (ModuleEntry m : ModuleRegistry.MODULES) if (category.equals("All") || m.category.equals(category)) visible.add(m);
            int rowY = y + 76;
            int shown = 0;
            for (int i = Math.max(0, scroll); i < visible.size() && shown < 11; i++, shown++) {
                ModuleEntry m = visible.get(i);
                int rowH = 22;
                boolean hover = mouseX >= listX && mouseX <= listX + listW && mouseY >= rowY - 3 && mouseY <= rowY + 17;
                ctx.fill(listX, rowY - 3, listX + listW, rowY + 17, hover ? 0xFF182B46 : 0xFF10192D);
                ctx.fill(listX, rowY - 3, listX + 2, rowY + 17, m.enabled ? 0xFF4DF3C0 : 0xFF34415F);
                ctx.drawText(textRenderer, m.name, listX + 8, rowY + 2, m.enabled ? 0xFFB7FFE7 : 0xFFD3DCF7, false);
                ctx.drawText(textRenderer, m.enabled ? "ON" : "OFF", listX + listW - 26, rowY + 2, m.enabled ? 0xFF55F2BD : 0xFF7884A6, false);
                rowY += rowH;
            }
            ctx.drawText(textRenderer, "Left-click: toggle    Right-click: settings    Scroll: browse", listX, y + panelH - 22, 0xFF7E8BB0, false);
        }
        ctx.drawText(textRenderer, "RIGHT SHIFT: CLOSE  •  COSMIC UI IS CLIENT-SIDE", x + 14, y + panelH - 13, 0xFF657395, false);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int panelW = Math.min(470, width - 30), panelH = Math.min(360, height - 30);
        int x = Math.min(panelX, width - panelW - 10), y = Math.min(panelY, height - panelH - 10);
        if (selected != null) {
            if (button == 1) { selected = null; return true; }
            int listX = x + 118, listW = panelW - 135;
            if (mouseY >= y + 150 && mouseY <= y + 177) selected.enabled = !selected.enabled;
            else if (mouseY >= y + 194 && mouseY <= y + 213) selected.amount = Math.max(0, Math.min(100, (mouseX - listX) / listW * 100));
            else if (mouseY >= y + 215 && mouseY <= y + 237) selected.option = !selected.option;
            else if (mouseY >= y + 238 && mouseY <= y + 260) selected.mode = selected.mode.equals("Default") ? "Alternate" : "Default";
            return true;
        }
        if (mouseY >= y && mouseY < y + 43 && mouseX >= x && mouseX < x + panelW) {
            if (button == 0) { dragging = true; dragX = (int)mouseX - x; dragY = (int)mouseY - y; }
            return true;
        }
        if (mouseX >= x + 10 && mouseX <= x + 104 && mouseY >= y + 52 && mouseY <= y + 300) {
            int index = (int)((mouseY - (y + 52)) / 22);
            if (index >= 0 && index < ModuleRegistry.CATEGORIES.size()) { category = ModuleRegistry.CATEGORIES.get(index); scroll = 0; }
            return true;
        }
        int listX = x + 118, listW = panelW - 135;
        if (mouseX >= listX && mouseX <= listX + listW && mouseY >= y + 73 && mouseY <= y + panelH - 30) {
            List<ModuleEntry> visible = new ArrayList<>();
            for (ModuleEntry m : ModuleRegistry.MODULES) if (category.equals("All") || m.category.equals(category)) visible.add(m);
            int index = Math.max(0, scroll) + (int)((mouseY - (y + 73)) / 22);
            if (index < visible.size()) {
                ModuleEntry m = visible.get(index);
                if (button == 1) selected = m;
                else if (button == 0) m.enabled = !m.enabled;
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override public boolean mouseReleased(double mouseX, double mouseY, int button) { dragging = false; return super.mouseReleased(mouseX, mouseY, button); }
    @Override public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) { panelX = (int)mouseX - dragX; panelY = (int)mouseY - dragY; return true; }
        cameraX += deltaX * .004; cameraY += deltaY * .004; return true;
    }
    @Override public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) { scroll = Math.max(0, Math.min( Math.max(0, ModuleRegistry.MODULES.size() - 10), scroll - (int)Math.signum(verticalAmount) * 2)); return true; }
    @Override public boolean shouldPause() { return false; }
    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) { if (selected != null) selected = null; else close(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private record Star(double x, double y, double depth, double phase) {}
}
