package com.nodrex.connectedworld.unit;

/**
 * Gas sensor class.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public class GasSensor extends Device {

    public GasSensor(int id,String ipAndPort) {
        this(id,ipAndPort,null);
    }

    public GasSensor(int id,String ipAndPort,String name){
        super(Types.GasSensor,id,ipAndPort,name,"Gas Sensor");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new GasSensor(this.id,this.name);
    }

}

