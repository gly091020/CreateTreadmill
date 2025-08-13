package com.gly091020.CreateTreadmill.renderer;

import com.gly091020.CreateTreadmill.Part;
import com.gly091020.CreateTreadmill.block.TreadmillBlock;
import com.gly091020.CreateTreadmill.block.TreadmillBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TreadmillRenderer extends KineticBlockEntityRenderer<TreadmillBlockEntity> {
    public TreadmillRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(TreadmillBlockEntity be, BlockState state) {
        return CachedBuffers.block(KineticBlockEntityRenderer.KINETIC_BLOCK,
                KineticBlockEntityRenderer.shaft(KineticBlockEntityRenderer.getRotationAxisOf(be)));
    }

    @Override
    public boolean shouldRender(TreadmillBlockEntity blockEntity, Vec3 cameraPos) {
        return super.shouldRender(blockEntity, cameraPos) && blockEntity.getBlockState().getValue(TreadmillBlock.PART) == Part.BOTTOM_FRONT;
    }
}
