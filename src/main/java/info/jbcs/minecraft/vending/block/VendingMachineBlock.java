package info.jbcs.minecraft.vending.block;

import info.jbcs.minecraft.vending.init.VendingItems;
import info.jbcs.minecraft.vending.init.VendingSoundEvents;
import info.jbcs.minecraft.vending.inventory.WrenchContainer;
import info.jbcs.minecraft.vending.tileentity.VendingMachineBlockEntity;
import info.jbcs.minecraft.vending.tileentity.InfiniteVendingMachineBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

import static info.jbcs.minecraft.vending.stats.ModStats.VENDING_MACHINES_OPENED;

public class VendingMachineBlock extends Block {

	private final boolean infinite;

	public VendingMachineBlock(Properties properties, boolean infinite) {
		super(properties);
		this.infinite = infinite;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
		InfiniteVendingMachineBlockEntity tileEntity = (InfiniteVendingMachineBlockEntity) world.getTileEntity(pos);
		if (player.inventory.getCurrentItem().getItem() == VendingItems.WRENCH && !world.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
				@Override
				public ITextComponent getDisplayName() {
					return new StringTextComponent("wrench");
				}

				@Override
				public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
					return new WrenchContainer(p_createMenu_1_,p_createMenu_2_,pos);
				}
			},pos);
			return ActionResultType.SUCCESS;
		}

		if (tileEntity.isOwner(player) && !player.isCrouching() ||
						player.abilities.isCreativeMode && !player.isSneaking()) {
			if (!world.isRemote) {
				player.addStat(VENDING_MACHINES_OPENED);
				NetworkHooks.openGui((ServerPlayerEntity) player, tileEntity, pos);
			}



			return ActionResultType.SUCCESS;
		} else {
			if (!world.isRemote) {
				if (/*player.isCrouching() ||*/ !tileEntity.isOwner(player)) {
					tileEntity.vend(player,p_225533_6_.getFace(), false);
				}

				world.playSound(player, pos, VendingSoundEvents.PROCESSED,
								SoundCategory.MASTER, 0.3f, 0.6f);
			}
			return ActionResultType.SUCCESS;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entityLiving, @Nonnull ItemStack stack) {
		TileEntity te = world.getTileEntity(pos);

		if (entityLiving != null && te instanceof InfiniteVendingMachineBlockEntity && !world.isRemote) {
			InfiniteVendingMachineBlockEntity teVend = (InfiniteVendingMachineBlockEntity) te;
			PlayerEntity player = (PlayerEntity) entityLiving;
			teVend.setOwnerUUID(player.getGameProfile().getId());
			teVend.markDirty();
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nonnull
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return infinite ? new InfiniteVendingMachineBlockEntity() : new VendingMachineBlockEntity();
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof VendingMachineBlockEntity) {
			VendingMachineBlockEntity vending = (VendingMachineBlockEntity) tileentity;
			if (!infinite)dropItems(vending.inventory, worldIn, pos);
			worldIn.updateComparatorOutputLevel(pos, this);
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}

	public static void dropItems(ItemStackHandler inv, World world, BlockPos pos) {
		IntStream.range(0, inv.getSlots()).mapToObj(inv::getStackInSlot).filter(s -> !s.isEmpty()).forEach(stack -> InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack));
	}

	public static final VoxelShape SHAPE = VoxelShapes.create(0.0625f, 0.125f, 0.0625f, 0.9375f, 0.9375f, 0.9375f);

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	@Nonnull
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

}
