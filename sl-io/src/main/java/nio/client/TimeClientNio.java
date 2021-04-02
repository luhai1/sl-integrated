package nio.client;

public class TimeClientNio {
    public static void main(String[] args){
        new Thread(new TimeClientHandleNio("127.0.0.1",9092),"TimeClientNio").start();
    }
}
