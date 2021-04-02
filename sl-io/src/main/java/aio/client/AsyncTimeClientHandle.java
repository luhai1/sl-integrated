package aio.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandle implements Runnable, CompletionHandler<Void,AsyncTimeClientHandle> {

    private String host;
    private int port;
    public static AsynchronousSocketChannel asynchronousSocketChannel;
    private CountDownLatch countDownLatch;

    public AsyncTimeClientHandle( String host,int port){
        this.host = host;
        this.port=port;
        try {
            asynchronousSocketChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        asynchronousSocketChannel.connect(new InetSocketAddress(host,port),this,this);
        System.out.println(" client start !");
        try {
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            asynchronousSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AsyncTimeClientHandle attachment) {
        try {
            byte[] writerData = "test_aio".getBytes("UTF-8");
            ByteBuffer byteBuffer = ByteBuffer.allocate(writerData.length);
            byteBuffer.put(writerData);
            byteBuffer.flip();
            asynchronousSocketChannel.write(byteBuffer,byteBuffer, new CompletionHandler<Integer,ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if(attachment.hasRemaining()){
                        asynchronousSocketChannel.write(attachment,attachment,this);
                    }else{
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        asynchronousSocketChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer attachment) {
                                attachment.flip();
                                byte[] readResult = new byte[attachment.remaining()];
                                attachment.get(readResult);
                                try {
                                    String body = new String(readResult,"UTF-8");
                                    System.out.println(" client receive: "+body);
                                    countDownLatch.countDown();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {

                            }
                        });

                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        asynchronousSocketChannel.close();
                        countDownLatch.countDown();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandle attachment) {
        exc.printStackTrace();
        try {
            asynchronousSocketChannel.close();
            countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
