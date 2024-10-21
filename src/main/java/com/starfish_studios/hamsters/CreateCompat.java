package com.starfish_studios.hamsters;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import net.minecraft.resources.ResourceLocation;

public class CreateCompat {
    public static void setup(){
        BlockStressDefaults.DEFAULT_CAPACITIES.put(new ResourceLocation(Hamsters.MOD_ID, "hamster_wheel"), 16.0);
    }

}
