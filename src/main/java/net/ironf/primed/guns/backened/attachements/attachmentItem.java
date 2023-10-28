package net.ironf.primed.guns.backened.attachements;

import net.ironf.primed.guns.backened.gunFiringData;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class attachmentItem extends Item {
    public attachmentItem(Properties properties, gunFiringData numericDataModifier) {
        super(properties);
        this.numericDataModifier = numericDataModifier;
    }

    public gunFiringData numericDataModifier;

    //public Map<attachmentTriggerEnum, ArrayList<attachmentTrigger>> triggers;

}
