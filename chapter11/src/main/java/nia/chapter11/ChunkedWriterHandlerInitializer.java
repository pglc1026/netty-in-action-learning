package nia.chapter11;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * ChunkedWriterHandlerInitializer
 *
 * @author pglc1026
 * @date 2019-03-31
 */
public class ChunkedWriterHandlerInitializer extends ChannelInitializer<Channel> {

    private final File file;

    private final SslContext sslCtx;

    public ChunkedWriterHandlerInitializer(File file, SslContext sslCtx) {
        this.file = file;
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SslHandler(sslCtx.newEngine(ch.alloc())));
        pipeline.addLast(new ChunkedWriteHandler());
        // 一旦连接简历，WriteStreamHandler就开始写数据
        pipeline.addLast(new WriteStreamHandler());
    }


    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(
                    new ChunkedStream(new FileInputStream(file)));
        }
    }
}
