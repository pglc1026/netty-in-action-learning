package nia.chapter8.bootstrap.udp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.oio.OioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * UDPClient
 *
 * @author liuchang39
 * @date 2019/3/11
 */
public class UDPClient {

    public static void main(String[] args) throws Exception {
        new UDPClient().start();
    }

    public void start() throws Exception {
        OioEventLoopGroup group = new OioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(group).channel(OioDatagramChannel.class).handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<DatagramPacket>() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            super.channelActive(ctx);
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                            System.out.println("Receive message: " + msg.toString());
                        }
                    });
                }
            });
            ChannelFuture bindFuture = b.bind(new InetSocketAddress("127.0.0.1", 8081));
            bindFuture.addListener((future) -> {
                if (future.isSuccess()) {
                    System.out.println("Channel bound");
                } else {
                    System.out.println("bind accept failed");
                    future.cause().printStackTrace();

                }
            });
        } finally {
            group.shutdownGracefully().sync();
        }
    }

}
