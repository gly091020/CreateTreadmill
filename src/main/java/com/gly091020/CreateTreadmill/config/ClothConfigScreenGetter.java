package com.gly091020.CreateTreadmill.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntFieldBuilder;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static com.gly091020.CreateTreadmill.CreateTreadmillMod.CONFIG;

public class ClothConfigScreenGetter {
    public static Screen get(Screen parent){
        // bro还是喜欢用cloth config api
        var builder = ConfigBuilder.create();
        builder.setTitle(Component.translatable("config.createtreadmill.title"));
        builder.setParentScreen(parent);
        var entryBuilder = builder.entryBuilder();
        var category = builder.getOrCreateCategory(Component.empty());
        category.addEntry(i(entryBuilder, CONFIG.TREADMILL_STRESS, "stress", 16, 0).build());
        category.addEntry(i(entryBuilder, CONFIG.TREADMILL_BASE_SPEED, "base_speed", 32, 0).build());
        category.addEntry(b(entryBuilder, CONFIG.TREADMILL_DROP_IT, "drop_it").build());
        category.addEntry(b(entryBuilder, CONFIG.TREADMILL_BREAK, "break").build());
        category.addEntry(b(entryBuilder, CONFIG.TREADMILL_SPEED_UP, "speed_up").build());
        return builder.build();
    }

    public static BooleanToggleBuilder b(ConfigEntryBuilder entryBuilder,
                                         ConfigBase.ConfigBool configKey,
                                         String key, boolean defaultValue){
        return entryBuilder.startBooleanToggle(getTransform(key), configKey.get())
                .setDefaultValue(defaultValue)
                .setTooltip(getTransform(key + ".tip"))
                .setSaveConsumer(configKey::set);
    }

    public static BooleanToggleBuilder b(ConfigEntryBuilder entryBuilder,
                                         ConfigBase.ConfigBool configKey,
                                         String key){
        return b(entryBuilder, configKey, key, true);
    }

    public static IntFieldBuilder i(ConfigEntryBuilder entryBuilder,
                                         ConfigBase.ConfigInt configKey,
                                         String key, int defaultValue, int min, int max){
        return entryBuilder.startIntField(getTransform(key), configKey.get())
                .setDefaultValue(defaultValue)
                .setMax(max).setMin(min)
                .setTooltip(getTransform(key + ".tip"))
                .setSaveConsumer(configKey::set);
    }

    public static IntFieldBuilder i(ConfigEntryBuilder entryBuilder,
                                    ConfigBase.ConfigInt configKey,
                                    String key, int defaultValue, int min){
        return i(entryBuilder, configKey, key, defaultValue, min, Integer.MAX_VALUE);
    }

    public static IntFieldBuilder i(ConfigEntryBuilder entryBuilder,
                                    ConfigBase.ConfigInt configKey,
                                    String key, int defaultValue){
        return i(entryBuilder, configKey, key, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static Component getTransform(String name){
        return Component.translatable(String.format("config.createtreadmill.%s", name));
    }
}
