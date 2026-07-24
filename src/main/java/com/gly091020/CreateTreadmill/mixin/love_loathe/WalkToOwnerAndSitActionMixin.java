package com.gly091020.CreateTreadmill.mixin.love_loathe;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gly091020.CreateTreadmill.love_loathe.LoveLoatheUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.github.tartaricacid.callresponse.compat.broadcast.actions.WalkToOwnerAndSitAction")
@Pseudo
public class WalkToOwnerAndSitActionMixin {
    @Inject(method = "standUp", at = @At("HEAD"))
    private static void exitTreadmill(EntityMaid maid, CallbackInfo ci){
        LoveLoatheUtil.exitTreadmill(maid);
    }
}
