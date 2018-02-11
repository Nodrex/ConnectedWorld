package com.nodrex.connectedworld.unit;

/**
 * Created by nchum on 2/11/2018.
 */

public class WaterTemperature extends Device {

    private boolean isHot;

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public WaterTemperature(int id, String ip, String name) {
        this(Types.WaterTemperature, id, ip, name, "");
    }

    public WaterTemperature(int type, int id, String ip, String name, String description) {
        super(type, id, ip, name, description);
    }
}
