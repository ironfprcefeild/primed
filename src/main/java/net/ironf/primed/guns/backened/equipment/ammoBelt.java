package net.ironf.primed.guns.backened.equipment;

import com.simibubi.create.content.equipment.armor.BaseArmorItem;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlockEntity;
import net.ironf.primed.guns.backened.clipInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;
import java.util.Map;

public class ammoBelt extends BaseArmorItem {
    public ammoBelt(ArmorMaterial armorMaterial, Properties properties, ResourceLocation textureLoc) {
        super(armorMaterial, EquipmentSlot.LEGS, properties, textureLoc);
    }


    public static void addAmmoItem(ItemStack toAdd, ItemStack ammoBeltStack){
        ItemStackHandler ammoInventory = new ItemStackHandler(5);
        ammoInventory.deserializeNBT(ammoBeltStack.getOrCreateTag().get("inventory") != null ?(CompoundTag) ammoBeltStack.getOrCreateTag().get("inventory") : new CompoundTag());
        for (int i = 0; i <= ammoInventory.getSlots(); i++){
            if (ammoInventory.isItemValid(i,toAdd)){
                ammoInventory.insertItem(i,toAdd,false);
                ammoBeltStack.getOrCreateTag().put("inventory",ammoInventory.serializeNBT());
                return;
            }
        }
    }

    public static clipInstance consumeAmmoItemFindClip(Map<String, clipInstance> ammoData, ItemStack ammoBeltStack, Player player){
        ItemStackHandler ammoInventory = new ItemStackHandler(5);
        ammoInventory.deserializeNBT((CompoundTag) ammoBeltStack.getOrCreateTag().get("inventory"));
        String ammoTag = "";
        for (int i = 0; i <= ammoInventory.getSlots(); i++){
            ammoTag = ((ammoItem) ammoInventory.getStackInSlot(i).getItem()).getAmmoTag();
            if (ammoData.containsKey(ammoTag)){
                clipInstance toReturn = ammoData.get(ammoTag);
                ammoInventory.extractItem(i,1,false);
                ammoBeltStack.getOrCreateTag().put("inventory",ammoInventory.serializeNBT());
                player.getMainHandItem().getOrCreateTag().putInt("clipItem",Item.getId(ammoInventory.getStackInSlot(i).getItem()));
                return toReturn;
            }
        }
        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        if (!player.isCrouching()){
            return super.use(level,player,interactionHand);
        }

        ItemStack ammoBeltStack = player.getItemInHand(interactionHand);
        ItemStackHandler ammoInventory = new ItemStackHandler(5);
        ammoInventory.deserializeNBT((CompoundTag) ammoBeltStack.getOrCreateTag().get("inventory"));

        for (int i = 0; i <= ammoInventory.getSlots(); i++){
            if (!ammoInventory.getStackInSlot(i).isEmpty()){
                player.getInventory().add(ammoInventory.getStackInSlot(i));
                ammoInventory.setStackInSlot(i,ItemStack.EMPTY);
                ammoBeltStack.getOrCreateTag().put("inventory",ammoInventory.serializeNBT());
                return InteractionResultHolder.sidedSuccess(ammoBeltStack, level.isClientSide());
            }
        }
        return InteractionResultHolder.fail(ammoBeltStack);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean b) {
        super.inventoryTick(itemStack, level, entity, i, b);
        if (!level.isClientSide() && !itemStack.getOrCreateTag().getBoolean("firstTick")){
            ItemStackHandler ammoInventory = new ItemStackHandler(5);
            itemStack.getOrCreateTag().put("inventory",ammoInventory.serializeNBT());
            itemStack.getOrCreateTag().putBoolean("firstTick", !itemStack.getOrCreateTag().getBoolean("firstTick"));
        }
    }
}
