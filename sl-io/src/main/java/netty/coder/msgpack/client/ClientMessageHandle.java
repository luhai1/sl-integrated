package netty.coder.msgpack.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import netty.coder.msgpack.code.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class ClientMessageHandle extends ChannelHandlerAdapter {
    private  int sendNumber;
    public ClientMessageHandle(int sendNumber){
        this.sendNumber = sendNumber;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        List<UserInfo> userInfoList = getUserInfo();
        for(UserInfo userInfo : userInfoList){
            ctx.writeAndFlush(userInfo);
        }
    }

    private List<UserInfo> getUserInfo(){
        List<UserInfo> userInfoList= new ArrayList<>();
        UserInfo userInfo;
        for(int i=0;i<sendNumber;i++){
            userInfo = new UserInfo();
            userInfo.setUserName("lh" + i);
            userInfo.setAge(18+i);
            userInfoList.add(userInfo);
        }
        return userInfoList;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(" client receive result : " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
