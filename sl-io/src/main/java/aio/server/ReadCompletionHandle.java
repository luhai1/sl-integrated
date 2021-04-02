package aio.server;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandle implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;
    public ReadCompletionHandle(AsynchronousSocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }
    @SneakyThrows
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        String body = new String(bytes,"UTF-8");
        System.out.println(" server accept data: "+body);
        String resultData = "error aio ";
        if("test_aio".equals(body)){
            resultData = String.valueOf(System.currentTimeMillis());
        }
        doWriter(resultData);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }

    private void doWriter(String resultData) throws UnsupportedEncodingException {
        byte[] writerByte = resultData.getBytes("UTF-8");
        ByteBuffer byteBuffer = ByteBuffer.allocate(writerByte.length);
        byteBuffer.put(writerByte);
        byteBuffer.flip();
        this.socketChannel.write(byteBuffer,byteBuffer, new CompletionHandler<Integer,ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if(attachment.hasRemaining()){
                    socketChannel.write(byteBuffer,byteBuffer,this);
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
