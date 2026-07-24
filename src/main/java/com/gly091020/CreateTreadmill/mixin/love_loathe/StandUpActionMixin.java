package com.gly091020.CreateTreadmill.mixin.love_loathe;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gly091020.CreateTreadmill.love_loathe.LoveLoatheUtil;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.github.tartaricacid.callresponse.compat.broadcast.actions.StandUpAction")
@Pseudo
public class StandUpActionMixin {
    @Shadow
    private static boolean isSitting(EntityMaid maid) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(method = "execute", at = @At("HEAD"))
    private static void exitTreadmill(EntityMaid maid, ServerPlayer debugPlayer, CallbackInfo ci){
        LoveLoatheUtil.exitTreadmill(maid);
    }
}
