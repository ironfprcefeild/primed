package net.ironf.primed.items;

import com.simibubi.create.AllItems;
import net.ironf.primed.guns.ammoItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class creativeModeTabs {
    public static final CreativeModeTab PRIMED_TAB = new CreativeModeTab("primed_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ammoItems.TEST_AMMO.get());
        }
    };

    public static final CreativeModeTab PRIMED_GUN_TAB = new CreativeModeTab("primed_gun_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AllItems.POTATO_CANNON.get());
        }
    };
    public static void init() {
    }
}
