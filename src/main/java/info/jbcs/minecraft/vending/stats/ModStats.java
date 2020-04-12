package info.jbcs.minecraft.vending.stats;

import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;

public class ModStats {
    public static final ResourceLocation VENDING_MACHINES_USED =
            registerCustom("vending:stat.vending_machines_used",IStatFormatter.DEFAULT);
    public static final ResourceLocation VENDING_MACHINES_OPENED =
            registerCustom("vending:stat.vending_machines_opened",IStatFormatter.DEFAULT);

    private static ResourceLocation registerCustom(String key, IStatFormatter formatter) {
        ResourceLocation resourcelocation = new ResourceLocation(key);
        Registry.register(Registry.CUSTOM_STAT, key, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, formatter);
        return resourcelocation;
    }
}