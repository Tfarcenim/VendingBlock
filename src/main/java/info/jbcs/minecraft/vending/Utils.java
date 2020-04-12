package info.jbcs.minecraft.vending;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Utils {
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    private static HashMap<String, ResourceLocation> resources = new HashMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    // http://stackoverflow.com/questions/4753251/how-to-go-about-formatting-1200-to-1-2k-in-java
    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static void bind(String textureName) {
        ResourceLocation res = resources.get(textureName);

        if (res == null) {
            res = new ResourceLocation(textureName);
            resources.put(textureName, res);
        }

        Minecraft.getInstance().getTextureManager().bindTexture(res);
    }

    public static void markBlockForUpdate(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, blockState, blockState, 3);
    }

    public static void throwAtPlayer(PlayerEntity player, Direction face, ItemEntity item,BlockPos vendingPos){

			Vec3d pos = new Vec3d(vendingPos.getX()+.5,vendingPos.getY()+.5,vendingPos.getZ()+.5);

			switch (face) {
				case WEST: pos = pos.add(-.7,0,0);break;
				case EAST: pos = pos.add(.7,0,0);break;
				case NORTH: pos = pos.add(0,0 ,- .7);break;
				case SOUTH: pos = pos.add(0,0, .7);break;
				case UP: pos = pos.add(0,.7,0);break;
				case DOWN: pos = pos.add(0,- .7,0);break;
			}

			item.setPositionAndUpdate(pos.x,pos.y,pos.z);
			item.setMotion(player.getPositionVec().add(0,1,0).subtract(pos).mul(.6,.6,.6));
			player.world.addEntity(item);
		}
}
