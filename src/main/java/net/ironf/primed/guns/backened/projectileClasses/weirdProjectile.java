package net.ironf.primed.guns.backened.projectileClasses;

import com.mojang.logging.LogUtils;
import net.ironf.primed.guns.backened.gunProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.slf4j.Logger;

public class weirdProjectile extends gunProjectile {
    public weirdProjectile(EntityType<? extends gunProjectile> entityType, Level level) {
        super(entityType, level);
        this.alwaysApplyFire = true;
        this.damage = 5f;
        this.knockBack = 2f;
        this.recoil = 0.2f;
        this.pierceLevel = 2;
        this.drag = 1f;
        this.gravity = 0f;
        this.speed = 0.95f;
    }

    private static final Logger LOGGER = LogUtils.getLogger();


    @Override
    protected void onHitEntity(EntityHitResult ehr) {
        super.onHitEntity(ehr);

    }

    @Override
    public void tick() {

        super.tick();

    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
;
        super.onHitBlock(blockHitResult);
    }


    /*
    @Override
    public boolean getAlwaysApplyFire() {
        return false;
    }

    @Override
    public float getDamage() {
        return 5f;
    }

    @Override
    public Float getKnockBack() {
        return 2f;
    }

    @Override
    public Float getRecoil() {
        return 0.2f;
    }

    @Override
    public Integer getPierceLevel() {
        return 2;
    }

    @Override
    public Float getDrag() {

        return 1f;
    }

    @Override
    public Float getGravity() {
        return 0f;
    }

    @Override
    protected float getInertia() {
        return 0.95f;
    }

    */






}
