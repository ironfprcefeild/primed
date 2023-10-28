package net.ironf.primed.guns.backened.equipment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

import java.security.SecureRandom;

public class ammoItem extends Item {
    public ammoItem(Properties properties, String ammmoTag, Boolean isAmmoItem) {
        super(properties);
        this.ammoTag = ammmoTag;
        this.isAmmoItem = isAmmoItem;
    }

    String ammoTag = "";

    Boolean isAmmoItem;

    public String getAmmoTag() {
        return ammoTag;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!isAmmoItem){
            return super.use(level,player,interactionHand);
        }
        ItemStack ammoBeltItemStack = player.getItemBySlot(EquipmentSlot.LEGS);

        if (ammoBeltItemStack.getItem() instanceof ammoBelt){
            ItemStack ammoToAdd = player.getMainHandItem();
            ammoBelt.addAmmoItem(ammoToAdd,ammoBeltItemStack);
        }
        return super.use(level, player, interactionHand);
    }
}
