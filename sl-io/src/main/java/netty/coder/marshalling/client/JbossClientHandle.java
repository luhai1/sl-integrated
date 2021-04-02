package netty.coder.marshalling.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import netty.coder.protobuf.SubscribeReqProto;

import java.util.ArrayList;
import java.util.List;

public class JbossClientHandle extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        List<SubscribeReqProto.SubscribeReq> subscribeReqList = initReq();
        for(SubscribeReqProto.SubscribeReq subscribeReq : subscribeReqList){
            ctx.writeAndFlush(subscribeReq);
        }
        ctx.flush();
    }

    private static List<SubscribeReqProto.SubscribeReq> initReq(){
        List<SubscribeReqProto.SubscribeReq> subscribeReqList = new ArrayList<>();
        SubscribeReqProto.SubscribeReq subscribeReq;
        SubscribeReqProto.SubscribeReq.Builder builder;
        for(int i=0;i<10;i++){
            builder = SubscribeReqProto.SubscribeReq.newBuilder();
            builder.setSubReqId(i);
            builder.setProductName("test marshshalling"+i);
            builder.setUserName("luhai"+i);
            builder.setAddress("hefei"+i);
            subscribeReq = builder.build();
            subscribeReqList.add(subscribeReq);
        }
        return subscribeReqList;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(" client receive data:" + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
