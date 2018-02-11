package com.nodrex.connectedworld.unit;

/**
 * Created by nchum on 2/11/2018.
 */

public class WallSocket extends Device {

    public WallSocket(int id, String ip, String name) {
        super(Types.WallSocket, id, ip, name, "");
    }

    public WallSocket(int type, int id, String ip, String name, String description) {
        super(type, id, ip, name, description);
    }

}
