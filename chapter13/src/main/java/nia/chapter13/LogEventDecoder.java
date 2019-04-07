package nia.chapter13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * LogEventDecoder
 *
 * @author pglc1026
 * @date 2019-04-07
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        // 获取datagramPacket中数据的引用
        ByteBuf data = datagramPacket.content();
        // 获取SEPARATOR的索引
        int idx = data.indexOf(0, data.readableBytes(), LogEvent.SEPORATOR);
        // 获取文件名
        String fileName = data.slice(0, idx).toString(CharsetUtil.UTF_8);
        // 获取日志信息
        String fileMsg = data.slice(idx + 1, data.readableBytes()).toString(CharsetUtil.UTF_8);
        // 构建一个LogEvent并将它添加到已经解码的消息列表中
        out.add(new LogEvent(datagramPacket.sender(), fileName, fileMsg, System.currentTimeMillis()));
    }
}
