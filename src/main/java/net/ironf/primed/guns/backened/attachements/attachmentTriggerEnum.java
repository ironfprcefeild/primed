package net.ironf.primed.guns.backened.attachements;

public enum attachmentTriggerEnum {

    //Pass is hit entity, second is projecitle entity
    OnHitEntity,

    //Pass is hit block, second is projecitle entity
    OnHitBlock,

    //Pass is either block or entity, second is projecitle entity
    OnHitEither,

    //pass is firing player
    OnFire,

    //pass is firing player
    OnReload


}
