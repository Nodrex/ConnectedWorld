package com.nodrex.connectedworld.unit;

/**
 * Created by nchum on 2/11/2018.
 */

public class Garage extends Device {

    public Garage(int id, String ip, String name) {
        this(Types.Garage, id, ip, name, "");
    }

    public Garage(int type, int id, String ip, String name, String description) {
        super(type, id, ip, name, description);
    }

}
