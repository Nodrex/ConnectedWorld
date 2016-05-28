package com.nodrex.connectedworld.unit;

/**
 * Gas sensor class.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public class GasSensor extends Device {

    public GasSensor(int id) {
        this(id,null);
    }

    public GasSensor(int id,String name){
        super(Types.GasSensor,id,name,"Gas Sensor");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new GasSensor(this.id,this.name);
    }

}

