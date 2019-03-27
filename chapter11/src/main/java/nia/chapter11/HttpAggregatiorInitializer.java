package nia.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * HttpAggregatiorInitializer
 *
 * @author liuchang39
 * @date 2019/3/22
 */
public class HttpAggregatiorInitializer extends ChannelInitializer<Channel> {

    private final boolean isClient;

    public HttpAggregatiorInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (isClient) {
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            pipeline.addLast("codec", new HttpServerCodec());
        }

        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
    }
}
