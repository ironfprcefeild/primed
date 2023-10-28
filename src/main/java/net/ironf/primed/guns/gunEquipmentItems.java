package net.ironf.primed.guns;

import net.ironf.primed.Primed;
import net.ironf.primed.guns.backened.equipment.ammoBelt;
import net.ironf.primed.guns.backened.equipment.exoSuit.exoSuitLeggings;
import net.ironf.primed.guns.backened.equipment.exoSuit.normalExoSuitItem;
import net.ironf.primed.guns.backened.gunItem;
import net.ironf.primed.items.creativeModeTabs;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class gunEquipmentItems {


    public static final DeferredRegister<Item> GUN_EQUIPMENT_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Primed.MODID);

    public static final RegistryObject<Item> TEST_EXO_BOOTS = GUN_EQUIPMENT_ITEMS.register("test_exo_boots", () -> new normalExoSuitItem(armorMaterials.BRASSEXOSUIT, EquipmentSlot.FEET,new Item.Properties().tab(creativeModeTabs.PRIMED_TAB).stacksTo(1),Primed.asResource("brass_exosuit_helmet")));
    public static final RegistryObject<Item> TEST_EXO_LEGGINGS = GUN_EQUIPMENT_ITEMS.register("test_exo_leggings", () -> new exoSuitLeggings(armorMaterials.BRASSEXOSUIT,new Item.Properties().tab(creativeModeTabs.PRIMED_TAB).stacksTo(1),Primed.asResource("brass_exosuit_leggings")));
    public static final RegistryObject<Item> TEST_EXO_CHESTPLATE = GUN_EQUIPMENT_ITEMS.register("test_exo_chestplate", () -> new normalExoSuitItem(armorMaterials.BRASSEXOSUIT, EquipmentSlot.CHEST,new Item.Properties().tab(creativeModeTabs.PRIMED_TAB).stacksTo(1),Primed.asResource("brass_exosuit_chestplate")));
    public static final RegistryObject<Item> TEST_EXO_HELMET = GUN_EQUIPMENT_ITEMS.register("test_exo_helmet", () -> new normalExoSuitItem(armorMaterials.BRASSEXOSUIT, EquipmentSlot.HEAD,new Item.Properties().tab(creativeModeTabs.PRIMED_TAB).stacksTo(1),Primed.asResource("brass_exosuit_helmet")));

    public static final RegistryObject<Item> TEST_AMMO_BELT = GUN_EQUIPMENT_ITEMS.register("test_ammo_belt", () -> new ammoBelt(ArmorMaterials.LEATHER,new Item.Properties().tab(creativeModeTabs.PRIMED_TAB).stacksTo(1),Primed.asResource("brass_exosuit_helmet")));


    public static void register(IEventBus eventBus) {
        GUN_EQUIPMENT_ITEMS.register(eventBus);
    }
}

