package com.gly091020.CreateTreadmill.maid;

import com.github.tartaricacid.touhoulittlemaid.api.entity.ai.IExtraMaidBrain;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

import java.util.ArrayList;
import java.util.List;

public class ExtraMaidBrain implements IExtraMaidBrain {
    @Override
    public List<SensorType<? extends Sensor<? super EntityMaid>>> getExtraSensorTypes() {
        var l = new ArrayList<SensorType<? extends Sensor<? super EntityMaid>>>();
        l.add(MaidPlugin.TREADMILL_SENSOR.get());
        return l;
    }

    @Override
    public List<MemoryModuleType<?>> getExtraMemoryTypes() {
        var l = new ArrayList<MemoryModuleType<?>>();
        l.add(MaidPlugin.TREADMILL_MEMORY.get());
        return l;
    }
}
