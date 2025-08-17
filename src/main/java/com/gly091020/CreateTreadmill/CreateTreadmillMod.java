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
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

@Mod(CreateTreadmillMod.ModID)
public class CreateTreadmillMod {
    public static final String ModID = "createtreadmill";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRIES = CreateRegistrate.create(ModID);

    public static final ItemEntry<TreadmillItem> TREADMILL_ITEM = REGISTRIES
            .item("treadmill", TreadmillItem::new)
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

    public static final Map<Integer, LivingEntity> WALKING_ENTITY = new HashMap<>();

    public static final UUID _5112151111121 = UUID.fromString("91bd580f-5f17-4e30-872f-2e480dd9a220");
    public static final UUID N44 = UUID.fromString("5a33e9b0-35bc-44ed-9b4e-03e3e180a3d2");

    public CreateTreadmillMod(IEventBus bus) {
        REGISTRIES.registerEventListeners(bus);
        if(ModList.get().isLoaded("touhou_little_maid")){
            MaidPlugin.registryData(bus);
        }
    }

    public static boolean isCreator(){
        return Objects.equals(Minecraft.getInstance().getGameProfile().getId(), N44) || Objects.equals(Minecraft.getInstance().getGameProfile().getId(), _5112151111121);
    }

    @EventBusSubscriber
    public static class HandleEvent{
        @SubscribeEvent
        public static void onRenderEntity(RenderLivingEvent.Pre event){
            if(WALKING_ENTITY.containsKey(event.getEntity().getId()) && !(event.getEntity() instanceof Player)) {
                var speed = 1;
                var entity = TreadmillBlockEntity.getBlockEntityByEntity(event.getEntity());
                if(entity != null && Math.abs(entity.getSpeed()) > entity.getSettingSpeed()){
                    speed = (int) (Math.abs(entity.getSpeed()) / 32);
                }
                event.getEntity().walkAnimation.setSpeed(speed);
            }
        }

        @SubscribeEvent
        public static void onEntityDie(LivingDeathEvent deathEvent){
            var entity = deathEvent.getEntity();
            if(entity.level().getBlockState(entity.blockPosition()).is(TREADMILL_BLOCK)){
                var last = entity.getLastAttacker();
                if(last instanceof ServerPlayer player){
                    grantAdvancement(player, ResourceLocation.fromNamespaceAndPath(ModID, "run_to_die"), "0");
                }
            }
        }

        public static void grantAdvancement(ServerPlayer player, ResourceLocation advancementId, String key) {
            ServerAdvancementManager manager = player.server.getAdvancements();
            AdvancementHolder advancement = manager.get(advancementId);

            if (advancement != null) {
                player.getAdvancements().award(advancement, key);
            }
        }
    }
}
