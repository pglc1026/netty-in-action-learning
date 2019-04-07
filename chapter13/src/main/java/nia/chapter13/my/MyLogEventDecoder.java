package nia.chapter13.my;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;

import java.util.List;

/**
 * MyLogEventDecoder
 *
 * @author pglc1026
 * @date 2019-04-07
 */
public class MyLogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        // 获取数据引用
        ByteBuf data = datagramPacket.content();
        // 获取SEPARATOR的索引
        int idx = data.indexOf(0, data.readableBytes(), MyLogEvent.SEPARATOR);
        // 获取文件名
        String fileName = data.slice(0, idx).toString(CharsetUtil.UTF_8);
        // 获取日志内容
        // slice的第二个参数的含义为派生ByteBuf的长度！而不是endIndex！
        String msg = data.slice(idx + 1, data.readableBytes() - idx - 1).toString(CharsetUtil.UTF_8);
        // 封装LogEvent并交给下个ChannelHandler
        out.add(new MyLogEvent(datagramPacket.sender(), fileName, msg, System.currentTimeMillis()));
    }
}
