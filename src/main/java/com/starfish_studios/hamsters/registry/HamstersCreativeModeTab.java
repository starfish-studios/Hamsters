package com.starfish_studios.hamsters.registry;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class HamstersCreativeModeTab extends CreativeModeTab {
    public HamstersCreativeModeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(HamstersItems.HAMSTER_SPAWN_EGG.get());
    }

    @Override
    public void fillItemList(NonNullList<ItemStack> itemStacks) {
        ItemStack item0 = new ItemStack(HamstersItems.HAMSTER.get());
        item0.getOrCreateTag().putInt("Variant", 0);
        itemStacks.add(item0);

        ItemStack item1 = new ItemStack(HamstersItems.HAMSTER.get());
        item1.getOrCreateTag().putInt("Variant", 1);
        itemStacks.add(item1);

        ItemStack item2 = new ItemStack(HamstersItems.HAMSTER.get());
        item2.getOrCreateTag().putInt("Variant", 2);
        itemStacks.add(item2);

        ItemStack item3 = new ItemStack(HamstersItems.HAMSTER.get());
        item3.getOrCreateTag().putInt("Variant", 3);
        itemStacks.add(item3);

        ItemStack item4 = new ItemStack(HamstersItems.HAMSTER.get());
        item4.getOrCreateTag().putInt("Variant", 4);
        itemStacks.add(item4);

        ItemStack item5 = new ItemStack(HamstersItems.HAMSTER.get());
        item5.getOrCreateTag().putInt("Variant", 5);
        itemStacks.add(item5);

        ItemStack item6 = new ItemStack(HamstersItems.HAMSTER.get());
        item6.getOrCreateTag().putInt("Variant", 6);
        itemStacks.add(item6);

        super.fillItemList(itemStacks);
        /*
        for (Item item : ForgeRegistries.ITEMS) {
            if (item != null) {
                if (ForgeRegistries.ITEMS.getKey(item).getNamespace().equals("hamsters")) {
                    item.fillItemCategory(this, itemStacks);
                }
            }
        }
         */
    }
}
