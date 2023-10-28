package net.ironf.primed;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.ironf.primed.guns.*;
import net.ironf.primed.guns.backened.gunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ironf.primed.guns.backened.bulletProjectileRenderer;
import org.slf4j.Logger;
import software.bernie.geckolib3.network.GeckoLibNetwork;

import java.util.Map;
import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Primed.MODID)
public class Primed
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "primed";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static Random rand = new Random();

    //REGISTRATE!
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public Primed()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();



        // Register ourselves for server and other game events we are interested in


        projectileEntityTypes.register(modEventBus);
        gunItems.register(modEventBus);

        ammoItems.register(modEventBus);
        gunEquipmentItems.register(modEventBus);


        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }


    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Thanks for choosing Create: Primed! ");
    }

    @SubscribeEvent
    public void FMLLoadComplete(FMLLoadCompleteEvent event){
    }



    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(projectileEntityTypes.TYPICAL_BULLET.get(), bulletProjectileRenderer::new);
            EntityRenderers.register(projectileEntityTypes.WEIRD_BULLET.get(), bulletProjectileRenderer::new);
            LOGGER.info("Create: Primed has registered projectile renderers");
            ammoData.registerAmmoData();
            LOGGER.info("Create: Primed has registered ammo Data");

            for (RegistryObject<Item> gunItemRegistryObject : gunItems.GUN_ITEMS.getEntries()) {
                if (ammoData.ammoDataMaster.containsKey(((gunItem) gunItemRegistryObject.get()).getAmmoDataTag())) {
                    ((gunItem) gunItemRegistryObject.get()).setAmmoData(ammoData.ammoDataMaster.get(((gunItem) gunItemRegistryObject.get()).getAmmoDataTag()));
                }
            }
            LOGGER.info("Create: Primed has loaded ammo Data into Gun Items");
        }
    }

    public ResourceLocation createRL(String path){
        return new ResourceLocation(MODID,path);
    }
}
