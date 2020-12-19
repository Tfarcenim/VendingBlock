package info.jbcs.minecraft.vending.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum EnumSupports implements IStringSerializable {
    STONE("stone", Blocks.STONE, Blocks.STONE),
    COBBLE_STONE("stonebrick", Blocks.COBBLESTONE, Blocks.COBBLESTONE),
    STONE_BRICK("stonebricksmooth", Blocks.STONE_BRICKS, Blocks.STONE_BRICKS),
    PLANKS("wood", Blocks.OAK_PLANKS, Blocks.OAK_PLANKS),
    CRAFTING_TABLE( "workbench", Blocks.CRAFTING_TABLE, Blocks.CRAFTING_TABLE),
    GRAVEL("gravel", Blocks.GRAVEL, Blocks.GRAVEL),
    NOTEBLOCK("musicblock", Blocks.NOTE_BLOCK, Blocks.NOTE_BLOCK),
    SANDSTONE("sandstone", Blocks.SANDSTONE, Blocks.SANDSTONE),
    GOLD("blockgold", Blocks.GOLD_BLOCK, Items.GOLD_INGOT),
    IRON("blockiron", Blocks.IRON_BLOCK, Items.IRON_INGOT),
    BRICK("brick", Blocks.BRICKS, Blocks.BRICKS),
    COBBLESTONE_MOSSY("stonemoss", Blocks.MOSSY_COBBLESTONE, Blocks.MOSSY_COBBLESTONE),
    OBSIDIAN("obsidian", Blocks.OBSIDIAN, Blocks.OBSIDIAN),
    DIAMOND("blockdiamond", Blocks.DIAMOND_BLOCK, Items.DIAMOND),
    EMERALD( "blockemerald", Blocks.EMERALD_BLOCK, Items.EMERALD),
    LAPIS( "blocklapis", Blocks.LAPIS_BLOCK, Blocks.LAPIS_BLOCK);

    private final String name;
    private final Block supportBlock;
    private final Item reagent;

    EnumSupports(String name, Block supportBlock, Item reagent) {
        this.name = name;
        this.supportBlock = supportBlock;
        this.reagent = reagent;
    }

    EnumSupports(String name, Block supportBlock, Block reagent) {
        this(name, supportBlock, reagent.asItem());
    }

    @Nonnull
    public String getName() {
        return this.name;
    }

    public Block getSupportBlock() {
        return supportBlock;
    }

    public Item getReagent() {
        return reagent;
    }
}