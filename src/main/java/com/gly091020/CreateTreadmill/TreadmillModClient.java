package com.gly091020.CreateTreadmill;

import com.gly091020.CreateTreadmill.ponder.TreadmillPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = CreateTreadmillMod.ModID, dist = Dist.CLIENT)
public class TreadmillModClient {
    public TreadmillModClient(){
        PonderIndex.addPlugin(new TreadmillPonderPlugin());
    }
}
