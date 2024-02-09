package com.starfish_studios.hamsters.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class HamsterWheelItem extends BlockItem implements IAnimatable {
    public HamsterWheelItem(Block block, Properties properties) {
        super(block, properties);
    }

    /*
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoItemRenderer<HamsterWheelItem> renderer = null;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = (renderer.getGeoModelProvider().getModel(new ResourceLocation(Hamsters.MOD_ID, "hamster_wheel")));

                return this.renderer;
            }
        });
    }
    */

//    @Override
//    public Supplier<Object> getRenderProvider() {
//        return this.renderProvider;
//    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return GeckoLibUtil.createFactory(this);
    }
}