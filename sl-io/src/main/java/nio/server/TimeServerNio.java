package nio.server;

import java.io.IOException;
import java.net.ServerSocket;

public class TimeServerNio {
    public static void main(String[] args){
        try {
            new Thread(new MultiplexerTimeServer(9092),"TimeServerNio").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
