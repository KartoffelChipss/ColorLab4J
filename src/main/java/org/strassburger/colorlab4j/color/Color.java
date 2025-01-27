package org.strassburger.colorlab4j.color;

import org.strassburger.colorlab4j.color.spaces.*;

public abstract class Color {

    /**
     * Convert the color to RGB
     * @return RGB representation of the color
     */
    public abstract RGBColor toRGB();

    /**
     * Convert the color to HSL
     * @return HSL representation of the color
     */
    public abstract HSLColor toHSL();

    /**
     * Convert the color to HSV
     * @return HSV representation of the color
     */
    public abstract HSVColor toHSV();

    /**
     * Convert the color to LAB
     * @return LAB representation of the color
     */
    public abstract LABColor toLAB();

    /**
     * Convert the color to XYZ
     * @return XYZ representation of the color
     */
    public abstract XYZColor toXYZ();

    /**
     * Get the hex representation of the color
     * @return Hex representation of the color (e.g. "#ff0000")
     */
    public String toHex() {
        return toRGB().toHex();
    }

    /**
     * Get the ANSI representation of the color
     * @return ANSI representation of the color
     */
    public String toAnsi() {
        return toRGB().toAnsi();
    }

    /**
     * Get the ANSI representation of the color for the background
     * @return ANSI representation of the color for the background
     */
    public String toAnsiBackground() {
        return toRGB().toAnsiBackground();
    }

    /**
     * Get the CSS representation of the color
     * @return CSS representation of the color
     */
    public abstract String toCssString();
}
