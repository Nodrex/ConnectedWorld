package com.nodrex.connectedworld.unit;

/**
 * Gas sensor class.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public class GasSensor extends Device {

    public static final String Description = "Gas Sensor";

    public GasSensor(int id) {
        super(DeviceType.GasSensor,id);
    }

    public GasSensor(int id,String name){
        super(DeviceType.LightBulb,id,name);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new GasSensor(this.id,this.name);
    }
}

