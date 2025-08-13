package com.gly091020.CreateTreadmill.item;

import com.gly091020.CreateTreadmill.CreateTreadmillMod;
import com.gly091020.CreateTreadmill.block.TreadmillBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class TreadmillItem extends BlockItem {
    public TreadmillItem(Properties properties) {
        super(CreateTreadmillMod.TREADMILL_BLOCK.get(), properties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        var f = state.getValue(TreadmillBlock.HORIZONTAL_FACING);
        if(!super.canPlace(context, state)){return false;}
        var p1 = BlockPos.ZERO;
        switch (f){
            case NORTH -> {
                p1 = context.getClickedPos().north(1);
            }
            case SOUTH -> {
                p1 = context.getClickedPos().south(1);
            }
            case WEST -> {
                p1 = context.getClickedPos().west(1);
            }
            case EAST -> {
                p1 = context.getClickedPos().east(1);
            }
        }
        var p2 = context.getClickedPos().above(1);
        var p3 = p1.above(1);
        return context.getLevel().getBlockState(p1).canBeReplaced() &&
                context.getLevel().getBlockState(p2).canBeReplaced() &&
                context.getLevel().getBlockState(p3).canBeReplaced();
    }
}
