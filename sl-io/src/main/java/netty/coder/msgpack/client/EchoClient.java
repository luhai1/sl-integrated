package netty.coder.msgpack.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import netty.coder.msgpack.code.MsgPackDecoder;
import netty.coder.msgpack.code.MsgPackEncoder;

public class EchoClient {
    private static String host;
    private static int port;
    private static int sendNumber;

    public EchoClient(String host,int port,int sendNumber){
        this.host = host;
        this.port = port;
        this.sendNumber = sendNumber;
    }

    public static void run() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,
                                2,0,2));
                        socketChannel.pipeline().addLast("msgpack译码器",new MsgPackDecoder());
                        socketChannel.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
                        socketChannel.pipeline().addLast("msgpack编码器",new MsgPackEncoder());
                        socketChannel.pipeline().addLast(new ClientMessageHandle(sendNumber));
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
            System.out.println("netty client start");
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args){
        try {
            new EchoClient("127.0.0.1",9096,10).run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
