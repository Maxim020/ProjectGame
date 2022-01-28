package server.model;

public class ServerUnavailableException extends Exception{
    public ServerUnavailableException(String msg){
        super(msg);
    }
}
