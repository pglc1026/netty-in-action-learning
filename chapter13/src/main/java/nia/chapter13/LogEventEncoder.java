package nia.chapter13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * LogEventEncoder
 *
 * @author pglc1026
 * @date 2019-04-05
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

    private final InetSocketAddress remoteAddress;

    public LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
        byte[] file = logEvent.getLogFile().getBytes(CharsetUtil.UTF_8);
        byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = ctx.alloc().buffer(file.length + msg.length + 1);
        // 将日志名写入ByteBuf中
        buf.writeBytes(file);
        // 写入一个分隔符
        buf.writeByte(LogEvent.SEPORATOR);
        // 将日志消息写入ByteBuf中
        buf.writeBytes(msg);
        // 将一个拥有数据和目标地址的新DatagramPacket添加到出站消息中
        out.add(new DatagramPacket(buf, remoteAddress));
    }
}
