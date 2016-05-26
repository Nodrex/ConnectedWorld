package com.nodrex.android.tools;

import android.app.Activity;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

/**
 * This class contains useful method to control swipe direction, distance, range and lot more for android view.
 * </br>All coordinates and distance is in <b>dp</b> dimension.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public class Swipe {
	
	private static float startX;
	private static float startY;
	private static float currentX;
	private static float currentY;
	private static int dirrectionX;
	private static int dirrectionY;
	private static float distanceX;
	private static float distanceY;
	private static float freshDistanceX;
	private static float freshDistanceY;
	private static int fingerCount;
	
	private static float leftRange =-1;
	private static float rightRange =-1;
	private static float topRange =-1;
	private static float bottomRange =-1;
	
	private Swipe(){};
	
	/**
	 * @return x position of finger generated in action down.
	 */
	public static float getStartX() {
		return startX;
	}

	/**
	 * @return y position of finger generated in action down.
	 */
	public static float getStartY() {
		return startY;
	}

	/**
	 * @return finger current x position.
	 */
	public static float getCurrentX() {
		return currentX;
	}

	/**
	 * @return finger current y position.
	 */
	public static float getCurrentY() {
		return currentY;
	}

	/**
	 * @return swipe direction of finger over x coordinate.</br>
	 * -1 means left.</br>
	 * 0 means without move.</br>
	 * 1 means right.
	 */
	public static int getDirrectionX() {
		return dirrectionX;
	}

	/**
	 * @return swipe direction of finger over y coordinate.</br>
	 * -1 means top.</br>
	 * 0 means without move.</br>
	 * 1 means bottom.
	 */
	public static int getDirrectionY() {
		return dirrectionY;
	}

	/**
	 * @return distance between action down x and current x.
	 */
	public static float getDistanceX() {
		return distanceX;
	}

	/**
	 * @return distance between action down y and current y.
	 */
	public static float getDistanceY() {
		return distanceY;
	}

	/**
	 * @return Fresh distance between old action move x and current x.
	 */
	public static float getFreshDistanceX() {
		return freshDistanceX;
	}

	/**
	 * @return Fresh distance between old action move y and current y.
	 */
	public static float getFreshDistanceY() {
		return freshDistanceY;
	}

	/**
	 * @return finger number of current view.
	 */
	public static int getFingerCount() {
		return fingerCount;
	}
	
	/**
	 * Should be called first in view's onTouch method.
	 * @param v
	 * @param event
	 */
	public static void onTouch(View v, MotionEvent event) {
		fingerCount = event.getPointerCount();
	}
	
	/**
	 * Should be called first after action down even.
	 * @param activity
	 * @param v
	 * @param event
	 */
	public static void actionDown(Activity activity, View v, MotionEvent event){
		startX = Util.pixelToDp(activity, event.getX());
		startY = Util.pixelToDp(activity, event.getY());
	}
	
	/**
	 * Should be called first after action move even.
	 * @param activity
	 * @param v
	 * @param event
	 */
	public static void actionMove(Activity activity, View v, MotionEvent event){
		calculate(activity, v, event);
	}
	
	/**
	 * Should be called first after action up even.
	 * @param activity
	 * @param v
	 * @param event
	 */
	public static void actionUp(Activity activity, View v, MotionEvent event){
		calculate(activity, v, event);
	}
	
	private static void calculate(Activity activity, View v, MotionEvent event){
		float tmpX = Util.pixelToDp(activity, event.getX());
		if(freshDistanceX == 0) freshDistanceX = tmpX;
		else freshDistanceX = tmpX - currentX;

		currentX = tmpX;
		distanceX = startX - currentX;
		dirrectionX = distanceX > 1 ? -1 : 1;
		distanceX = Math.abs(distanceX);

		float tmpY = Util.pixelToDp(activity, event.getY());
		if(freshDistanceY == 0) freshDistanceY = tmpY;
		else freshDistanceY = tmpY - currentY;

		currentY = tmpY;
		distanceY = startY - currentY;
		dirrectionY = distanceY > 1 ? -1 : 1;
		distanceY = Math.abs(distanceY);
	}

	/**
	 * @param activity
	 * @param screenDivisionCount number of screen parts in which screen should be divided.
	 * @return left range of screen.
	 */
	public static float getLeftRange(Activity activity, float screenDivisionCount){
		if(leftRange > -1 || activity == null || screenDivisionCount == 0) return leftRange;
		Point size = Util.getDisplayPoint(activity);
		if(size == null) return leftRange;
		float displayWidth = Util.pixelToDp(activity, size.x);
		leftRange = displayWidth/screenDivisionCount;
		return leftRange;
	}
	
	/**
	 * @param xPoint
	 * @return true if given point is in left range.
	 */
	public static boolean isInLeftRange(float xPoint){
		return xPoint < leftRange;
	}
	
	/**
	 * @param activity
	 * @param screenDivisionCount number of screen parts in which screen should be divided.
	 * @return right range of screen
	 */
	public static float getRightRange(Activity activity, float screenDivisionCount){
		if(rightRange > -1 || activity == null || screenDivisionCount == 0) return rightRange;
		Point size = Util.getDisplayPoint(activity);
		if(size == null) return rightRange;
		float displayWidth = Util.pixelToDp(activity, size.x);
		leftRange = displayWidth/screenDivisionCount;
		return displayWidth - leftRange;
	}
	
	/**
	 * @param xPoint
	 * @return true if given point is in right range.
	 */
	public static boolean isInRightRange(float xPoint){
		return xPoint > rightRange;
	}
	
	/**
	 * @param activity
	 * @param screenDivisionCount number of screen parts in which screen should be divided.
	 * @return top range of screen.
	 */
	public static float getTopRange(Activity activity, float screenDivisionCount){
		if(topRange > -1 || activity == null || screenDivisionCount == 0) return topRange;
		Point size = Util.getDisplayPoint(activity);
		if(size == null) return topRange;
		float displayHeight = Util.pixelToDp(activity, size.y);
		bottomRange = displayHeight/screenDivisionCount;
		return displayHeight - bottomRange;
	}
	
	/**
	 * @param yPoint
	 * @return true if given point is in top range.
	 */
	public static boolean isInTopRange(float yPoint){
		return yPoint > topRange;
	}
	
	/**
	 * @param activity
	 * @param screenDivisionCount number of screen parts in which screen should be divided.
	 * @return bottom range of screen.
	 */
	public static float getBottomRange(Activity activity, float screenDivisionCount){
		if(bottomRange > -1 || activity == null || screenDivisionCount == 0) return topRange;
		Point size = Util.getDisplayPoint(activity);
		if(size == null) return bottomRange;
		float displayHeight = Util.pixelToDp(activity, size.y);
		bottomRange = displayHeight/screenDivisionCount;
		return bottomRange;
	}
	
	/**
	 * @param yPoint
	 * @return true if given point is in bottom range.
	 */
	public static boolean isInBottomRange(float yPoint){
		return yPoint < bottomRange;
	}
	
	/**
	 * @param by
	 * @return true if x distance is more then y distance multiplied by.
	 */
	public static boolean isXmoreThenY(double by){
		return distanceX > by * distanceY;
	}
	
	/**
	 * @return true if x distance is twice more then y distance.
	 */
	public static boolean isXTwicemoreThenY(){
		return distanceX > 2 * distanceY;
	}
	
	/**
	 * @param by
	 * @return true if y distance is more then x distance multiplied by.
	 */
	public static boolean isYmoreThenX(double by){
		return distanceY > by * distanceX;
	}
	
	/**
	 * @return true if y distance is twice more then x distance.
	 */
	public static boolean isYTwicemoreThenX(){
		return distanceY > 2 * distanceX;
	}
	
	/**
	 * @param value
	 * @return true if x distance is more then given value. 
	 */
	public static boolean isXDistanceMore(float value){
		return distanceX > value;
	}
	
	/**
	 * @param value
	 * @return true if y distance is more then given value.
	 */
	public static boolean isYDistanceMore(float value){
		return distanceY > value;
	}
	
	/**
	 * @param value
	 * @return true if x distance is less then given value.
	 */
	public static boolean isXDistanceLess(double value){
		return distanceX < value;
	}
	
	/**
	 * @param value
	 * @return true if y distance is less then given value.
	 */
	public static boolean isYDistanceLess(double value){
		return distanceY < value;
	}
	
}
