package net.ironf.primed.guns;

import net.ironf.primed.Primed;
import net.ironf.primed.guns.backened.gunProjectile;
import net.ironf.primed.guns.backened.projectileClasses.weirdProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class projectileEntityTypes {


    public static final DeferredRegister<EntityType<?>> PROJECTILE_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Primed.MODID);
    public static final RegistryObject<EntityType<gunProjectile>> TYPICAL_BULLET = PROJECTILE_ENTITY_TYPES.register("typical_bullet",
            () -> EntityType.Builder.of(gunProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F)
                    .clientTrackingRange(10).updateInterval(20).setShouldReceiveVelocityUpdates(true).setUpdateInterval(20).build("typical_bullet"));

    public static final RegistryObject<EntityType<weirdProjectile>> WEIRD_BULLET = PROJECTILE_ENTITY_TYPES.register("weird_bullet",
            () -> EntityType.Builder.of(weirdProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F)
                    .clientTrackingRange(30).updateInterval(20).setShouldReceiveVelocityUpdates(true).setUpdateInterval(20).build("weird_bullet"));
    public static void register(IEventBus eventBus) {
        PROJECTILE_ENTITY_TYPES.register(eventBus);
    }


}
