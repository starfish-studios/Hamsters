package com.starfish_studios.hamsters.block.properties;

import net.minecraft.util.StringRepresentable;

public enum CageType implements StringRepresentable {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom"),
    NONE("none");

    private final String name;

    private CageType(String type) {
        this.name = type;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}