package nia.chapter11;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;

/**
 * ContinuationFrameHandler
 *
 * @author pglc1026
 * @date 2019-03-30
 */
public class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ContinuationWebSocketFrame continuationWebSocketFrame) throws Exception {
        // handle continuation frame
    }
}
