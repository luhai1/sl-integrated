package aio.server;

public class TimeServerAio {

    public static void main(String[] args){
        new Thread(new AsyncTimeServerHandel(9094),"AsyncTimeServerHandel ").start();
    }
}
