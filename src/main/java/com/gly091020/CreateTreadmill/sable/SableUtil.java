package com.gly091020.CreateTreadmill.sable;

import com.gly091020.CreateTreadmill.block.TreadmillBlock;
import com.gly091020.CreateTreadmill.block.TreadmillBlockEntity;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class SableUtil {
    public static Vec3 getReallyPosition(Level level, Vec3 pos){
        return SableCompanion.INSTANCE.projectOutOfSubLevel(level, (Position) pos);
    }

    public static boolean isMoving(TreadmillBlockEntity blockEntity) {
        SubLevelAccess subLevelAccess = SableCompanion.INSTANCE.getContaining(blockEntity.getLevel(), blockEntity.getBlockPos());
        if(subLevelAccess == null)return blockEntity.isMoving();
        var pose = subLevelAccess.logicalPose();
        final double min = 0.1;

        if (blockEntity.getOnTreadmillEntity() == null) {
            return false;
        }

        Vector3d movement = new Vector3d(
                blockEntity.getOnTreadmillEntity().getKnownMovement().x,
                0,
                blockEntity.getOnTreadmillEntity().getKnownMovement().z
        );

        if (movement.lengthSquared() < min * min) {
            return false;
        }

        // 局部方向（根据方块朝向定义“前进轴”）
        Vector3d localDir = switch (blockEntity.getBlockState().getValue(TreadmillBlock.HORIZONTAL_FACING)) {
            case EAST -> new Vector3d(1, 0, 0);
            case WEST -> new Vector3d(-1, 0, 0);
            case SOUTH -> new Vector3d(0, 0, 1);
            case DOWN, UP -> new Vector3d(0, 0, 0);
            case NORTH -> new Vector3d(0, 0, -1);
        };

        // 转换到世界空间
        pose.orientation().transform(localDir);

        localDir.y = 0;
        localDir.normalize();
        movement.normalize();

        return movement.dot(localDir) > 0;
    }
}
