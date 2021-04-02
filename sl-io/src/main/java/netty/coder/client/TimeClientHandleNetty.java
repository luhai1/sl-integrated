package netty.coder.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

public class TimeClientHandleNetty extends ChannelHandlerAdapter {

    private   byte[] bytes;
    public TimeClientHandleNetty() throws UnsupportedEncodingException {
       bytes = ("test_netty"+System.getProperty("line.separator")).getBytes();

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i =0;i<50;i++){
           ByteBuf sendMessage = Unpooled.buffer(bytes.length);
            sendMessage.writeBytes(bytes);
            ctx.writeAndFlush(sendMessage);
        }

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf)msg;
//        byte[] bytes = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(bytes);
//        String result = new String(bytes,"UTF-8");
        String result =(String)msg;
        System.out.println(" netty client receive: " + result);
    }
}
