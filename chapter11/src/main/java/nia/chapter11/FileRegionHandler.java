package nia.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;

/**
 * FileRegionHandler
 *
 * @author pglc1026
 * @date 2019-03-31
 */
public class FileRegionHandler extends ChannelInboundHandlerAdapter {

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    private static final File FILE_FROM_SOMEWHERE = new File("");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel ch = CHANNEL_FROM_SOMEWHERE;
        File file = FILE_FROM_SOMEWHERE;

        // 创建一个FileInputStream
        FileInputStream in = new FileInputStream(file);
        // 以该文件的完整长度创建一个FileRegion
        DefaultFileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());
        // 发送该DefaultFileRegion并注册一个ChannelFutureListener
        ch.writeAndFlush(region).addListener((future) -> {
            if (!future.isSuccess()) {
                // 处理失败
                Throwable cause = future.cause();
                // Do something
            }
        });

        super.channelActive(ctx);
    }
}
