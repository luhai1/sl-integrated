package aio.client;

public class TimeClientAio {
    public static void main(String[] args){
        new Thread(new AsyncTimeClientHandle("127.0.0.1",9094)).start();
    }
}
