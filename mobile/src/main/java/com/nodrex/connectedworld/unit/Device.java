package com.nodrex.connectedworld.unit;

import com.nodrex.connectedworld.helper.Helper;

/**
 * Main device class.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public abstract class Device {

    protected int deviceType;
    protected int id;
    protected String name;

    public Device(int deviceType,int id){
        this.deviceType = deviceType;
        this.id = id;
    }

    public Device(int deviceType,int id,String name){
        this(deviceType,id);
        this.name = name;
    }

    public int getDeviceType(){
        return this.deviceType;
    }

    public String getDeviceType(int deviceType){
        switch (deviceType){
            case 0: return LightBulb.Description;
            case 1: return GasSensor.Description;
            default: return Helper.unknownDevice;
        }
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

    @Override
    public String toString() {
        String deviceTypeStr = getDeviceType(deviceType);
        StringBuilder strBuilder = new StringBuilder(deviceTypeStr);
        strBuilder.append(Helper.newLineAndTab);
        strBuilder.append("id");
        strBuilder.append(this.id);
        strBuilder.append(Helper.newLineAndTab);
        strBuilder.append("name");
        strBuilder.append(this.name);
        strBuilder.append(Helper.newLineAndTab);
        return strBuilder.toString();
        //return getDeviceType(deviceType) + Helper.newLineAndTab + "id: " + this.id + Helper.newLineAndTab + "name: " + this.name + Helper.newLineAndTab ;
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
        return this.id == second.id && this.deviceType == second.deviceType && nameEquals;
    }

    @Override
    protected void finalize() throws Throwable {
        this.name = null;
        super.finalize();
    }

}
