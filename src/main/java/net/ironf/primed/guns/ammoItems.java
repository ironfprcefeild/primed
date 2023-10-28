package net.ironf.primed.guns;

import net.ironf.primed.Primed;
import net.ironf.primed.guns.backened.equipment.ammoItem;
import net.ironf.primed.guns.backened.gunItem;
import net.ironf.primed.items.creativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ammoItems {

    public static final DeferredRegister<Item> AMMO_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Primed.MODID);


    //Test Ammo
    public static final RegistryObject<Item> TEST_AMMO = AMMO_ITEMS.register("test_ammo", () -> new ammoItem(new Item.Properties().tab(creativeModeTabs.PRIMED_TAB).stacksTo(1),"test_ammo", true));

    public static final RegistryObject<Item> TEST_AMMO_FIRE_RETURN = AMMO_ITEMS.register("test_ammo_fire_return", () -> new ammoItem(new Item.Properties().tab(creativeModeTabs.PRIMED_TAB).stacksTo(64), "test_fire_return", false));
    public static final RegistryObject<Item> TEST_AMMO_RELOAD_RETURN = AMMO_ITEMS.register("test_ammo_reload_return", () -> new ammoItem(new Item.Properties().tab(creativeModeTabs.PRIMED_TAB).stacksTo(64),"test_reload_return",false));

    //Other Test Ammo
    public static final RegistryObject<Item> OTHER_TEST_AMMO = AMMO_ITEMS.register("other_test_ammo", () -> new ammoItem(new Item.Properties().tab(creativeModeTabs.PRIMED_TAB).stacksTo(1),"other_test_ammo", true));

    public static final RegistryObject<Item> OTHER_TEST_AMMO_FIRE_RETURN = AMMO_ITEMS.register("other_test_ammo_fire_return", () -> new ammoItem(new Item.Properties().tab(creativeModeTabs.PRIMED_TAB).stacksTo(64),"other_fire_return", false));
    public static final RegistryObject<Item> OTHER_TEST_AMMO_RELOAD_RETURN = AMMO_ITEMS.register("other_test_ammo_reload_return", () -> new ammoItem(new Item.Properties().tab(creativeModeTabs.PRIMED_TAB).stacksTo(64), "other_reload_return", false));

    public static void register(IEventBus eventBus) {
        AMMO_ITEMS.register(eventBus);
    }

}
