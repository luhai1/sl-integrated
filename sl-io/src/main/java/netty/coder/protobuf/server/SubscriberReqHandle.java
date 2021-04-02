package netty.coder.protobuf.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import netty.coder.protobuf.SubscribeReqProto;
import netty.coder.protobuf.SubscribeRespProto;

public class SubscriberReqHandle extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq)msg;
        System.out.println(" server receive data: "+ req);
        if(req.getUserName().contains("luhai")){
            ctx.writeAndFlush(resp(req.getSubReqId()));
        }

    }

    private SubscribeRespProto.SubscribeReq resp(int reqId){
        SubscribeRespProto.SubscribeReq.Builder  builder =  SubscribeRespProto.SubscribeReq.newBuilder();
        builder.setSubReqId(reqId);
        builder.setRespCode("200");
        builder.setDesc("success");
        return builder.build();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
