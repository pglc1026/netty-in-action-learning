package nia.chapter2.echoserver.review;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * EchoServerHandler
 *
 * @author liuchang39
 * @date 2019/3/5
 */
@ChannelHandler.Sharable // 表示ChannelHandler可以在不同Channel安全共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        // 将内容输出到控制台
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        // 将内容原样写回但不冲刷出站消息
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 冲刷未决消息出站
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 打印异常堆栈并关闭Channel
        cause.printStackTrace();
        ctx.close();
    }
}
