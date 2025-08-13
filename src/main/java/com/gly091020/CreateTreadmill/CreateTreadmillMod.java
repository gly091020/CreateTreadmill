package com.gly091020.CreateTreadmill;

import com.gly091020.CreateTreadmill.block.TreadmillBlock;
import com.gly091020.CreateTreadmill.block.TreadmillBlockEntity;
import com.gly091020.CreateTreadmill.item.TreadmillItem;
import com.gly091020.CreateTreadmill.maid.MaidPlugin;
import com.gly091020.CreateTreadmill.renderer.TreadmillRenderer;
import com.gly091020.CreateTreadmill.renderer.TreadmillVisual;
import com.mojang.logging.LogUtils;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

@Mod(CreateTreadmillMod.ModID)
public class CreateTreadmillMod {
    public static final String ModID = "createtreadmill";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRIES = CreateRegistrate.create(ModID);

    public static final ItemEntry<TreadmillItem> TREADMILL_ITEM = REGISTRIES
            .item("treadmill_item", TreadmillItem::new)
            .register();

    public static final BlockEntry<TreadmillBlock> TREADMILL_BLOCK = REGISTRIES
            .block("treadmill", TreadmillBlock::new)
            .initialProperties(SharedProperties::stone)
            .onRegister(b -> BlockStressValues.CAPACITIES.register(b, () -> 16d))
            .transform(axeOrPickaxe())
            .register();
    public static final BlockEntityEntry<TreadmillBlockEntity> TREADMILL_ENTITY = REGISTRIES
            .blockEntity("treadmill_entity", TreadmillBlockEntity::new)
            .visual(() -> TreadmillVisual::new)
            .renderer(() -> TreadmillRenderer::new)
            .validBlock(TREADMILL_BLOCK)
            .register();
    public static final PartialModel BELT_MODEL = PartialModel.of(ResourceLocation.fromNamespaceAndPath(ModID, "block/belt"));
    public static final SpriteShiftEntry BELT_SHIFT = SpriteShifter.get(ResourceLocation.fromNamespaceAndPath(ModID, "block/belt"), ResourceLocation.fromNamespaceAndPath(ModID, "block/belt_shift"));

    public CreateTreadmillMod(IEventBus bus) {
        REGISTRIES.registerEventListeners(bus);
        if(ModList.get().isLoaded("touhou_little_maid")){
            MaidPlugin.registryData(bus);
        }
    }
}
