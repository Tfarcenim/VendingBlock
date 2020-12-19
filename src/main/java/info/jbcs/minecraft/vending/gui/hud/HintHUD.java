package info.jbcs.minecraft.vending.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import info.jbcs.minecraft.vending.Utils;
import info.jbcs.minecraft.vending.settings.Settings;
import info.jbcs.minecraft.vending.tileentity.InfiniteVendingMachineBlockEntity;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class HintHUD {
	private static Minecraft mc = Minecraft.getInstance();

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void renderInfo(RenderGameOverlayEvent.Post event) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;

		if (mc == null || mc.player == null || mc.world == null) return;
		RayTraceResult mop = mc.objectMouseOver;
		if (!(mop instanceof BlockRayTraceResult)) return;
		BlockRayTraceResult blockRayTrace = (BlockRayTraceResult) mop;
		TileEntity te = mc.world.getTileEntity(blockRayTrace.getPos());
		if (!(te instanceof InfiniteVendingMachineBlockEntity)) return;
		InfiniteVendingMachineBlockEntity vending = (InfiniteVendingMachineBlockEntity) te;

		ITextComponent owner = vending.getOwnerName();

		MainWindow resolution = mc.getMainWindow();

		int w = resolution.getScaledWidth();
		int h = resolution.getScaledHeight();

		int centerx = w / 2;


		if (owner != null) {

			String topText = owner.getFormattedText() + " is selling";

			String bottomText = "for ";

			int barw = mc.fontRenderer.getStringWidth(topText) + 24;

			AbstractGui.fill(centerx - barw / 2, 0,
							centerx + barw / 2, 50, 0x7F000000);

			mc.fontRenderer.drawString(topText,
							centerx - barw / 2 + 4, 3, 0xffffff);

			mc.fontRenderer.drawString(bottomText,
							centerx - barw / 2 + 4, 19, 0xffffff);

			if (vending.isInfinite() && Settings.show_infinite_tag.get())mc.fontRenderer.drawString("Infinite Stock",
							centerx - barw / 2 + 4, 35, 0xffffff);

			final int sellX = centerx + barw/2 - 18;
			final int sellY = 0;
			final int buyX = centerx - barw/2 +4+ mc.fontRenderer.getStringWidth(bottomText);
			final int buyY = 16;
			renderItem(sellX, sellY, 0, mc.player, vending.selling.getStackInSlot(0));
			renderItem(buyX, buyY, 0, mc.player, vending.buying.getStackInSlot(0));
		}

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		Utils.bind("textures/gui/icons.png");
	}

	private static void renderItem(int x, int y, float partialTicks, PlayerEntity player, ItemStack stack) {

		mc.getItemRenderer().renderItemAndEffectIntoGUI(player, stack, x, y);

		mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, stack, x, y);
	}

}
