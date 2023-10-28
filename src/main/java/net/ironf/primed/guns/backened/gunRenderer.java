package net.ironf.primed.guns.backened;

import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class gunRenderer extends GeoItemRenderer<gunItem> {
    public gunRenderer() {
        super(new gunModel());
    }
}
