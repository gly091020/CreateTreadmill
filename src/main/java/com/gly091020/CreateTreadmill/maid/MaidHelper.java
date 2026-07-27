package com.gly091020.CreateTreadmill.maid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;

public class MaidHelper {
    public static int getMaidLevel(Entity maid){
        return ((EntityMaid)(maid)).getFavorabilityManager().getLevel();
    }

    public static boolean isMaid(Entity entity){
        return entity instanceof EntityMaid;
    }

    public static void setMaidHome(Entity entity){
        var maid = ((EntityMaid)(entity));
        maid.setHomeModeEnable(true);
        maid.getSchedulePos().setDimension(maid.level().dimension().location());
        maid.getSchedulePos().setIdlePos(entity.blockPosition());
        maid.getSchedulePos().setWorkPos(entity.blockPosition());
        maid.getSchedulePos().setSleepPos(entity.blockPosition());
    }

    public static void clearMaidHome(Entity entity){
        ((EntityMaid)(entity)).setHomeModeEnable(false);
    }
}
