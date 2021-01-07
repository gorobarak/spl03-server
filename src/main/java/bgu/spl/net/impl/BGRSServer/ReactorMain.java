package bgu.spl.net.impl.BGRSServer;



import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main(String[] args){

        int port = Integer.parseInt(args[0]);
        int nthreads = Integer.parseInt(args[1]);
        Server.reactor(
                nthreads,//Runtime.getRuntime().availableProcessors()
                port, //port
                () ->  new BGRS_Protocol() , //protocol factory
                () -> new BGRS_EncoderDecoder() //message encoder decoder factory
        ).serve();

//
    }
}
