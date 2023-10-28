package net.ironf.primed.guns.backened;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.foundation.utility.VecHelper;
import net.ironf.primed.Primed;
import net.ironf.primed.guns.backened.attachements.attachmentItem;
import net.ironf.primed.guns.backened.attachements.attachmentTrigger;
import net.ironf.primed.guns.backened.attachements.attachmentTriggerEnum;
import net.ironf.primed.guns.backened.equipment.ammoBelt;
import net.ironf.primed.guns.backened.equipment.ammoItem;
import net.ironf.primed.guns.backened.equipment.exoSuit.exoSuitLeggings;
import net.ironf.primed.guns.backened.equipment.exoSuit.normalExoSuitItem;
import net.ironf.primed.items.creativeModeTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class gunItem extends ProjectileWeaponItem implements IForgeItem, IAnimatable, ISyncable, CustomArmPoseItem, Vanishable {

    //Constructor and Data
    public gunItem(Properties properties, String ammoDataTag, Integer type, Integer fireDelay, Integer chargeUp, Integer reloadDelay, Float kickBackAmount, Integer weightClass, ResourceLocation textureLocation, ResourceLocation modelLocation, ResourceLocation animationLocation, SoundEvent fireSound, SoundEvent reloadSound) {
        super(properties.stacksTo(1).tab(creativeModeTabs.PRIMED_GUN_TAB));
        this.type = type;
        this.fireDelay = fireDelay;
        this.reloadDelay = reloadDelay;
        this.kickBackAmount = kickBackAmount;
        this.kickbackRate = kickBackAmount/fireDelay;
        this.fireSound = fireSound;
        this.reloadSound = reloadSound;
        this.chargeUp = chargeUp;
        this.weightClass = weightClass;
        this.ammoDataTag = ammoDataTag;
        this.textureLocation = textureLocation;
        this.animationLocation = animationLocation;
        this.modelLocation = modelLocation;

        if (GeckoLibNetwork.getSyncable(this.getSyncKey()) == null){
            GeckoLibNetwork.registerSyncable(this);
        }

    }

    public gunItem(Properties properties, String ammoDataTag, Integer type, Integer fireDelay, Integer chargeUp, Integer reloadDelay, Float kickBackAmount, Integer weightClass, String textureLocation, String modelLocation, String animationLocation, SoundEvent fireSound, SoundEvent reloadSound) {
        super(properties.stacksTo(1).tab(creativeModeTabs.PRIMED_GUN_TAB));
        this.type = type;
        this.kickBackAmount = kickBackAmount;
        this.fireSound = fireSound;
        this.reloadDelay = fireDelay;
        this.fireDelay = reloadDelay;
        this.kickbackRate = kickBackAmount/fireDelay;
        this.reloadSound = reloadSound;
        this.chargeUp = chargeUp;
        this.weightClass = weightClass;
        this.ammoDataTag = ammoDataTag;
        this.textureLocation = Primed.asResource(textureLocation);
        this.animationLocation = Primed.asResource(animationLocation);
        this.modelLocation = Primed.asResource(modelLocation);

        if (this.fireDelay + this.reloadDelay == 0) {
            AtomicReference<Boolean> succesful = new AtomicReference<>(true);
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            JsonObject animationData = GsonHelper.parse(resourceManager.getResource(this.animationLocation).get().toString());
            this.reloadDelay = 20 * (int) GsonHelper.getAsFloat(GsonHelper.getAsJsonObject(animationData, "reload"), "animation_length");
            this.fireDelay = 20 * (int) GsonHelper.getAsFloat(GsonHelper.getAsJsonObject(animationData, "fire"), "animation_length");
        }

        if (GeckoLibNetwork.getSyncable(this.getSyncKey()) == null){
            GeckoLibNetwork.registerSyncable(this);
        }


    }
    String ammoDataTag;
    HashMap<Item,clipInstance> ammoData;
    Integer type;
    Integer fireDelay;
    Integer chargeUp;
    Integer reloadDelay;
    Integer weightClass;
    SoundEvent fireSound;
    SoundEvent reloadSound;

    ResourceLocation textureLocation;
    ResourceLocation modelLocation;
    ResourceLocation animationLocation;




    public void setAmmoData(HashMap<Item, clipInstance> ammoData) {
        this.ammoData = ammoData;
        LOGGER.info(ammoData.toString());
    }

    public String getAmmoDataTag() {
        return ammoDataTag;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> ammoData.containsKey(stack.getItem());
    }

    @Override
    public int getDefaultProjectileRange() {
        return 20;
    }


    //Stuff to keep track of


    private static final Logger LOGGER = LogUtils.getLogger();


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if (getEffectiveWeightClass(player) != 4) {
            if (level.isClientSide()) {
                return InteractionResultHolder.fail(itemstack);
            }
            if (InteractionHand.OFF_HAND == hand) {
                return InteractionResultHolder.fail(itemstack);
            }
            if (type == 2) {
                autoUse(level, player);
                return InteractionResultHolder.fail(itemstack);
            }


            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int duration) {

        if (type != 2 && getEffectiveWeightClass((Player) livingEntity) != 4){
            manualOrSemiUse(level,livingEntity);
        }
        super.releaseUsing(stack, level, livingEntity, duration);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return type == 2 ? 0 : 200;
    }

    public boolean getExoSuitStatus(Player player){
        return ((player.getItemBySlot(EquipmentSlot.FEET).getItem()) instanceof normalExoSuitItem
                && (player.getItemBySlot(EquipmentSlot.LEGS).getItem()) instanceof exoSuitLeggings
                && (player.getItemBySlot(EquipmentSlot.CHEST).getItem()) instanceof normalExoSuitItem
                && (player.getItemBySlot(EquipmentSlot.HEAD).getItem()) instanceof normalExoSuitItem);
    }

    public int getEffectiveWeightClass(Player player){
        return getExoSuitStatus(player) ? weightClass - 1 : weightClass;
    }

    public void manualOrSemiUse(Level level, LivingEntity livingEntity){
        if (livingEntity instanceof Player && !level.isClientSide() && getEffectiveWeightClass((Player) livingEntity) != 4){
            Player player = (Player) livingEntity;
            int shotsRemaining = player.getMainHandItem().getOrCreateTag().getInt("shotsRemaining");

            //No Ammo? Then Reload!

            if (shotsRemaining == 0){
                player.getCooldowns().addCooldown(this,Reload(player, true) ? this.reloadDelay : 0);
                return;

            }

            //Manual gun and shot not ready? then make it ready! IS it ready? then fire! Either way return.

            if (this.type == 0){
                if (player.getMainHandItem().getOrCreateTag().getBoolean("shotReady")){
                    preFire(player,level);
                }
                player.getMainHandItem().getOrCreateTag().putBoolean("shotReady", !player.getMainHandItem().getOrCreateTag().getBoolean("shotReady"));

                player.getCooldowns().addCooldown(this,this.fireDelay);
                return;
            }

            //Firing

            preFire(player, level);
        }
    }

    public void autoUse(Level level, LivingEntity livingEntity){
        if (!level.isClientSide && livingEntity instanceof Player && getEffectiveWeightClass((Player) livingEntity) != 4) {

            Player player = (Player) livingEntity;
            int shotsRemaining = player.getMainHandItem().getOrCreateTag().getInt("shotsRemaining");

            //No Ammo? Then Reload!

            if (shotsRemaining == 0) {
                player.getCooldowns().addCooldown(this,Reload(player, true) ? this.reloadDelay : 0);
                return;
            }

            preFire(player, level);
        }
    }

    public void preFire(Player player, Level level){
        ItemStack item = player.getMainHandItem();
        player.getMainHandItem().getOrCreateTag().putInt("anim_state",ANIM_FIRE);
        PlayFireSound(player);

        boolean singleShotCase = player.getOffhandItem().getItem() instanceof ammoItem && this.ammoData.containsKey(item.getItem()) && this.ammoData.get(item.getItem()).clipSize == 1;
        if (singleShotCase) {
            Reload(player, false);
        }


        player.getCooldowns().addCooldown(this, singleShotCase ? this.fireDelay + this.reloadDelay : this.fireDelay);
        if (chargeUp != 0) {
            item.getOrCreateTag().putInt("chargeUpCounter", chargeUp + 1);
        } else {
            Fire(player,level);
        }


    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {

        if (!(entity instanceof Player)){
            super.inventoryTick(stack,level,entity,i,b);
            return;
        }


        Player player = ((Player) entity);

        ItemStack mainHandItem = player.getMainHandItem();



        ItemStack offHandItem = player.getOffhandItem();


        int chargeUpCounter = stack.getOrCreateTag().getInt("chargeUpCounter");
        if (mainHandItem == stack) {
            if (offHandItem.isEmpty() || weightClass <= 1) {
                if (chargeUpCounter == 1) {
                    stack.getOrCreateTag().putInt("chargeUpCounter", 0);
                    Fire(player, level);
                } else if (chargeUpCounter > 1) {
                    stack.getOrCreateTag().putInt("chargeUpCounter", chargeUpCounter - 1);
                }



                if (getEffectiveWeightClass(player) > 2) {
                    MobEffectInstance slowDownEffect = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,getEffectiveWeightClass(player),5);
                    slowDownEffect.applyEffect((LivingEntity) entity);
                }
            }
            if (!player.getCooldowns().isOnCooldown(this)){
                stack.getOrCreateTag().putInt("anim_state",ANIM_IDLE);
            }
        } else {
            stack.getOrCreateTag().putInt("chargeUpCounter", 0);
            stack.getOrCreateTag().putInt("anim_state",ANIM_IDLE);
            cancelSounds();
        }

        if (level.isClientSide()){
            kickbackHandler.handleKickBack();
            return;
        }

        PlayAnimation(player);

        super.inventoryTick(stack, level, entity, i, b);
    }


    float kickbackRate;
    float kickBackAmount;

    public void Fire(Player player, Level level) {
        if (level.isClientSide()){
            kickbackHandler.beginKickback(player,this.kickBackAmount,this.kickbackRate);
            return;
        }

        ItemStack mainHandITem = player.getMainHandItem();

        LOGGER.info("Firing");


        ammoItem ammo = (ammoItem) Item.byId(mainHandITem.getOrCreateTag().getInt("clipItem"));

        if (ammo == null) {
            LOGGER.info("Ammo Item is null");
            return;
        }
        clipInstance currentClip = ammoData.get(Item.byId(mainHandITem.getOrCreateTag().getInt("clipItem")));
        LOGGER.info(ammo instanceof ammoItem ? "ammo seems good" : "ammo is stinky and not a real ammo item");

        callAttachments(attachmentTriggerEnum.OnReload,mainHandITem,currentClip,player,null);
        gunFiringData firingData = currentClip.firingData;

        if (currentClip.fireReturn != null) {
            player.getInventory().add(new ItemStack(currentClip.fireReturn,1));
        }


        int shotsRemaining = Math.max(mainHandITem.getOrCreateTag().getInt("shotsRemaining") - currentClip.firingData.split, 0);


        if (shotsRemaining == 0 && currentClip.reloadReturn != null) {
            player.getInventory().add(new ItemStack(currentClip.reloadReturn,1));
        }

        //Modify GunFiring data with attachments
        int preminor1 = mainHandITem.getOrCreateTag().getInt("minorAttachment1");
        int preminor2 = mainHandITem.getOrCreateTag().getInt("minorAttachment2");
        int premajor = mainHandITem.getOrCreateTag().getInt("majorAttachment");
        gunFiringData minor1 = preminor1 == 0 ? new gunFiringData() : ((attachmentItem) Item.byId(mainHandITem.getOrCreateTag().getInt("minorAttachment1"))).numericDataModifier;
        gunFiringData minor2 = preminor2 == 0 ? new gunFiringData() : ((attachmentItem) Item.byId(mainHandITem.getOrCreateTag().getInt("minorAttachment2"))).numericDataModifier;
        gunFiringData major = premajor == 0 ? new gunFiringData()   : ((attachmentItem) Item.byId(mainHandITem.getOrCreateTag().getInt("majorAttachment"))).numericDataModifier;


        float inertia = firingData.inertia + minor1.inertia + minor2.inertia + major.inertia;
        float damage = firingData.damage + minor1.damage + minor2.damage + major.damage;
        float knockBack = firingData.knockBack + minor1.knockBack + minor2.knockBack + major.knockBack;
        float recoil = firingData.recoil + minor1.recoil + minor2.recoil + major.recoil;
        float gravity = firingData.gravity + minor1.gravity + minor2.gravity + major.gravity;
        float drag = firingData.drag + minor1.drag + minor2.drag + major.drag;
        float velocityMultiplier = firingData.velocityMultiplier + minor1.velocityMultiplier + minor2.velocityMultiplier + major.velocityMultiplier;
        float split = firingData.split + minor1.split + minor2.split + major.split;
        float splitImperfection = firingData.splitImperfection + minor1.splitImperfection + minor2.splitImperfection + major.splitImperfection;
        int peirceLevel = firingData.pierceLevel + minor1.pierceLevel + minor2.pierceLevel + major.pierceLevel;


        ///Actually Shoot

        Vec3 lookVec = player.getLookAngle();
        Vec3 motion = lookVec
                .normalize()
                .scale(2)
                .scale(velocityMultiplier);

        Vec3 sprayBase = VecHelper.rotate(new Vec3(0, 0.3, 0), 360, Direction.Axis.Z);
        float sprayChange = 360f / split;


        //Split and create Projectiles
        for (int i = 0; i < split; i++) {

            gunProjectile projectile = new gunProjectile(currentClip.firingData.entityType, level);

            Vec3 splitMotion = motion;
            if (split > 1) {
                float imperfection = splitImperfection * (Primed.rand.nextFloat(1f) - 0.5f);
                Vec3 sprayOffset = VecHelper.rotate(sprayBase, i * sprayChange + imperfection, Direction.Axis.Z);
                splitMotion = splitMotion.add(VecHelper.lookAt(sprayOffset, motion));
            }


            projectile.setInertia(inertia);
            projectile.setDamage(damage);
            projectile.setPierceLevel(peirceLevel);
            projectile.setKnockBack(knockBack);
            projectile.setRecoil(recoil);
            projectile.setAlwaysApplyFire(firingData.alwaysApplyFire);
            projectile.setGravity(gravity);
            projectile.setDrag(drag);

            //TODO this doesn't work make it work
            projectile.triggers.get(attachmentTriggerEnum.OnHitBlock)
                    .addAll(firingData.triggers.get(attachmentTriggerEnum.OnHitBlock));
            projectile.triggers.get(attachmentTriggerEnum.OnHitEntity)
                    .addAll(firingData.triggers.get(attachmentTriggerEnum.OnHitEntity));
            projectile.triggers.get(attachmentTriggerEnum.OnHitEither)
                    .addAll(firingData.triggers.get(attachmentTriggerEnum.OnHitEither));

            projectile.triggers.get(attachmentTriggerEnum.OnHitBlock)
                    .addAll(minor1.triggers.get(attachmentTriggerEnum.OnHitBlock));
            projectile.triggers.get(attachmentTriggerEnum.OnHitEntity)
                    .addAll(minor1.triggers.get(attachmentTriggerEnum.OnHitEntity));
            projectile.triggers.get(attachmentTriggerEnum.OnHitEither)
                    .addAll(minor1.triggers.get(attachmentTriggerEnum.OnHitEither));

            projectile.triggers.get(attachmentTriggerEnum.OnHitBlock)
                    .addAll(minor2.triggers.get(attachmentTriggerEnum.OnHitBlock));
            projectile.triggers.get(attachmentTriggerEnum.OnHitEntity)
                    .addAll(minor2.triggers.get(attachmentTriggerEnum.OnHitEntity));
            projectile.triggers.get(attachmentTriggerEnum.OnHitEither)
                    .addAll(minor2.triggers.get(attachmentTriggerEnum.OnHitEither));

            projectile.triggers.get(attachmentTriggerEnum.OnHitBlock)
                    .addAll(major.triggers.get(attachmentTriggerEnum.OnHitBlock));
            projectile.triggers.get(attachmentTriggerEnum.OnHitEntity)
                    .addAll(major.triggers.get(attachmentTriggerEnum.OnHitEntity));
            projectile.triggers.get(attachmentTriggerEnum.OnHitEither)
                    .addAll(major.triggers.get(attachmentTriggerEnum.OnHitEither));


            projectile.setPos(player.getEyePosition());
            projectile.setDeltaMovement(splitMotion);
            projectile.setOwner(player);

            LOGGER.info(projectile.getClass().getName());
            level.addFreshEntity(projectile);
        }




        if (firingData.recoil > 0) {
            Vec3 appliedMotion = lookVec
                    .multiply(-1.0D,-1.0D,-1.0D)
                    .normalize()
                    .scale(firingData.recoil);

            player.push(appliedMotion.x, 0.0D, appliedMotion.z);
        }

        player.getMainHandItem().getOrCreateTag().putInt("shotsRemaining",shotsRemaining);

    }




    public boolean Reload(Player player, boolean playAnim){

        LOGGER.info("Reloading");
        ItemStack ammoBeltItem = player.getItemBySlot(EquipmentSlot.LEGS);
        ammoBeltItem.getItem();
        ItemStack mainHandITem = player.getMainHandItem();

        boolean ammoBeltReload = ammoBeltItem.getItem() instanceof ammoBelt && !player.isCrouching();

        /*
        clipInstance currentClip = ammoBeltReload ?
                ammoBelt.consumeAmmoItemFindClip(ammoData,ammoBeltItem,player)
                : ammoData.get(player.getOffhandItem().getItem().getDescriptionId());

         */

        clipInstance currentClip = ammoData.get(player.getOffhandItem().getItem());

        if (currentClip == null){
            LOGGER.info("Ammo item is null");
            return false;
        }

        if (!ammoBeltReload){
            mainHandITem.getOrCreateTag().putInt("clipItem", Item.getId(player.getOffhandItem().getItem()));

            player.getOffhandItem().setCount(player.getOffhandItem().getCount() - 1);

        }

        mainHandITem.getOrCreateTag().putInt("shotsRemaining", currentClip.clipSize);
        if (playAnim) {
            mainHandITem.getOrCreateTag().putInt("anim_state",ANIM_RELOAD);
            PlayReloadSound(player);
        }
        mainHandITem.getOrCreateTag().putBoolean("shotReady", true);

        callAttachments(attachmentTriggerEnum.OnReload,mainHandITem,currentClip,player,null);


        return true;
    }

    public void callAttachments(attachmentTriggerEnum event,ItemStack thisStack, Object pass, Object otherpass){
        ammoItem ammo = (ammoItem) Item.byId(thisStack.getOrCreateTag().getInt("clipItem"));
        callAttachments(event,thisStack,ammoData.get(ammo),pass,otherpass);
    }

    public void callAttachments(attachmentTriggerEnum event,ItemStack thisStack,clipInstance currentClip, Object pass, Object otherpass){
        ArrayList<attachmentTrigger> triggers = currentClip.firingData.triggers.get(event);
        if (triggers != null) {
            triggers.forEach((t) -> t.execute(pass, otherpass));
        }

        int minorAttachment1 = thisStack.getOrCreateTag().getInt("minorAttachment1");
        if (minorAttachment1 != 0){
            ((attachmentItem) Item.byId(minorAttachment1)).numericDataModifier.triggers.get(event).forEach((t) -> t.execute(pass,otherpass));
        }
        int minorAttachment2 = thisStack.getOrCreateTag().getInt("minorAttachment2");
        if (minorAttachment2 != 0){
            ((attachmentItem) Item.byId(minorAttachment2)).numericDataModifier.triggers.get(event).forEach((t) -> t.execute(pass,otherpass));
        }
        int majorAttachment = thisStack.getOrCreateTag().getInt("majorAttachment");
        if (majorAttachment != 0) {
            ((attachmentItem) Item.byId(majorAttachment)).numericDataModifier.triggers.get(event).forEach((t) -> t.execute(pass,otherpass));
        }

    }

    //Rendering

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(Component.literal("Shots Remaining: " + itemStack.getOrCreateTag().getInt("shotsRemaining")));

        Item clipItem = Item.byId(itemStack.getOrCreateTag().getInt("clipItem"));

        if (!(clipItem == null || clipItem.asItem() == Items.AIR)) {
            components.add(Component.literal("Clip Item: ").append(((ammoItem) clipItem).getAmmoTag()));
        }

        if (this.type == 0 && itemStack.getOrCreateTag().getBoolean("shotReady")){
            components.add(Component.literal("Shot Ready"));
        } else if (this.type == 0){
            components.add(Component.literal("Shot not Ready"));
        }
        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }



    public void PlayFireSound(Player player){
        player.playSound(fireSound, 1.0F, 1.3f);

    }

    public void PlayReloadSound(Player player){
        player.playSound(reloadSound, 1.0F, 1.3f);
    }

    ///KickBack



    public void cancelSounds(){

    }

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public String controllerName = "controller";
    public static final int ANIM_FIRE = 1;
    public static final int ANIM_RELOAD = 2;
    public static final int ANIM_IDLE = 0;


    //Override this to change arm pose if needed

    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        if (!player.swinging) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }
        return null;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }


    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, controllerName, 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    public void PlayAnimation(Player player){
        //LOGGER.info(String.valueOf(player.getMainHandItem().getOrCreateTag().getInt("anim_state")));
        if (player.getMainHandItem().getOrCreateTag().getInt("prev_anim") != player.getMainHandItem().getOrCreateTag().getInt("anim_state")){
            syncAnimation(player,player.getMainHandItem().getOrCreateTag().getInt("anim_state"));
        }
        player.getMainHandItem().getOrCreateTag().putInt("prev_anim",player.getMainHandItem().getOrCreateTag().getInt("anim_state"));

    }


    public void syncAnimation(Player player, int state){
        Level worldIn = player.level;
        if (!worldIn.isClientSide) {
            final int id = GeckoLibUtil.guaranteeIDForStack(player.getMainHandItem(), (ServerLevel) worldIn);
            final PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF
                    .with(() -> player);
            GeckoLibNetwork.syncAnimation(target, this, id, state);
        }
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        if (controller.getAnimationState() == AnimationState.Stopped){
            switch (state) {
                case ANIM_IDLE ->
                        controller.setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
                case ANIM_FIRE ->
                        controller.setAnimation(new AnimationBuilder().addAnimation("fire", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
                case ANIM_RELOAD ->
                        controller.setAnimation(new AnimationBuilder().addAnimation("reload", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            }
            controller.markNeedsReload();
        }
    }



    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new gunRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    public ResourceLocation getAnimationLocation() {
        return animationLocation;
    }
    public ResourceLocation getModelLocation() {
        return modelLocation;
    }
    public ResourceLocation getTextureLocation() {
        return textureLocation;
    }


    //Kickback Handler


}
