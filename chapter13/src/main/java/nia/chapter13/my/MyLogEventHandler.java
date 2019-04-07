package nia.chapter13.my;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * LogEventHandler
 *
 * @author pglc1026
 * @date 2019-04-07
 */
public class MyLogEventHandler extends SimpleChannelInboundHandler<MyLogEvent> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyLogEvent msg) throws Exception {
        // 输出到控制台
        String outMsg = msg.getReceivedTimestamp() + " [" + msg.getSource().toString() + "] [" + msg.getLogFile() + "] " + msg.getMsg();
        System.out.println(outMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
