package com.gly091020.CreateTreadmill.maid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.ExtraMaidBrainManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.gly091020.CreateTreadmill.CreateTreadmillMod;
import com.gly091020.CreateTreadmill.maid.treadmill.TreadmillSensor;
import com.gly091020.CreateTreadmill.maid.treadmill.UseTreadmillTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

@LittleMaidExtension
public class MaidPlugin implements ILittleMaid {
    public static DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> TREADMILL_MEMORY;
    public static DeferredHolder<SensorType<?>, SensorType<TreadmillSensor>> TREADMILL_SENSOR;
    public static void registryData(IEventBus bus){
        var MEMORY = DeferredRegister.create(BuiltInRegistries.MEMORY_MODULE_TYPE, CreateTreadmillMod.ModID);
        var SENSOR = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPE, CreateTreadmillMod.ModID);
        TREADMILL_MEMORY = MEMORY.register("treadmill_memory", resourceLocation -> new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));
        TREADMILL_SENSOR = SENSOR.register("treadmill_sensor", resourceLocation -> new SensorType<>(TreadmillSensor::new));
        SENSOR.register(bus);
        MEMORY.register(bus);
    }

    @Override
    public void addExtraMaidBrain(ExtraMaidBrainManager manager) {
        manager.addExtraMaidBrain(new ExtraMaidBrain());
    }

    @Override
    public void addMaidTask(TaskManager manager) {
        manager.add(new UseTreadmillTask());
    }
}
