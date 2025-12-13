package org.strassburger.colorlab4j.color;

import org.strassburger.colorlab4j.color.spaces.*;

/**
 * Abstract representation of a color.
 * Provides methods to convert between different color spaces.
 */
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

    /**
     * Get the color from a hex string
     * @param hex Hex representation of the color (e.g. "#ff0000")
     * @return Color representation of the color
     * @throws IllegalArgumentException If the HEX code is invalid
     */
    public static Color fromHex(String hex) {
        return RGBColor.fromHex(hex);
    }

    /**
     * Create an RGB color
     * @param r Red component (0-255)
     * @param g Green component (0-255)
     * @param b Blue component (0-255)
     * @return RGBColor instance
     */
    public static RGBColor rgb(int r, int g, int b) {
        return new RGBColor(r, g, b);
    }

    /**
     * Create an HSL color
     * @param h Hue component (0-360)
     * @param s Saturation component (0-1)
     * @param l Lightness component (0-1)
     * @return HSLColor instance
     */
    public static HSLColor hsl(double h, double s, double l) {
        return new HSLColor(h, s, l);
    }

    /**
     * Create an HSV color
     * @param h Hue component (0-360)
     * @param s Saturation component (0-1)
     * @param v Value component (0-1)
     * @return HSVColor instance
     */
    public static HSVColor hsv(double h, double s, double v) {
        return new HSVColor(h, s, v);
    }

    /**
     * Create a LAB color
     * @param l Lightness component
     * @param a A component
     * @param b B component
     * @return LABColor instance
     */
    public static LABColor lab(double l, double a, double b) {
        return new LABColor(l, a, b);
    }

    /**
     * Create an XYZ color
     * @param x X component
     * @param y Y component
     * @param z Z component
     * @return XYZColor instance
     */
    public static XYZColor xyz(double x, double y, double z) {
        return new XYZColor(x, y, z);
    }
}
