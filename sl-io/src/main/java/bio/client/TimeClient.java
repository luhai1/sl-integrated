package bio.client;

import java.io.*;
import java.net.Socket;

public class TimeClient {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1",9091);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
        printWriter.println("test_bio");
        System.out.println("client: test_bio");
        String resultData = bufferedReader.readLine();
        System.out.println(resultData);
    }
}
