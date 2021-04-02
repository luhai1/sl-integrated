package bio.server;

import java.io.*;
import java.net.Socket;

public class TimeServiceHandle implements Runnable {

    private Socket socket;

    public TimeServiceHandle(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            printWriter = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()),true);
            String body = null;
            String outData = "error bio";
            while (true){
                body = bufferedReader.readLine();
                if(body == null){
                    break;
                }
                if(body.equals("test_bio")){
                    outData = String.valueOf(System.currentTimeMillis());
                }
                System.out.println("server accept"+outData);
                printWriter.println(outData);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(printWriter != null){
                printWriter.close();

            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
