package info.jbcs.minecraft.vending.init;

import info.jbcs.minecraft.vending.block.VendingMachineBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class VendingBlocks {
    public static final Block STONE_VENDING_MACHINE;
    public static final Block INFINITE_STONE_VENDING_MACHINE;

    static {
        Block.Properties vend = Block.Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.3F, 6000000.0F);
        STONE_VENDING_MACHINE = new VendingMachineBlock(vend,false).setRegistryName("stone_vending_machine");
        INFINITE_STONE_VENDING_MACHINE = new VendingMachineBlock(vend,true).setRegistryName("infinite_stone_vending_machine");

    }
}
