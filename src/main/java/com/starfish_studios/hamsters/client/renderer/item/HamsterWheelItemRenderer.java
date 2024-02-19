package com.starfish_studios.hamsters.client.renderer.item;

import com.starfish_studios.hamsters.client.model.item.HamsterWheelItemModel;
import com.starfish_studios.hamsters.item.HamsterWheelItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class HamsterWheelItemRenderer extends GeoItemRenderer<HamsterWheelItem> {
    public HamsterWheelItemRenderer() {
        super(new HamsterWheelItemModel());
    }
}
