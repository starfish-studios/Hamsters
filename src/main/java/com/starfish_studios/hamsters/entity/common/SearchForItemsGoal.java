package com.starfish_studios.hamsters.entity.common;

import com.starfish_studios.hamsters.entity.Hamster;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumSet;
import java.util.List;

public class SearchForItemsGoal extends Goal {
    private final Hamster mob;
    private final double speedModifier;
    private final double horizontalSearchRange;
    private final double verticalSearchRange;
    private final Ingredient ingredient;

    public SearchForItemsGoal(Hamster mob, double speedModifier, Ingredient ingredient, double horizontalSearchRange, double verticalSearchRange) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.ingredient = ingredient;
        this.horizontalSearchRange = horizontalSearchRange;
        this.verticalSearchRange = verticalSearchRange;
    }

    @Override
    public boolean canUse() {
        if (mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && !mob.isInSittingPose()) {
            List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getItem()));
            return !list.isEmpty() && mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !mob.isInSittingPose();
    }

    @Override
    public void tick() {
        List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getItem()));
        ItemStack itemstack = mob.getItemBySlot(EquipmentSlot.MAINHAND);
        if (itemstack.isEmpty() && !list.isEmpty()) {
            mob.getNavigation().moveTo(list.get(0), speedModifier);
        }

    }

    @Override
    public void start() {
        List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getItem()));
        if (!list.isEmpty()) {
            mob.getNavigation().moveTo(list.get(0), speedModifier);
        }

    }
}
