package nia.chapter13.my;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * MyLogEventBroadcaster
 *
 * @author pglc1026
 * @date 2019-04-07
 */
public class MyLogEventBroadcaster {

    private static final String fileName = "/Users/pglc1026/Documents/develop/test/test.log";

    private final EventLoopGroup group;

    private final Bootstrap bootstrap;

    private final File file;

    public MyLogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MyLogEventEncoder(address));
                    }
                });
        this.file = file;
    }

    protected void run() throws Exception {
        // 绑定Channel
        Channel channel = bootstrap.bind(0).sync().channel();
        // 声明一个变量用于记录文件位置
        long pointer = 0L;

        System.out.println("MyLogEvent Broadcaster is running!");

        // 开始程序
        for (;;) {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            long len = raf.length();
            if (len < pointer) {
                // file was reset
                //如果有必要，将pointer置于文件末位
                pointer = len;
            } else if (len > pointer) {
                // 指定开始读取文件的位置
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {

                        // 构建LogEvent并写入channel
                        channel.writeAndFlush(new MyLogEvent(file.getAbsolutePath(), line));

                }
                pointer = raf.getFilePointer();
                raf.close();
            }
            // 休眠1s，如果中断则退出循环，否则继续
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    protected void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        MyLogEventBroadcaster broadcaster = new MyLogEventBroadcaster(new InetSocketAddress("255.255.255.255", 9999), new File(fileName));
        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }

    }
}
