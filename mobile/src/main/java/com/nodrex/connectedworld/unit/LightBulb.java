package com.nodrex.connectedworld.unit;

import com.nodrex.connectedworld.helper.Helper;

/**
 * Light bulb class.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public class LightBulb extends Device{

    private boolean on;

    public LightBulb(int id){
        this(id,null,false);
    }

    public LightBulb(int id,String name){
        this(id, name, false);
    }

    public LightBulb(int id, String name, boolean isOn){
        super(Types.LightBulb,id,name,"Light Bulb");
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
    }

    @Override
    protected LightBulb clone() throws CloneNotSupportedException {
        return new LightBulb(this.id,this.name,this.on);
    }

}
