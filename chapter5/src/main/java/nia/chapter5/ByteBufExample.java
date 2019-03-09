package nia.chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * ByteBufExample
 *
 * @author liuchang39
 * @date 2019/3/8
 */
public class ByteBufExample {

    private static Charset utf8 = Charset.forName("UTF-8");

    /**
     * slice ByteBuf
     */
    public static void sliceByteBuf() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf sliceBuf = byteBuf.slice(0, 15);
        System.out.println(sliceBuf.toString(utf8));
        byteBuf.setByte(0, (byte) 'J');
        assert byteBuf.getByte(0) == sliceBuf.getByte((0));
    }

    public static void copyByteBuf() {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf copy = byteBuf.copy();
        byteBuf.setByte(0, (byte) 'J');
        assert byteBuf.getByte(0) == copy.getByte(0);
    }

}
