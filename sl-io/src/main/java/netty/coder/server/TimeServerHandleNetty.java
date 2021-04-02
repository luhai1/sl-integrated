package netty.coder.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;



public class TimeServerHandleNetty extends ChannelHandlerAdapter  {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuffer =(ByteBuf)msg;
//        byte[] bytes = new byte[byteBuffer.readableBytes()];
//        byteBuffer.readBytes(bytes);
//        String body = new String(bytes,"UTF-8");
        String body = (String) msg;
      //  body.substring(0,body.length()-System.getProperty("line.separator").length());
        System.out.println(" server receive: " + body);
        String resultStr = " erro nio netty ";
        if("test_netty".equals(body)){
            resultStr = "当前时间： " + System.currentTimeMillis()+System.getProperty("line.separator");
        }
        ByteBuf writerBuffer = Unpooled.copiedBuffer(resultStr.getBytes());
        ctx.writeAndFlush(writerBuffer);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
