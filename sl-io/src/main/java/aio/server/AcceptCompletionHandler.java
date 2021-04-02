package aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler  implements CompletionHandler<AsynchronousSocketChannel,AsyncTimeServerHandel> {
    public AcceptCompletionHandler() {
    }

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandel attachment) {
        attachment.asynchronousServerSocketChannel.accept(attachment,this);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        result.read(byteBuffer,byteBuffer,new ReadCompletionHandle(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandel attachment) {
        exc.printStackTrace();
        attachment.countDownLatch.countDown();
    }
}
