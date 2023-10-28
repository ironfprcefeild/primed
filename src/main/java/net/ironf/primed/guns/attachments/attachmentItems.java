package net.ironf.primed.guns.attachments;

import net.ironf.primed.Primed;
import net.ironf.primed.guns.backened.attachements.attachmentItem;
import net.ironf.primed.guns.backened.gunFiringData;
import net.ironf.primed.guns.backened.gunItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class attachmentItems {
    public static final DeferredRegister<Item> ATTACHMENT_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Primed.MODID);



    public static void register(IEventBus eventBus) {
        ATTACHMENT_ITEMS.register(eventBus);
    }

}
