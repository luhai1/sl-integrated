package netty.coder.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServerNetty {

    public static void bind( int port){
        EventLoopGroup startEvenLoop = new NioEventLoopGroup();
        EventLoopGroup workEvenLoop = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(startEvenLoop, workEvenLoop)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandle());
           ChannelFuture channelFuture = bootstrap.bind(port).sync();
           System.out.println(" netty server start!");
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            startEvenLoop.shutdownGracefully();
            workEvenLoop.shutdownGracefully();
        }
    }

    private static class ChildChannelHandle extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
            socketChannel.pipeline().addLast(new StringDecoder());
            socketChannel.pipeline().addLast(new TimeServerHandleNetty());
        }
    }

    public static void main(String[] args){
        new TimeServerNetty().bind(9095);
    }
}
