package com.gly091020.CreateTreadmill.block;

import com.gly091020.CreateTreadmill.CreateTreadmillMod;
import com.gly091020.CreateTreadmill.Part;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

import static com.gly091020.CreateTreadmill.block.TreadmillBlock.PART;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class TreadmillBlockEntity extends GeneratingKineticBlockEntity {
    private LivingEntity onTreadmillEntity;
    private boolean isRunning = false;
    private boolean isRuned = false;
    public TreadmillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setChanged();
    }

    @Override
    public void tick() {
        if(this.getBlockState().getValue(PART) != Part.BOTTOM_FRONT){return;}
        if(onTreadmillEntity != null){
            if(onTreadmillEntity.isRemoved()){
                setOnTreadmillEntity(null);
                return;
            }
            if(onTreadmillEntity instanceof Player && !onTreadmillEntity.position().closerThan(getFixedPos(), 0.5)){
                setOnTreadmillEntity(null);
                return;
            }
            setPos();
            if (onTreadmillEntity instanceof Player player) {
                if (player.isShiftKeyDown() || player.getPose() == Pose.SITTING) {
                    setOnTreadmillEntity(null);
                }
            }else{
                onTreadmillEntity.setDeltaMovement(Vec3.atLowerCornerOf(getBlockState().getValue(HorizontalKineticBlock.HORIZONTAL_FACING).getNormal()).multiply(0.3f, 0, 0.3f));
                onTreadmillEntity.lookAt(EntityAnchorArgument.Anchor.EYES, onTreadmillEntity.getEyePosition().relative(getBlockState().getValue(HorizontalKineticBlock.HORIZONTAL_FACING), 1));
                onTreadmillEntity.setPose(Pose.STANDING);
                lazyTick();
            }
            dropIt();
        }
        super.tick();
    }

    public void setOnTreadmillEntity(@Nullable LivingEntity onTreadmillEntity) {
        if(onTreadmillEntity == null && this.onTreadmillEntity != null){
            this.onTreadmillEntity.setDeltaMovement(Vec3.ZERO);
            CreateTreadmillMod.WALKING_ENTITY.remove(this.onTreadmillEntity.getId());
        }
        if (onTreadmillEntity != null) {
            CreateTreadmillMod.WALKING_ENTITY.put(onTreadmillEntity.getId(), onTreadmillEntity);
        }
        this.onTreadmillEntity = onTreadmillEntity;
        setPos();
        update();
    }

    public Entity getOnTreadmillEntity() {
        return onTreadmillEntity;
    }

    public void setPos(){
        if(onTreadmillEntity != null){
            onTreadmillEntity.setPos(getFixedPos());
            onTreadmillEntity.setOnGround(true);
        }
    }

    public Vec3 getFixedPos(){
        var p = this.getBlockPos().above();
        var y = p.getY() + 5.5 / 16;
        switch (getBlockState().getValue(HORIZONTAL_FACING)){
            case WEST -> {
                return new Vec3(p.getX() + 1, y, p.getZ() + 0.5);
            }
            case EAST -> {
                return new Vec3(p.getX(), y, p.getZ() + 0.5);
            }
            case NORTH -> {
                return new Vec3(p.getX() + 0.5, y, p.getZ() + 1);
            }
            case SOUTH -> {
                return new Vec3(p.getX() + 0.5, y, p.getZ());
            }
        }
        return Vec3.atCenterOf(p);
    }

    @Override
    public void initialize() {
        super.initialize();
        if(this.getBlockState().getValue(PART) != Part.BOTTOM_FRONT){return;}
        updateGeneratedRotation();
        setLazyTickRate(10);
        setChanged();
    }

    @Override
    public void lazyTick() {
        isRunning = isMoving();
        if(isRunning != isRuned){
            isRuned = isRunning;
            update();
        }
        if(isRunning && onTreadmillEntity instanceof Player player){
            player.causeFoodExhaustion(getSettingSpeed() * 0.01f);
        }
    }

    private void update(){
        updateGeneratedRotation();
        setChanged();
    }

    private void dropIt(){
        switch (getBlockState().getValue(TreadmillBlock.HORIZONTAL_FACING)) {
            case NORTH, EAST -> {
                if(getSpeed() < 0){
                    float m = 3f * (Math.abs(getSpeed()) / 256);
                    onTreadmillEntity.setDeltaMovement(Vec3.atLowerCornerOf(getBlockState().getValue(HorizontalKineticBlock.HORIZONTAL_FACING).getNormal()).multiply(m, 0, m));
                    onTreadmillEntity = null;
                }
            }
            case SOUTH, WEST -> {
                if(getSpeed() > 0){
                    float m = 3f * (Math.abs(getSpeed()) / 256);
                    onTreadmillEntity.setDeltaMovement(Vec3.atLowerCornerOf(getBlockState().getValue(HorizontalKineticBlock.HORIZONTAL_FACING).getNormal()).multiply(m, 0, m));
                    onTreadmillEntity = null;
                }
            }
        }
    }

    @Override
    public float getGeneratedSpeed() {
        if (isRunning) {
            switch (getBlockState().getValue(TreadmillBlock.HORIZONTAL_FACING)) {
                case NORTH, EAST -> {
                    return getSettingSpeed();
                }
                case SOUTH, WEST -> {
                    return -getSettingSpeed();
                }
            }
        }
        return 0;
    }

    private float getSettingSpeed(){
        return 32;
    }

    public boolean isMoving(){
        var min = 0.1f;
        if(onTreadmillEntity == null){
            return false;
        }
        switch (getBlockState().getValue(TreadmillBlock.HORIZONTAL_FACING)){
            case EAST -> {
                return onTreadmillEntity.getKnownMovement().x > min;
            }
            case WEST -> {
                return onTreadmillEntity.getKnownMovement().x < -min;
            }
            case SOUTH -> {
                return onTreadmillEntity.getKnownMovement().z > min;
            }
            case NORTH -> {
                return onTreadmillEntity.getKnownMovement().z < -min;
            }
        }
        return false;
    }

    @Override
    protected Block getStressConfigKey() {
        return super.getStressConfigKey();
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
