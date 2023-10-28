package net.ironf.primed.guns.backened;

import com.mojang.logging.LogUtils;
import net.ironf.primed.Primed;
import net.ironf.primed.guns.backened.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import static net.minecraft.util.Mth.lerp;

public class kickbackHandler {
    static float kickbackDebt = 0;
    static float pullDownDebt = 0;
    static float kickBackAmount = 0;

    static float kickBackSpeed = 0;
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void beginKickback(Player player, Float apply, Float setkickBackSpeed){
        LOGGER.info("starting kickback (hopefully)");
        kickBackSpeed = setkickBackSpeed;
        kickBackAmount = apply;
        kickbackDebt = kickbackDebt + kickBackAmount;
    }

    private static kickbackHandler instance;

    public static kickbackHandler get(){
        if (instance == null){
            instance = new kickbackHandler();
        }
        return instance;
    }


    //I took some inspiration from Mr Crayfishes gun mod for this method, but Its not exactly the same

    public static void handleKickBack(){
        //LOGGER.info("Handling Kickback");

        if ((kickBackAmount == 0 || kickbackDebt + pullDownDebt < 0.002)){
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if(player == null) {
            return;
        }

        float appliedKickback;
        if (kickbackDebt > 0.001){
            appliedKickback = kickbackDebt * mc.getDeltaFrameTime() * 0.5f;
            player.setXRot(player.getXRot() - appliedKickback);

            pullDownDebt += kickBackSpeed;
            kickbackDebt -= kickBackSpeed;
        } else if (pullDownDebt > 0.001) {
            appliedKickback = pullDownDebt * mc.getDeltaFrameTime() * 0.5f;
            player.setXRot(player.getXRot() + appliedKickback);
            pullDownDebt -= kickBackSpeed;
        }
    }

    /*

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        handleKickBack();
    }

     */




}
