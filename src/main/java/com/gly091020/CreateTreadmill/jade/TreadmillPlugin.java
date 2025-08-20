package com.gly091020.CreateTreadmill.jade;

import com.gly091020.CreateTreadmill.block.TreadmillBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class TreadmillPlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new TreadmillProvider(), TreadmillBlock.class);
    }
}
