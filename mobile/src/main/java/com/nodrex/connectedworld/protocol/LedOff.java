package com.nodrex.connectedworld.protocol;

public class LedOff extends AsyncTaskParam {

    private final String Error = "ipAndPort should not be null or empty";

    private String value;

    public String getValue(){
        return value;
    }

    public LedOff(String ipAndPort) {
        super(Protocol.LED_OFF);
        if(ipAndPort == null || "".equals(ipAndPort)) throw new NullPointerException(ipAndPort);
        this.value = ipAndPort + Protocol.LED_OFF_ARDUINO;
    }
}
