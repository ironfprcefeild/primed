package net.ironf.primed.guns.backened;

import net.minecraft.world.item.Item;

public class clipInstance {

    public clipInstance(Integer clipSize, gunFiringData projectile, Item fireReturn, Item reloadReturn){
        this.clipSize = clipSize;
        this.firingData = projectile;
        this.reloadReturn = reloadReturn;
        this.fireReturn = fireReturn;
    }


    Item reloadReturn;
    Item fireReturn;
    Integer clipSize;
    gunFiringData firingData;
}
