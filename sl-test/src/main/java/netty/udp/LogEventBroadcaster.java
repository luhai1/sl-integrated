package netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * 广播
 */
public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;
    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)//引导该 NioDatagramChannel（无连接的）
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }
    public void run() throws Exception {
        Channel ch = bootstrap.bind(9998).sync().channel();
        long pointer = 0;
        for (;;) {
            long len = file.length();
            if (len < pointer) {
// file was reset
                pointer = len;
            } else if (len > pointer) {
// Content was added
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {
                    ch.writeAndFlush(new LogEvent(null, -1,
                            file.getAbsolutePath(), line));
                }
                pointer = raf.getFilePointer();
                raf.close();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }
    public void stop() {
        group.shutdownGracefully();
    }
    public static void main(String[] args) throws Exception {

        LogEventBroadcaster broadcaster = new LogEventBroadcaster(
                new InetSocketAddress("255.255.255.255",
                       9999), new File("D:\\data\\logs\\ncms\\core_biz.log"));
        try {
            broadcaster.run();
        }
        finally {
            broadcaster.stop();
        }
    }
}
