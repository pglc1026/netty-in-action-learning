package nia.chapter13;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * LogEventBroadcaster
 *
 * @author pglc1026
 * @date 2019-04-05
 */
public class LogEventBroadcaster {

    private static final String fileName = "/Users/pglc1026/Documents/develop/test/test.log";

    private final EventLoopGroup group;

    private final Bootstrap bootstrap;

    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));

        this.file = file;
    }

    public void run() throws Exception {
        // 绑定channel
        Channel channel = bootstrap.bind(0).sync().channel();
        System.out.println("LogEvent Broadcaster is running!");
        long pointer = 0;
        for (;;) {
            long len = file.length();
            if (len < pointer) {
                // file was reset
                // 若有必要，将pointer置于文件末位
                pointer = len;
            } else if (len > pointer) {
                // Content was added
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                // 设置当前文件指针，以确保没有任何旧日志被发送
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {
                    // 对于每一个日志条目，写一个LogEvent到Channel
                    channel.writeAndFlush(new LogEvent(null, file.getAbsolutePath(), line, -1));
                }
                // 存储当前在文件中的位置
                pointer = raf.getFilePointer();
                raf.close();
            }
            // 休眠1秒，如果中断则退出循环；否则重新处理
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }

        }

    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 2) {
//            throw new IllegalArgumentException();
//        }

//        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", Integer.parseInt(args[0])), new File(args[1]));
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", 9999), new File(fileName));
        try {
            broadcaster.run();
        } catch (Exception e) {
            broadcaster.stop();
        }
    }

}
