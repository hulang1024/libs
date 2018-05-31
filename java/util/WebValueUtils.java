package util;

import java.awt.Color;

public class WebValueUtils {
    
    /**
     * 
     * @param rgb
     * @return rgb(r, g, b)
     */
    public static String rgb(Color color) {
        return "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
    }
}
