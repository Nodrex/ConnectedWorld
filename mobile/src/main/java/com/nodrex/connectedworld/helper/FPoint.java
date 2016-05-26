package com.nodrex.connectedworld.helper;

/**
 * Floating point(x,t)
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public class FPoint {

    private float x, y;

    public FPoint(){}

    public FPoint(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    /**
     * Offset the point's coordinates by dx, dy
     */
    public void offset(float dx, float dy) {
        offsetX(dx);
        offsetY(dy);
    }

    public void offsetX(float dx) {
        x += dx;
    }

    public void offsetY(float dy) {
        y += dy;
    }
}
