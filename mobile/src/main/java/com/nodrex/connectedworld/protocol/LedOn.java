package com.nodrex.connectedworld.protocol;

public class LedOn extends AsyncTaskParam {

    private final String Error = "ipAndPort should not be null or empty";

    private String value;

    public String getValue(){
        return value;
    }

    public LedOn(String ipAndPort){
        super(Protocol.LED_ON);
        if(ipAndPort == null || "".equals(ipAndPort)) throw new NullPointerException(ipAndPort);
        this.value = ipAndPort + Protocol.LED_ON_ARDUINO;
    }

}
