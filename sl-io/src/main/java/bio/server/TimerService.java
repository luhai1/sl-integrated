package bio.server;

import java.net.ServerSocket;
import java.net.Socket;

public class TimerService {
    public static void main(String[] args){
        try {
            ServerSocket serverSocket = new ServerSocket(9091);
            while (true){
               Socket socket = serverSocket.accept();
                new Thread(new TimeServiceHandle(socket)).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
