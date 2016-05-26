package com.nodrex.connectedworld.unit;

import com.nodrex.connectedworld.helper.Helper;

/**
 * Light bulb class.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public class LightBulb extends Device{

    public static final String Description = "Light Bulb";
    private boolean on;

    public LightBulb(int id){
        super(DeviceType.LightBulb,id);
    }

    public LightBulb(int id,String name){
        super(DeviceType.LightBulb,id,name);
    }

    public LightBulb(int id, String name, boolean isOn){
        super(DeviceType.LightBulb,id,name);
        this.on = isOn;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)return false;
        LightBulb second = null;
        try {
            second = (LightBulb) o;
        }catch (ClassCastException e){
            return false;
        }
        return super.equals(o) && this.on == second.on;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder(super.toString());
        strBuilder.append("State");
        strBuilder.append(this.on ? "on" : "off");
        strBuilder.append(Helper.newLine);
        return strBuilder.toString();
        //return super.toString() + "State" + (this.on ? "on" : "off") + Helper.newLine;
    }

    @Override
    protected LightBulb clone() throws CloneNotSupportedException {
        return new LightBulb(this.id,this.name,this.on);
    }

}
