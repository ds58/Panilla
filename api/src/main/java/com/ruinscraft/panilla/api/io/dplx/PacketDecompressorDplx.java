package com.ruinscraft.panilla.api.io.dplx;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.config.PTranslations;
import com.ruinscraft.panilla.api.io.IPacketSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.zip.Inflater;

public class PacketDecompressorDplx extends ByteToMessageDecoder {

    private final IPanilla panilla;
    private final IPanillaPlayer player;
    private final int minBytes;
    private final int maxBytes;
    private final Inflater inflater;

    public PacketDecompressorDplx(IPanilla panilla, IPanillaPlayer player) {
        this.panilla = panilla;
        this.player = player;
        this.minBytes = panilla.getProtocolConstants().minPacketSize();
        this.maxBytes = panilla.getProtocolConstants().maxPacketSize();
        this.inflater = new Inflater();
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final int readableBytes = byteBuf.readableBytes();

        if (readableBytes <= 0) {
            return;
        }

        IPacketSerializer packetSerializer = panilla.createPacketSerializer(byteBuf);

        int packetLength = packetSerializer.readVarInt();

        // uncompressed packet
        if (packetLength == 0) {
            list.add(packetSerializer.readBytes(packetSerializer.readableBytes()));
        }

        // compressed packet
        else if (packetLength > 0) {
            if (packetLength > maxBytes) {
                PTranslations translations = panilla.getPTranslations();
                String message = translations.getTranslation("preventedKick", player.getName(), " oversized packet");
                panilla.getPanillaLogger().log(message, true);
            }

            if (packetLength < minBytes) {
                PTranslations translations = panilla.getPTranslations();
                String message = translations.getTranslation("preventedKick", player.getName(), " undersized packet");
                panilla.getPanillaLogger().log(message, true);
            }

            byte[] buffer = new byte[packetSerializer.readableBytes()];

            packetSerializer.readBytes(buffer);

            inflater.setInput(buffer);

            byte[] data = new byte[packetLength];

            inflater.inflate(data);

            list.add(Unpooled.wrappedBuffer(data));

            inflater.reset();
        }

        // a negative length packet? ignore it.
        else {
            PTranslations translations = panilla.getPTranslations();
            String message = translations.getTranslation("preventedKick", player.getName(), " invalid packet length");
            panilla.getPanillaLogger().log(message, true);
        }
    }

}
