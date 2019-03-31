package nia.chapter11;

import com.google.protobuf.MessageLite;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * ProtoBufInitializer
 *
 * @author pglc1026
 * @date 2019-03-31
 */
public class ProtoBufInitializer extends ChannelInitializer<Channel> {

    private final MessageLite lite;

    public ProtoBufInitializer(MessageLite lite) {
        this.lite = lite;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 添加ProtoBufVarint32FrameDecoder分隔帧
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        // 编码进帧长度信息
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        // 添加ProtoBufEncoder处理编码
        pipeline.addLast(new ProtobufEncoder());
        // 添加ProtoBufDecoder解码
        pipeline.addLast(new ProtobufDecoder(lite));
        // 添加ObjectHandler以处理逻辑
        pipeline.addLast(new ObjectHandler());
    }

    public final class ObjectHandler extends SimpleChannelInboundHandler<Object> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            // Do something
        }
    }
}
