package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.item.HamsterItem;
import com.starfish_studios.hamsters.item.HamsterWheelItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Hamsters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HamstersItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Hamsters.MOD_ID);

    public static final RegistryObject<Item> HAMSTER_SPAWN_EGG = ITEMS.register("hamster_spawn_egg", () -> new ForgeSpawnEggItem(HamstersEntityType.HAMSTER, 16747824, 16775119, new Item.Properties().tab(Hamsters.hamsterCreativeTab)));
    // public static final RegistryObject<Item> TUNNEL = ITEMS.register("tunnel", new BlockItem () ->(HamstersBlocks.TUNNEL, new FabricItemSettings()));

    public static final RegistryObject<Item> HAMSTER = ITEMS.register("hamster", () -> new HamsterItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HAMSTER_WHEEL = ITEMS.register("hamster_wheel", () -> new HamsterWheelItem(HamstersBlocks.HAMSTER_WHEEL.get(), new Item.Properties().tab(Hamsters.hamsterCreativeTab)));

    //public static Supplier<Item> registerCaughtMobItem(String name, EntityType entitySupplier, Supplier<? extends Fluid> fluidSupplier, SoundEvent soundSupplier, int variantAmount) {
    //    return registerItem(name, () ->  new HamsterItem(entitySupplier, fluidSupplier.get(), soundSupplier, variantAmount, new Item.Properties().stacksTo(1)));
    //}
}
