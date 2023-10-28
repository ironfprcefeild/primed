package net.ironf.primed.guns;

import com.mojang.datafixers.TypeRewriteRule;
import com.simibubi.create.AllItems;
import com.tterrag.registrate.util.entry.ItemEntry;
import it.unimi.dsi.fastutil.Hash;
import net.ironf.primed.Primed;
import net.ironf.primed.guns.backened.clipInstance;
import net.ironf.primed.guns.backened.gunItem;
import net.ironf.primed.items.creativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

import static com.simibubi.create.Create.REGISTRATE;

public class gunItems {

    public static final DeferredRegister<Item> GUN_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Primed.MODID);




    public static final RegistryObject<Item> MANUAL_TEST_GUN = GUN_ITEMS.register("manual_test_gun", () -> new gunItem(new Item.Properties(),"testAmmoData",0,125,100,90,2f,2,"textures/quantrite_heavy_railgun.png","geo/quantrite_heavy_railgun.geo.json","animations/quantrite_heavy_railgun.animation.json",null,null));

    public static final RegistryObject<Item> SEMIAUTO_TEST_GUN = GUN_ITEMS.register("semi_test_gun", () -> new gunItem(new Item.Properties(),"testAmmoData",1,5,0,90,5f,2,"","","",null,null));

    public static final RegistryObject<Item> AUTO_TEST_GUN = GUN_ITEMS.register("auto_test_gun", () -> new gunItem(new Item.Properties(),"testAmmoData",2,3,1,25,0.01f,4,"","","",null,null));


    public static void register(IEventBus eventBus) {
        GUN_ITEMS.register(eventBus);
    }


}
