package netty.coder.msgpack.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import netty.coder.msgpack.code.MsgPackDecoder;
import netty.coder.msgpack.code.MsgPackEncoder;

public class EchoServer {
    private static int port;

    public EchoServer(int port ){
        this.port = port;
    }
    public static void startLister()  {
        EventLoopGroup startEvenLoop = new NioEventLoopGroup();
        EventLoopGroup workEvenLoop = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(startEvenLoop, workEvenLoop)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                            socketChannel.pipeline().addLast(new MsgPackDecoder());
                            socketChannel.pipeline().addLast(new LengthFieldPrepender(2));
                            socketChannel.pipeline().addLast(new MsgPackEncoder());
                            socketChannel.pipeline().addLast(new ServiceMessageHandle());
                        }
                    });
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

    public static void main(String[] args){
        new EchoServer(9096).startLister();
    }

}
