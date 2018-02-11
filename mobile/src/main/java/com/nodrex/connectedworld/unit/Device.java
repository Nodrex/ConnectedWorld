package com.nodrex.connectedworld.unit;

import com.nodrex.connectedworld.helper.Helper;

/**
 * Main device class.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public abstract class Device {

    /**
     * All device types.
     */
    public interface Types {
        int LightBulb = 0;
        int GasSensor = 1;
        int Garage = 2;
        int WaterTemperature = 3;
        int WallSocket = 4;
    }

    protected int type;
    protected int id;
    protected String ip;
    /**
     * User assigned name.
     */
    protected String name;
    /**
     * Developer assigned description.
     */
    protected String description = "Device";

    public Device(int type,int id,String ip,String name,String description){
        this.type = type;
        this.id = id;
        this.ip = ip;
        this.name = name;
        this.description = description;
    }

    public int getType(){
        return this.type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getIp(){
        return this.ip;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder(description);
        strBuilder.append(Helper.newLineAndTab);
        strBuilder.append("id");
        strBuilder.append(this.id);
        strBuilder.append(Helper.newLineAndTab);
        if(this.name != null){
            strBuilder.append("name");
            strBuilder.append(this.name);
        }
        strBuilder.append(Helper.newLineAndTab);
        return strBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)return false;
        Device second = null;
        try {
            second = (Device) o;
        }catch (ClassCastException e){
            return false;
        }
        boolean nameEquals = false;
        try{
            nameEquals = this.name.equals(second.name);
        }catch (NullPointerException e){
            if(second.name == null) nameEquals = true;
        }
        return this.id == second.id && this.type == second.type && nameEquals;
    }

    @Override
    protected void finalize() throws Throwable {
        this.name = null;
        super.finalize();
    }

}
