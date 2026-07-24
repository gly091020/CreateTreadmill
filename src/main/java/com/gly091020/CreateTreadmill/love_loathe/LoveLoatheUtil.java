package com.gly091020.CreateTreadmill.love_loathe;

import com.github.tartaricacid.callresponse.compat.emotion.EmotionData;
import com.github.tartaricacid.callresponse.compat.hunger.HungerData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gly091020.CreateTreadmill.CreateTreadmillMod;
import com.gly091020.CreateTreadmill.Part;
import com.gly091020.CreateTreadmill.block.TreadmillBlock;
import com.gly091020.CreateTreadmill.block.TreadmillBlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

import static com.github.tartaricacid.callresponse.compat.emotion.EmotionData.getTendency;

public class LoveLoatheUtil {
    private static final Random RANDOM = new Random();
    public static double getHungerEfficiency(EntityMaid maid){
        // 80 两倍
        return 2.0 * Math.exp(-Math.pow(HungerData.get(maid) - 80, 2) / 800.0);
    }

    public static double getHungerEfficiency(LivingEntity entity){
        if(!(entity instanceof EntityMaid entityMaid))return 1;
        return getHungerEfficiency(entityMaid);
    }

    public static void exitTreadmill(EntityMaid maid){
        var t = getTreadmillMaidOn(maid);
        if(t == null)return;
        t.setOnTreadmillEntity(null);
    }

    public static TreadmillBlockEntity getTreadmillMaidOn(EntityMaid maid){
        var pos = maid.blockPosition().below(1);
        var state = maid.level().getBlockState(pos);
        if(!state.is(CreateTreadmillMod.TREADMILL_BLOCK))return null;
        var treadmill = TreadmillBlock.findPart(maid.level(), state, pos, Part.BOTTOM_FRONT);
        if(!(maid.level().getBlockEntity(treadmill) instanceof TreadmillBlockEntity blockEntity))return null;
        return blockEntity;
    }

    @Nullable
    public static String getTreadmillPromptSuffix(EntityMaid maid) {
        UUID playerId;
        if(!(maid.getOwner() instanceof Player player))return null;
        playerId = player.getUUID();
        var hunger = HungerData.get(maid);
        EmotionData.EmotionTendency t = getTendency(maid, playerId);
        EmotionData.EmotionValues v = EmotionData.get(maid, playerId);

        double efficiency = getHungerEfficiency(maid);

        String state;
        if (efficiency > 1.7) {
            state = "轻松状态：你步伐轻快，呼吸平稳，还有余力和主人聊天";
        } else if (efficiency > 1.0) {
            state = "正常状态：你在认真跑步，略微有些气喘，但还能分心说话";
        } else if (efficiency > 0.5) {
            state = "吃力状态：你有些疲惫，说话断断续续，需要喘气";
        } else {
            state = "精疲力尽状态：你非常累，说话很短，甚至夹杂喘息";
        }

        String var10000;
        switch (t.ordinal()) {
            case 0 -> var10000 = "（你正在跑步机上运动，当前饱食度" + hunger + "，效率" + String.format("%.2f", efficiency) +
                    "，信任" + v.trust() + "，恐惧" + v.fear() +
                    "，羁绊状态：你一边轻快地跑着，一边带着亲昵的语气和主人撒娇交流，" + state + "）";

            case 1 -> var10000 = "（你正在跑步机上运动，当前饱食度" + hunger + "，效率" + String.format("%.2f", efficiency) +
                    "，信任" + v.trust() + "，恐惧" + v.fear() +
                    "，友好状态：你愿意一边跑步一边和主人聊天，语气轻松自然，" + state + "）";

            case 2 -> var10000 = "（你正在跑步机上运动，当前饱食度" + hunger + "，效率" + String.format("%.2f", efficiency) +
                    "，信任" + v.trust() + "，恐惧" + v.fear() +
                    "，陌生状态：你保持礼貌，一边跑步一边简短回应主人，语气稍显疏离，" + state + "）";

            case 3 -> var10000 = "（你正在跑步机上运动，当前饱食度" + hunger + "，效率" + String.format("%.2f", efficiency) +
                    "，信任" + v.trust() + "，恐惧" + v.fear() +
                    "，畏惧状态：你有些紧张，小心翼翼地回应主人，说话时带着不安，" + state + "）";

            case 4 -> var10000 = "（你正在跑步机上运动，当前饱食度" + hunger + "，效率" + String.format("%.2f", efficiency) +
                    "，信任" + v.trust() + "，恐惧" + v.fear() +
                    "，恐惧状态：你非常害怕主人，交流时明显拘谨甚至想停止跑步远离，" + state + "）";

            case 5 -> var10000 = "（你正在跑步机上运动，当前饱食度" + hunger + "，效率" + String.format("%.2f", efficiency) +
                    "，信任" + v.trust() + "，恐惧" + v.fear() +
                    "，矛盾状态：你表面正常交流，但内心有些不安和依赖交织，说话偶尔迟疑，" + state + "）";

            case 6 -> var10000 = "（你正在跑步机上运动，当前饱食度" + hunger + "，效率" + String.format("%.2f", efficiency) +
                    "，信任" + v.trust() + "，恐惧" + v.fear() +
                    "，中立状态：你只是完成任务，语气平淡地回应主人，" + state + "）";

            default -> throw new MatchException(null, null);
        }

        return var10000;
    }
}
