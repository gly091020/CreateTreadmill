package com.gly091020.CreateTreadmill.love_loathe;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.ChatClientInfo;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gly091020.CreateTreadmill.CreateTreadmillMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Collections;

public class LoveLoatheEventHandler {
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if(CreateTreadmillMod.CONFIG.TREADMILL_SAYING.get() == 0)return;
        for(ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            player.level().getEntitiesOfClass(EntityMaid.class, player.getBoundingBox().inflate(32.0F)).forEach((maid) -> {
                if(maid.tickCount % (20 * 60 * CreateTreadmillMod.CONFIG.TREADMILL_SAYING.get()) != 0)return;
                var t = LoveLoatheUtil.getTreadmillMaidOn(maid);
                if(t == null)return;

                MaidAIChatManager manager = maid.getAiChatManager();
                ChatClientInfo info = new ChatClientInfo(
                        player.getName().getString(),
                        "zh_CN",
                        Collections.emptyList()
                );
                manager.chat(LoveLoatheUtil.getTreadmillPromptSuffix(maid), info, player);
            });
        }
    }

    // 不用女仆的事件是因为爱憎分明取消了这个事件
    @SubscribeEvent
    public static void onMaidHurt(LivingDamageEvent.Post event){
        if(CreateTreadmillMod.CONFIG.TREADMILL_SAYING.get() == 0)return;
        if(event.getEntity().level().isClientSide)return;
        if(!(event.getEntity() instanceof EntityMaid maid))return;
        if(!(maid.getOwner() instanceof Player player))return;
        var t = LoveLoatheUtil.getTreadmillMaidOn(maid);
        if(t == null)return;

        MaidAIChatManager manager = maid.getAiChatManager();
        ChatClientInfo info = new ChatClientInfo(
                player.getName().getString(),
                "zh_CN",
                Collections.emptyList()
        );
        manager.chat(LoveLoatheUtil.getTreadmillPromptSuffix(maid) + "但不过你的主人攻击了你尝试加速并成功了，你可以使用“站起来”工具下跑步机（如果有的话）", info, (ServerPlayer) player);
    }
}
