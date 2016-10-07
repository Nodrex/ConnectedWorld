package com.nodrex.connectedworld.helper;

/**
 * Helper class.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public abstract class Helper {

    public static final String newLineAndTab = "\n\t";
    public static final String newLine = "\n";
    public static final String unknownDevice = "Unknown Device";
    public static final String GEORGIAN = "ge";

    public static boolean recheckByDevice;//not to make eny action on check change.

    /**
     * checks String data on empty or null content.
     * @param data which should be checked
     * @throws NullPointerException if content is null or empty.
     */
    public static void checkStrContent(String data)throws NullPointerException{
        if(data == null || "".equals(data)) throw new NullPointerException();
    }

    public static boolean scrollingUp = true;

}
