package info.jbcs.minecraft.vending.gui;

import info.jbcs.minecraft.vending.Utils;
import info.jbcs.minecraft.vending.inventory.VendingMachineContainer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

public class VendingMachineScreen extends ContainerScreen<VendingMachineContainer> {

    public VendingMachineScreen(VendingMachineContainer containerVendingMachine,
                                PlayerInventory inventoryplayer, ITextComponent title) {
        super(containerVendingMachine,inventoryplayer,title);
        ySize += 67;
    }

    @Override
    public void init() {
        super.init();
        /*buttons.clear();
        LockIconButton button = new LockIconButton(guiLeft + 7, guiTop + 63, b -> {
            C2SMessageLock msg = new C2SMessageLock();

            PacketHandler.dispatcher.sendToServer(msg);
            ((LockIconButton)b).setLocked(!container.blockEntity.isOpen());
        });
        button.setLocked(!container.blockEntity.isOpen());
        buttons.add(button);*/
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int a, int b) {
        FontRenderer font = this.font;
        font.drawString(net.minecraft.client.resources.I18n.format("gui.vending.sell").trim(), 12, 87, 0x404040);
        font.drawString(net.minecraft.client.resources.I18n.format("gui.vending.buy").trim(), 12, 20, 0x404040);

        font.drawString(net.minecraft.client.resources.I18n.format("gui.vending.buy_storage").trim(), 40, 6, 0x404040);
        font.drawString(net.minecraft.client.resources.I18n.format("gui.vending.sell_storage").trim(), 40, 74, 0x404040);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int a, int b) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Utils.bind("vending:textures/vending-gui.png");
        blit((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
    }

    @Override
    public void onClose() {
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks){
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
