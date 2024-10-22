package com.starfish_studios.hamsters.entity.common;

import software.bernie.geckolib.animatable.GeoEntity;

public interface HamstersGeoEntity extends GeoEntity {

    @Override
    default double getBoneResetTime() {
        return 5;
    }



}
