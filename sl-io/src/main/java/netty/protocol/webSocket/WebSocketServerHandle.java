package netty.protocol.webSocket;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;

public class WebSocketServerHandle extends ChannelHandlerAdapter {
    WebSocketServerHandshaker handshaker;
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest){
            handleFullHttpRequest(ctx,(FullHttpRequest)msg);
        }else if(msg instanceof WebSocketFrame) {
            handleWebsocketRequest(ctx,(WebSocketFrame)msg);
        }
    }


    private void handleFullHttpRequest(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest){
        if(!"websocket".equals(fullHttpRequest.headers().get("Upgrade"))){
            sendResponse(ctx,fullHttpRequest,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
        }
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory("ws://127.0.0.1:9099/websocket",null,false);
        handshaker = factory.newHandshaker(fullHttpRequest);
        if(null == handshaker){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else{
            handshaker.handshake(ctx.channel(),fullHttpRequest);
        }
    }

    private void handleWebsocketRequest(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame){
        if(webSocketFrame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),(CloseWebSocketFrame)webSocketFrame.retain());
            return;
        }
        if(webSocketFrame instanceof PingWebSocketFrame){
            ctx.channel().writeAndFlush(new PongWebSocketFrame(webSocketFrame.content().retain()));
            return;
        }

        if (! (webSocketFrame instanceof  TextWebSocketFrame)){
            throw new UnsupportedOperationException("只支持文本类型");
        }

        String data = ((TextWebSocketFrame)webSocketFrame).text();
        ctx.channel().writeAndFlush(new TextWebSocketFrame(data + " , Welcome netty wobSocket,system time: "+System.currentTimeMillis()));
    }

    private void sendResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response){
        ctx.channel().writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
