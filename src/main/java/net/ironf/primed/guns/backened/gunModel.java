package net.ironf.primed.guns.backened;

import net.ironf.primed.Primed;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

    public class gunModel extends AnimatedGeoModel<gunItem> {

        public ResourceLocation getModelResource(gunItem object) {
            return object.getModelLocation();
        }

        public ResourceLocation getTextureResource(gunItem object) {
            return object.getTextureLocation();
        }

        public ResourceLocation getAnimationResource(gunItem animatable) {
            return animatable.getAnimationLocation();
        }
    }
