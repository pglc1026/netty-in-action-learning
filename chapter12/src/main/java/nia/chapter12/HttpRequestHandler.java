package nia.chapter12;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * HttpRequestHandler
 *
 * @author pglc1026
 * @date 2019-03-31
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;

    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation();

        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(
                    "Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 如果升级了websocket，则增加它的引用计数，并传递给下一个ChannelInboundHandler
        if (wsUri.equalsIgnoreCase(request.uri())) {
            ctx.fireChannelRead(request.retain());
        } else {
            // 处理100Continue请求以符合HTTP 1.1规范
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }

            RandomAccessFile file = new RandomAccessFile(INDEX, "r");
            FullHttpResponse response = new DefaultFullHttpResponse(
                    request.protocolVersion(), HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
            // 如果请求了keep-alive，则设置需要的头信息
            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderNames.KEEP_ALIVE);
            }
            ctx.write(response);
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }

            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }

        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
