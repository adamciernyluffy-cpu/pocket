package com.aether.client;

public final class ModuleEntry {
    public final String name;
    public final String category;
    public final String description;
    public boolean enabled;
    public double amount = 50;
    public boolean option = true;
    public String mode = "Default";

    public ModuleEntry(String name, String category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }
}
