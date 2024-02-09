package com.starfish_studios.hamsters.item;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.client.renderer.HamsterWheelRenderer;
import com.starfish_studios.hamsters.client.renderer.item.HamsterWheelItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;

public class HamsterWheelItem extends BlockItem implements IAnimatable {
    public HamsterWheelItem(Block block, Properties properties) {
        super(block, properties);
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new HamsterWheelItemRenderer();
            }
        });
    }


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