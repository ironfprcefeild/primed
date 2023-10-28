package net.ironf.primed.guns.backened;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.ironf.primed.guns.backened.attachements.attachmentItem;
import net.ironf.primed.guns.backened.attachements.attachmentTrigger;
import net.ironf.primed.guns.backened.attachements.attachmentTriggerEnum;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class gunProjectile extends AbstractHurtingProjectile {



    public gunProjectile(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
        this.alwaysApplyFire = false;
        this.damage = 0f;
        this.knockBack = 0f;
        this.recoil = 0f;
        this.pierceLevel = 0;
        this.drag = 1f;
        this.gravity = 0f;
        this.speed = 0.95f;
    }

    public Float speed;


    @Override
    protected float getInertia() {
        return speed;
    }

    public void setInertia(float i){
        this.speed = i;
    }

    public Float damage;

    public float getDamage(){
        return this.damage;
    }

    public void setDamage(float i){
        this.damage = i;
    }
    public Integer pierceLevel;
    public Integer getPierceLevel() {
        return this.pierceLevel;
    }
    public void setPierceLevel(int i){
        this.pierceLevel = i;
    }
    public Float knockBack;
    public Float getKnockBack() {
        return this.knockBack;
    }
    public void setKnockBack(float i){
        this.knockBack = i;
    }
    public Float recoil;
    public Float getRecoil() {
        return this.recoil;
    }
    public void setRecoil(float i){
        this.recoil = i;
    }
    public Integer split;
    public Integer getSplit() {
        return this.split;
    }
    public void setSplit(int i){
        this.split = i;
    }
    public Float splitImperfection;
    public Float getSplitImperfection() {
        return this.splitImperfection;
    }
    public void setSplitImperfection(float i){
        this.splitImperfection = i;
    }

    //TODO test removing the set gravity and drags
    public Float gravity;
    public Float getGravity() {
        return this.gravity;
    }
    public void setGravity(float i){
        this.gravity = i;
    }

    public Float drag;
    public Float getDrag() {
        return this.drag;
    }
    public void setDrag(float i){
        this.drag = i;
    }
    public Boolean alwaysApplyFire;

    public boolean getAlwaysApplyFire(){
        return this.alwaysApplyFire;
    }

    public void setAlwaysApplyFire(boolean i){
        this.alwaysApplyFire = i;
    }

    @Override
    protected boolean shouldBurn() {
        return true;
    }

    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;
    @Nullable
    private List<Entity> piercedAndKilledEntities;

    public void setTriggers(Map<attachmentTriggerEnum, ArrayList<attachmentTrigger>> triggers) {
        this.triggers = triggers;
    }

    public Map<attachmentTriggerEnum, ArrayList<attachmentTrigger>> triggers =  Map.ofEntries(
            Map.entry(attachmentTriggerEnum.OnFire,blankList),
            Map.entry(attachmentTriggerEnum.OnReload,blankList),
            Map.entry(attachmentTriggerEnum.OnHitBlock,blankList),
            Map.entry(attachmentTriggerEnum.OnHitEntity,blankList),
            Map.entry(attachmentTriggerEnum.OnHitEither,blankList));
    static ArrayList<attachmentTrigger> blankList = new ArrayList<>();



    //Override this and include the super to do entity hit effects
    @Override
    protected void onHitEntity(EntityHitResult ray) {
        super.onHitEntity(ray);

        Vec3 hit = ray.getLocation();
        Entity target = ray.getEntity();
        Entity owner = this.getOwner();

        if (this.getPierceLevel() > 0) {
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            this.piercingIgnoreEntityIds.add(target.getId());
        }

        if (!target.isAlive())
            return;

        if (owner instanceof LivingEntity)
            ((LivingEntity) owner).setLastHurtMob(target);



        if (target instanceof WitherBoss && ((WitherBoss) target).isPowered())
            return;


        boolean targetIsEnderman = target.getType() == EntityType.ENDERMAN;
        int k = target.getRemainingFireTicks();
        if ((this.getAlwaysApplyFire()) && !targetIsEnderman)
            target.setSecondsOnFire(5);

        boolean onServer = !level.isClientSide;

        if (onServer && !target.hurt(causeGunDamage(), this.getDamage())) {
            target.setRemainingFireTicks(k);
            kill();
            return;
        }

        if (targetIsEnderman)
            return;

        if (!(target instanceof LivingEntity)) {
            playHitSound(level, position());
            kill();
            return;
        }

        LivingEntity livingentity = (LivingEntity) target;

        if (onServer && this.getKnockBack() > 0) {
            Vec3 appliedMotion = this.getDeltaMovement()
                    .multiply(1.0D, 0.0D, 1.0D)
                    .normalize()
                    .scale(this.getKnockBack() * 0.6);
            if (appliedMotion.lengthSqr() > 0.0D)
                livingentity.push(appliedMotion.x, 0.1D, appliedMotion.z);
        }



        doPostHurtEffects((LivingEntity) target);

        if (livingentity != owner && livingentity instanceof Player && owner instanceof ServerPlayer
                && !this.isSilent()) {
            ((ServerPlayer) owner).connection
                    .send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
        }

        callAttachments(attachmentTriggerEnum.OnHitEntity,target);
        callAttachments(attachmentTriggerEnum.OnHitEither,target);


        if (this.getPierceLevel() == 0){
            kill();
            return;
        }

        if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
            kill();
        }

    }

    public void callAttachments(attachmentTriggerEnum event, Object pass){
        this.triggers.get(event).forEach((t) -> t.execute(pass,this));
    }
    private void playHitSound(Level level, Vec3 position) {
    }

    //Override this and do not include the super to do block hit effects, if returns true projectile will be destroyed
    public boolean onBlockCollision(BlockHitResult bhr){
        return true;
    }


    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        //TODO make it break glass
        if (onBlockCollision(blockHitResult)){
            kill();
        }
    }

    //Override this to do PostHurt Effects

    protected void doPostHurtEffects(LivingEntity livingEntity) {

    }
    //Override this to add particle effects to the firingData, triggered every tick
    public void renderParticles(){

    }

    //Override this and include the super to apply effects every tick.
    @Override
    public void tick() {
        Entity entity = this.getOwner();

        if(this.level.isClientSide || (entity == null || !entity.isRemoved()) && this.level.hasChunkAt(this.blockPosition())){
            renderParticles();
            setDeltaMovement(getDeltaMovement().add(0, -0.05 * this.getGravity(), 0).scale(this.getDrag()));
            this.xPower = 0;
            this.yPower = 0;
            this.zPower = 0;
            super.tick();

        } else {
            kill();
        }
    }
    private DamageSource causeGunDamage() {
        return new gunProjectile.gunDamageSource(this, getOwner()).setProjectile();
    }

    public static class gunDamageSource extends IndirectEntityDamageSource {

        public gunDamageSource(Entity source, @Nullable Entity trueSource) {
            super("primed.gun", source, trueSource);
        }

    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return super.getTrailParticle();
    }

    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<gunProjectile> entityBuilder = (EntityType.Builder<gunProjectile>) builder;
        return entityBuilder.sized(.25f, .25f);
    }
}
