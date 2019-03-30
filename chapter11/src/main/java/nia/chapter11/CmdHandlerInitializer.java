package nia.chapter11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * CmdHandlerInitializer
 *
 * @author pglc1026
 * @date 2019-03-30
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    private static final byte SPACE = (byte)' ';

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                new CmdDecoder(64 * 1024),
                new CmdHandler());
    }

    public static final class Cmd {
        private final ByteBuf name;

        private final ByteBuf args;

        public Cmd(ByteBuf name, ByteBuf args) {
            this.name = name;
            this.args = args;
        }

        public ByteBuf getName() {
            return name;
        }

        public ByteBuf getArgs() {
            return args;
        }
    }

    public static final class CmdDecoder extends LineBasedFrameDecoder {

        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            // 从ByteBuf中提取由行尾符分隔帧
            ByteBuf frame = (ByteBuf) super.decode(ctx, msg);
            // 如果没有输入，则返回null
            if (frame == null) {
                return null;
            }

            // 查找第一个空格符的索引，前面是名称，后面是参数
            int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
            return new Cmd(frame.slice(frame.readerIndex(), index), frame.slice(index + 1, frame.writerIndex()));

        }
    }

    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
            // do something with the command
        }
    }
}
