package com.aether.client;

import java.util.ArrayList;
import java.util.List;

public final class ModuleRegistry {
    public static final List<String> CATEGORIES = List.of("All", "Combat", "Movement", "Player", "Render", "World", "Inventory", "Utility", "Social", "HUD", "Misc");
    public static final List<ModuleEntry> MODULES = new ArrayList<>();

    static {
        add("Combat", "Aim Display", "Shows a configurable visual target indicator.", "Target HUD", "Hit Sound", "Combat Text", "Damage Numbers", "Attack Indicator", "Target Outline", "Reach Display", "CPS Counter", "Combo Counter", "Hitbox Viewer");
        add("Movement", "Sprint Toggle", "Client-side sprint preference.", "Motion HUD", "Speedometer", "Direction HUD", "Jump Circle", "Step Counter", "Velocity Graph", "Movement Trails", "Landing Particles", "Strafe Guide", "Momentum Display");
        add("Player", "Player Status", "Displays your current player status.", "Coordinates", "Armor Status", "Durability Alerts", "Food Status", "Potion Timer", "Effect HUD", "Totem Counter", "Player Model Preview", "Skin Preview", "Cape Preview");
        add("Render", "Cosmic Sky", "Cosmetic starfield and nebula styling for the Aether interface.", "Fullbright Preference", "Entity Highlights", "Block Outline Style", "Particle Controls", "Weather Visuals", "Time Display", "Camera Sway", "Zoom Preference", "Screenshot Overlay", "Color Theme");
        add("World", "World Info", "Displays local world information.", "Biome Display", "Seed Notes", "Chunk Grid Preview", "Light Level HUD", "Block Info", "Waypoint Markers", "Waypoint Manager", "Structure Notes", "World Clock", "Local Map Panel");
        add("Inventory", "Inventory HUD", "Displays inventory information.", "Item Durability", "Hotbar Labels", "Item Names", "Slot Highlights", "Container Preview", "Recipe Notes", "Item Counter", "Equipment Preview", "Inventory Search", "Stack Counter");
        add("Utility", "Screenshot Helper", "Adds quick access to screenshot tools.", "Timer", "Stopwatch", "Session Stats", "Notes Panel", "Calculator", "Clipboard Notes", "Quick Messages", "Profile Manager", "Layout Presets", "UI Scale");
        add("Social", "Friend Notes", "Local notes for players you choose to remember.", "Party HUD", "Chat Timestamp", "Chat Cleanup", "Chat Search", "Mention Highlight", "Message Templates", "Social HUD", "Name Color Preview", "Server Info", "Ping Display");
        add("HUD", "FPS Counter", "Shows current frame rate.", "Clock", "Keystrokes Display", "Coordinates HUD", "Crosshair Editor", "Armor HUD", "Potion HUD", "Target Panel", "Watermark", "HUD Editor", "HUD Presets");
        add("Misc", "Aether Welcome", "Shows the Aether welcome panel.", "Cosmic Particles", "Animation Settings", "Sound Settings", "Theme Editor", "Accessibility", "Reduced Motion", "Debug Overlay", "Module Search", "Favorites", "About Aether");
    }

    private static void add(String category, String firstName, String firstDescription, String... names) {
        MODULES.add(new ModuleEntry(firstName, category, firstDescription));
        for (String name : names) MODULES.add(new ModuleEntry(name, category, name + " interface module. Configure its available options in the settings panel."));
    }

    private ModuleRegistry() {}
}
