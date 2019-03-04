package nia.chapter2.echoclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * EchoClient
 *
 * @author liuchang39
 * @date 2019/2/27
 */
public class EchoClient {

    private final String host;

    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host><port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        new EchoClient(host, port).start();
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建Bootstrap
            Bootstrap b = new Bootstrap();
            // 指定EventLoopGroup以处理客户端事件；需适用于NIO的实现
            b.group(group)
                    // 适用于NIO传输的Channel类型
                    .channel(NioSocketChannel.class)
                    // 设置服务器的InetSocketAddress
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 在创建Channel时，向ChannelPipeline中添加EchoClientHandler的实例
                    .handler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            // 链接到服务器节点并阻塞当前线程直到连接完成
            ChannelFuture f = b.connect().sync();
            // 阻塞，直到Channel关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭线程池并释放所有资源
            group.shutdownGracefully().sync();
        }
    }

}
