package com.gly091020.CreateTreadmill;

import com.gly091020.CreateTreadmill.config.ClothConfigScreenGetter;
import com.gly091020.CreateTreadmill.maid.MaidPlugin;
import com.gly091020.CreateTreadmill.ponder.TreadmillPonderPlugin;
import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import static com.gly091020.CreateTreadmill.CreateTreadmillMod.ModID;

@SuppressWarnings("all")
@Mod(value = ModID, dist = Dist.CLIENT)
public class TreadmillModClient {
    public TreadmillModClient(ModContainer container){
        PonderIndex.addPlugin(new TreadmillPonderPlugin());
        registryScreen(container);
    }

    @EventBusSubscriber(modid = ModID)
    public static class EventHandler{
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            if(ModList.get().isLoaded("touhou_little_maid")){
                event.enqueueWork(() -> {
                    ItemBlockRenderTypes.setRenderLayer(MaidPlugin.MAID_MOTOR_BLOCK.get(),
                            RenderType.translucent());
                });
            }
        }
    }

    public static void registryScreen(ModContainer container){
        container.registerExtensionPoint(IConfigScreenFactory.class, (mc, parent) -> {
            if(ModList.get().isLoaded("cloth_config")){
                return ClothConfigScreenGetter.get(parent);
            }
            return new BaseConfigScreen(parent, ModID);
        });
    }
}
