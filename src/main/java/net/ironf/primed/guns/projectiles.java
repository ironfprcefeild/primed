package net.ironf.primed.guns;

import net.ironf.primed.guns.backened.attachements.attachmentTrigger;
import net.ironf.primed.guns.backened.attachements.attachmentTriggerEnum;
import net.ironf.primed.guns.backened.gunFiringData;
import net.ironf.primed.guns.attachments.attachmentTriggers.sayHello;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class projectiles {
    static ArrayList<attachmentTrigger> blankList = new ArrayList<>();
    static Map<attachmentTriggerEnum,ArrayList<attachmentTrigger>> blankmap = Map.ofEntries(
            Map.entry(attachmentTriggerEnum.OnFire,blankList),
            Map.entry(attachmentTriggerEnum.OnReload,blankList),
            Map.entry(attachmentTriggerEnum.OnHitBlock,blankList),
            Map.entry(attachmentTriggerEnum.OnHitEntity,blankList),
            Map.entry(attachmentTriggerEnum.OnHitEither,blankList)

    );




    public static ArrayList<attachmentTrigger> arrayHelper(attachmentTrigger first, @Nullable attachmentTrigger second, @Nullable attachmentTrigger third){
        ArrayList<attachmentTrigger> toReturn = new ArrayList<>();
        toReturn.add(first);
        if (second != null){
            toReturn.add(second);
        }
        if (third != null){
            toReturn.add(third);
        }
        return toReturn;
    }

    public static gunFiringData testingBullet = new gunFiringData(
            projectileEntityTypes.TYPICAL_BULLET.get(),0.95f,2f,5f,2,0.2f,0.01f,1,20f,0f,1f,false,
            Map.ofEntries(
                    Map.entry(attachmentTriggerEnum.OnFire,arrayHelper(new sayHello(),null,null))));

    public static gunFiringData weirdBullet = new gunFiringData(
            projectileEntityTypes.WEIRD_BULLET.get(),0.95f,0.95f,25f,2,2f,-1.2f,1,20f,0f,1f,false,blankmap);

}
