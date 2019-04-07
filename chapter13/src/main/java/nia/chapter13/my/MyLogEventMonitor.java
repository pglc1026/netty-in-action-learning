package nia.chapter13.my;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * MyLogEventMonitor
 *
 * @author pglc1026
 * @date 2019-04-07
 */
public class MyLogEventMonitor {

    private final EventLoopGroup group;

    private final Bootstrap bootstrap;

    public MyLogEventMonitor() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MyLogEventDecoder());
                        pipeline.addLast(new MyLogEventHandler());
                    }
                });
    }

    protected Channel bind(int port) throws Exception {
        return bootstrap.bind(port).sync().channel();
    }

    protected void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {

        MyLogEventMonitor monitor = new MyLogEventMonitor();
        Channel channel = monitor.bind(9999);
        System.out.println("MyLogEvent Monitor is running!");
        try {
            channel.closeFuture().syncUninterruptibly();
        } finally {
            monitor.stop();
        }

    }
}
