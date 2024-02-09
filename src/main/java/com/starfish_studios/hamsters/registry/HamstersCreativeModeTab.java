package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.starfish_studios.hamsters.registry.HamstersItems.HAMSTER;
import static com.starfish_studios.hamsters.registry.HamstersItems.HAMSTER_SPAWN_EGG;

@Mod.EventBusSubscriber(modid = Hamsters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HamstersCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Hamsters.MOD_ID);

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab> ITEM_GROUP = CREATIVE_MODE_TABS.register("item_group", () -> CreativeModeTab.builder().icon(HAMSTER_SPAWN_EGG.get()::getDefaultInstance).title(Component.translatable("itemGroup.hamsters.tab")).displayItems((featureFlagSet, output) -> {

        // output.accept(TUNNEL);
        output.accept(HamstersItems.HAMSTER_WHEEL.get());


        output.accept(HAMSTER_SPAWN_EGG.get());

        // TODO: Not very pretty right now. Maybe there's a better way to do this?

        ItemStack item0 = new ItemStack(HAMSTER.get());
        item0.getOrCreateTag().putInt("Variant", 0);
        output.accept(item0);

        ItemStack item1 = new ItemStack(HAMSTER.get());
        item1.getOrCreateTag().putInt("Variant", 1);
        output.accept(item1);

        ItemStack item2 = new ItemStack(HAMSTER.get());
        item2.getOrCreateTag().putInt("Variant", 2);
        output.accept(item2);

        ItemStack item3 = new ItemStack(HAMSTER.get());
        item3.getOrCreateTag().putInt("Variant", 3);
        output.accept(item3);

        ItemStack item4 = new ItemStack(HAMSTER.get());
        item4.getOrCreateTag().putInt("Variant", 4);
        output.accept(item4);

        ItemStack item5 = new ItemStack(HAMSTER.get());
        item5.getOrCreateTag().putInt("Variant", 5);
        output.accept(item5);

        ItemStack item6 = new ItemStack(HAMSTER.get());
        item6.getOrCreateTag().putInt("Variant", 6);
        output.accept(item6);



        }).build()
    );

    private static CreativeModeTab register(String id, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Hamsters.MOD_ID, id), tab);
    }
}
