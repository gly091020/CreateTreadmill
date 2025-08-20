package com.gly091020.CreateTreadmill.maid.treadmill;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gly091020.CreateTreadmill.maid.MaidPlugin;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class MoveToTreadmillBehavior extends MaidMoveToBlockTask {
    public MoveToTreadmillBehavior(float movementSpeed) {
        super(movementSpeed);
    }

    @Override
    protected boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, BlockPos blockPos) {
        return entityMaid.getBrain().isMemoryValue(MaidPlugin.TREADMILL_MEMORY.get(), blockPos);
    }
}
