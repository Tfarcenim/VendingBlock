package info.jbcs.minecraft.vending.network;

import info.jbcs.minecraft.vending.Vending;
import info.jbcs.minecraft.vending.network.server.C2SMessageLock;
import info.jbcs.minecraft.vending.network.server.C2SVerifyNameMessage;
import info.jbcs.minecraft.vending.network.server.MessageWrench;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;


/**
 * https://github.com/coolAlias/Tutorial-Demo/blob/master/src/main/java/tutorial/network/PacketDispatcher.java
 * <p>
 * This class will house the SimpleNetworkWrapper instance, which I will name 'dispatcher',
 * as well as give us a logical place from which to register our packets. These two things
 * could be done anywhere, however, even in your Main class, but I will be adding other
 * functionality (see below) that gives this class a bit more utility.
 * <p>
 * While unnecessary, I'm going to turn this class into a 'wrapper' for SimpleNetworkWrapper
 * so that instead of writing "PacketDispatcher.dispatcher.{method}" I can simply write
 * "PacketDispatcher.{method}" All this does is make it quicker to type and slightly shorter;
 * if you do not care about that, then make the 'dispatcher' field public instead of private,
 * or, if you do not want to add a new class just for one field and one static method that
 * you could put anywhere, feel free to put them wherever.
 * <p>
 * For further convenience, I have also added two extra sendToAllAround methods: one which
 * takes an EntityPlayer and one which takes coordinates.
 */
public class PacketHandler {
    /**
     * The SimpleNetworkWrapper instance is used both to register and send packets.
     * Since I will be adding wrapper methods, this field is private, but you should
     * make it public if you plan on using it directly.
     */
    public static final SimpleChannel dispatcher = NetworkRegistry.newSimpleChannel(new ResourceLocation(Vending.MODID,Vending.MODID)
            ,()->"1.0",a -> true,a -> true);
    // a simple counter will allow us to get rid of 'magic' numbers used during packet registration
    private static byte packetId = 0;

    public static void registerPackets() {

        dispatcher.registerMessage(packetId++,MessageWrench.class,
                MessageWrench::write,
                MessageWrench::new,
                MessageWrench::handle);

        dispatcher.registerMessage(packetId++, C2SMessageLock.class,
                C2SMessageLock::write,
                a -> new C2SMessageLock(),
                C2SMessageLock::handle);

        dispatcher.registerMessage(packetId++, C2SVerifyNameMessage.class,
                C2SVerifyNameMessage::write,
                C2SVerifyNameMessage::new,
                C2SVerifyNameMessage::handle);
    }
}