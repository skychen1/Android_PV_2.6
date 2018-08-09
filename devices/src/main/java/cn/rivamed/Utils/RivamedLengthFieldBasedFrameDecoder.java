package cn.rivamed.Utils;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class RivamedLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {

    int afterLenOffset=0;

    /**
     * Creates a new instance.
     *
     * @param maxFrameLength    the maximum length of the frame.  If the length of the frame is
     *                          greater than this value, {@link } will be
     *                          thrown.
     * @param lengthFieldOffset the offset of the length field
     * @param lengthFieldLength
     */

    public RivamedLengthFieldBasedFrameDecoder(
            int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
            int lengthAdjustment, int initialBytesToStrip, int afterLenOffset, boolean failFast) {
        super(
                ByteOrder.BIG_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength,
                lengthAdjustment, initialBytesToStrip, failFast);
        this.afterLenOffset=afterLenOffset;
    }
    /**
     * Decodes the specified region of the buffer into an unadjusted frame length.  The default implementation is
     * capable of decoding the specified region into an unsigned 8/16/24/32/64 bit integer.  Override this method to
     * decode the length field encoded differently.  Note that this method must not modify the state of the specified
     * buffer (e.g. {@code readerIndex}, {@code writerIndex}, and the content of the buffer.)
     *
     * @throws DecoderException if failed to decode the specified region
     */
    protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
        if(afterLenOffset==0)return super.getUnadjustedFrameLength(buf,offset,length,order);
        else{
            buf = buf.order(order);
            long frameLength;
            switch (length) {
                case 1:
                    frameLength = buf.getUnsignedByte(offset);
                    break;
                case 2:
                    frameLength = buf.getUnsignedShort(offset);
                    break;
                case 3:
                    frameLength = buf.getUnsignedMedium(offset);
                    break;
                case 4:
                    frameLength = buf.getUnsignedInt(offset);
                    break;
                case 8:
                    frameLength = buf.getLong(offset);
                    break;
                default:
                    throw new DecoderException(
                            "unsupported lengthFieldLength: "  + " (expected: 1, 2, 3, 4, or 8)");
            }
            return frameLength+afterLenOffset;
        }

    }

}
