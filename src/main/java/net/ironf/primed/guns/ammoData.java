package net.ironf.primed.guns;

import net.minecraft.world.item.Item;
import net.ironf.primed.guns.backened.clipInstance;

import java.util.HashMap;
import java.util.Map;

public class ammoData {

    public static HashMap<String, HashMap<Item,clipInstance>> ammoDataMaster = new HashMap<>();

    public static void addAmmoDataEntry(String name,Item item, clipInstance clipInstance){
        if (ammoDataMaster.containsKey(name)){
            ammoDataMaster.get(name).put(item,clipInstance);
        } else {
            ammoDataMaster.put(name, new HashMap<>());
            ammoDataMaster.get(name).put(item,clipInstance);
        }
    }

    public static void registerAmmoData(){
        addAmmoDataEntry("testAmmoData",ammoItems.TEST_AMMO.get(),
                new clipInstance(10,projectiles.testingBullet,ammoItems.TEST_AMMO_FIRE_RETURN.get(),ammoItems.TEST_AMMO_RELOAD_RETURN.get()));

        addAmmoDataEntry("testAmmoData",ammoItems.OTHER_TEST_AMMO.get(),
                new clipInstance(50,projectiles.weirdBullet,ammoItems.OTHER_TEST_AMMO_FIRE_RETURN.get(),ammoItems.OTHER_TEST_AMMO_RELOAD_RETURN.get()));
    }

}
