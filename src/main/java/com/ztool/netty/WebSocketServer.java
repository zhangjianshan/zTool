package com.ztool.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author zhangjianshan on 2023-06-18
 */
public class WebSocketServer {
    private final static EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final static EventLoopGroup bossGroup = new NioEventLoopGroup();

    public void run() {
        ServerBootstrap boot = new ServerBootstrap();
        boot.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("http-codec", new HttpServerCodec());
                        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                        pipeline.addLast("handler", new WebSocketServerHandler());
                    }

                });

        try {
            Channel ch = boot.bind(2048).sync().channel();
            System.out.println("websocket server start at port:2048");
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new WebSocketServer().run();
    }
}
