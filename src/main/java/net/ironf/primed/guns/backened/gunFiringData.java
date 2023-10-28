package net.ironf.primed.guns.backened;

import net.ironf.primed.guns.backened.attachements.attachmentTrigger;
import net.ironf.primed.guns.backened.attachements.attachmentTriggerEnum;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.Map;

public class gunFiringData {


    public gunFiringData(EntityType<? extends gunProjectile> entityType, Float Inertia, Float velocityMultiplier, Float damage, Integer PierceLevel, Float Knockback, Float Recoil, Integer split, Float splitImperfection, Float gravity, Float drag, Boolean alwaysApplyFire, Map<attachmentTriggerEnum, ArrayList<attachmentTrigger>> triggers) {
        this.inertia = Inertia;
        this.damage = damage;
        this.pierceLevel = PierceLevel;
        this.knockBack = Knockback;
        this.alwaysApplyFire = alwaysApplyFire;
        this.recoil = Recoil;
        this.split = split;
        this.splitImperfection = splitImperfection;
        this.entityType = entityType;
        this.velocityMultiplier = velocityMultiplier;
        this.drag = drag;
        this.gravity = gravity;
        this.triggers = triggers;
    }

    public gunFiringData(){

    }
    public Float inertia = 0f;
    public Float velocityMultiplier = 0f;
    public Float damage = 0f;
    public Integer pierceLevel = 0;
    public Float knockBack = 0f;
    public Float recoil = 0f;
    public Float splitImperfection = 0f;
    public Boolean alwaysApplyFire;
    public Integer split = 0;

    public float gravity = 0f;
    public float drag = 0f;
    public EntityType<? extends gunProjectile> entityType;

    public Map<attachmentTriggerEnum, ArrayList<attachmentTrigger>> triggers = Map.ofEntries(
            Map.entry(attachmentTriggerEnum.OnFire,blankList),
            Map.entry(attachmentTriggerEnum.OnReload,blankList),
            Map.entry(attachmentTriggerEnum.OnHitBlock,blankList),
            Map.entry(attachmentTriggerEnum.OnHitEntity,blankList),
            Map.entry(attachmentTriggerEnum.OnHitEither,blankList));

    static ArrayList<attachmentTrigger> blankList = new ArrayList<>();

}
