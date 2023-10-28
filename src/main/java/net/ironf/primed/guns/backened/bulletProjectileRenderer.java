package net.ironf.primed.guns.backened;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class bulletProjectileRenderer extends EntityRenderer<gunProjectile> {
    public bulletProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(gunProjectile entity, float yaw, float pt, PoseStack ms, MultiBufferSource buffer, int light_) {

    }

    @Override
    public ResourceLocation getTextureLocation(gunProjectile p_114482_) {
        return null;
    }
}
