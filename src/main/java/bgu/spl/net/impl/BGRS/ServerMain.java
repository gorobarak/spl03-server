package bgu.spl.net.impl.BGRS;



import bgu.spl.net.srv.Server;

public class ServerMain {

    public static void main(String[] args){
        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                7777, //port
                () ->  new BGRS_Protocol() , //protocol factory
                () -> new BGRS_EncoderDecoder() //message encoder decoder factory
        ).serve();

//        Server.threadPerClient(
//                7777, //port
//                () ->  new BGRS_Protocol() , //protocol factory
//                () -> new BGRS_EncoderDecoder() //message encoder decoder factory
//        ).serve();
    }
}
