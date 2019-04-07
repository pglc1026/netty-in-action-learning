package nia.chapter13.my;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * MyLogEventEncoder
 *
 * @author pglc1026
 * @date 2019-04-07
 */
public class MyLogEventEncoder extends MessageToMessageEncoder<MyLogEvent> {

    private final InetSocketAddress remoteAddress;

    public MyLogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, MyLogEvent event, List<Object> out) throws Exception {
        // 获取文件名
        byte[] file = event.getLogFile().getBytes(CharsetUtil.UTF_8);
        // 获取日志信息
        byte[] msg = event.getMsg().getBytes(CharsetUtil.UTF_8);
        // 准备ByteBuf容器
        ByteBuf buf = ctx.alloc().buffer(file.length + 1 + msg.length);
        // 将文件名写入容器
        buf.writeBytes(file);
        // 将SEPARATOR写入容器
        buf.writeByte(MyLogEvent.SEPARATOR);
        // 将日志信息写入容器
        buf.writeBytes(msg);
        // 组装一个含有发送地址和内容的DatagramPacket并添加到出站消息中
        out.add(new DatagramPacket(buf, remoteAddress));
    }
}
