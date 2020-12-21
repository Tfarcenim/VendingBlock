package info.jbcs.minecraft.vending.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import info.jbcs.minecraft.vending.settings.Settings;
import info.jbcs.minecraft.vending.tileentity.InfiniteVendingMachineBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.List;

public class VendingMachineBlockEntityRenderer extends TileEntityRenderer<InfiniteVendingMachineBlockEntity> {

	public VendingMachineBlockEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(InfiniteVendingMachineBlockEntity blockEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		ItemStack selling = blockEntity.selling.getStackInSlot(0);
		if (!selling.isEmpty()) {
				//pushmatrix
				matrixStack.push();
				//translate x,y,z
				matrixStack.translate(.5,.5,.5);
				//scale x,y,z
				float scale = (float)(double)Settings.item_size.get();
				matrixStack.scale(scale,scale,scale);
				//rotate
				double f3 = ( Util.milliTime()/20f + partialTicks) * Settings.rotation_speed.get();
				matrixStack.rotate(Vector3f.YP.rotation((float)f3));

				Minecraft.getInstance().getItemRenderer().renderItem(selling, ItemCameraTransforms.TransformType.FIXED,
								combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
				//popmatrix
				matrixStack.pop();
			}
			if (blockEntity.isInfinite())
			if (Settings.show_infinite_tag.get())renderName(blockEntity,"infinite",matrixStack,buffer,combinedLightIn);
	}

	protected void renderNames(InfiniteVendingMachineBlockEntity blockEntity, List<String> messages, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn){
		messages.forEach(message -> renderName(blockEntity,message,matrixStackIn,bufferIn,packedLightIn));
	}

	protected void renderName(InfiniteVendingMachineBlockEntity blockEntity, String displayNameIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if (Vector3d.copyCentered(blockEntity.getPos()).isWithinDistanceOf(this.renderDispatcher.renderInfo.getProjectedView(), blockEntity.getMaxRenderDistanceSquared())) {
			matrixStackIn.push();
			matrixStackIn.translate(0.5, 1.3, 0.5);
			matrixStackIn.rotate(Minecraft.getInstance().getRenderManager().getCameraOrientation());
			matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
			float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
			int j = (int)(f1 * 255.0F) << 24;
			FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
			float f2 = -fontrenderer.getStringWidth(displayNameIn) / 2f;
			fontrenderer.renderString(displayNameIn, f2, 0, 0xffffffff, false, matrix4f, bufferIn, false, j, packedLightIn);

			matrixStackIn.pop();
		}
	}
}
