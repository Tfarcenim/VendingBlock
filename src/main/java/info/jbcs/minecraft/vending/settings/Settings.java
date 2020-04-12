package info.jbcs.minecraft.vending.settings;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Settings {
    private boolean use_custom_creative_tab;
    private boolean close_on_sold_out;
    private boolean close_on_partial_sold_out;
    private boolean block_placing_next_to_doors;
    private boolean transfer_to_inventory;
    private int offsetY;

    public static ForgeConfigSpec.DoubleValue rotation_speed;
    public static ForgeConfigSpec.DoubleValue item_size;
    public static ForgeConfigSpec.BooleanValue show_infinite_tag;

    public static final ClientSettings CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final Pair<ClientSettings, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientSettings::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public boolean shouldCloseOnSoldOut() {
        return close_on_sold_out;
    }

    public boolean shouldCloseOnPartialSoldOut() {
        return close_on_partial_sold_out;
    }

    public boolean isPlacingNextToDoorsBlocked() {
        return block_placing_next_to_doors;
    }

    public static class ClientSettings {

        public ClientSettings(ForgeConfigSpec.Builder builder) {
            builder.push("client");
            rotation_speed = builder.defineInRange("rotation_speed",.02f,0,Double.MAX_VALUE);
            item_size = builder.defineInRange("item_size",.5f,0,Double.MAX_VALUE);
            show_infinite_tag = builder.define("show_infinite_tag",true);

        }
    }


}
