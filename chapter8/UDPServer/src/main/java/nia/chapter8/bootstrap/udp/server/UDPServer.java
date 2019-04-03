package nia.chapter8.bootstrap.udp.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

/**
 * UDPServer
 *
 * @author liuchang39
 * @date 2019/3/11
 */
public class UDPServer {

    public static void main(String[] args) throws Exception {
        new UDPServer().start();
    }

    public void start() throws Exception {
        OioEventLoopGroup group = new OioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        try {
            b.group(group)
                    .channel(OioServerSocketChannel.class)
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        protected void initChannel(DatagramChannel ch) throws Exception {

                        }
                    });
        } finally {
            group.shutdownGracefully().sync();
        }

    }

}
