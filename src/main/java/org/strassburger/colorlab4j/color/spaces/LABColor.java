package org.strassburger.colorlab4j.color.spaces;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.colorlab4j.color.ColorConverter;

/**
 * Represents a color in the LAB (CIELAB) color space
 * @see <a href="https://en.wikipedia.org/wiki/CIELAB_color_space">CIELAB color space</a>
 */
public class LABColor extends Color {
    private final double l;
    private final double a;
    private final double b;

    /**
     * @param l Lightness value (0-100)
     * @param a a value (-128-128)
     * @param b b value (-128-128)
     * @throws IllegalArgumentException If any of the values are out of range
     */
    public LABColor(double l, double a, double b) throws IllegalArgumentException {
        if (l < 0 || l > 100) throw new IllegalArgumentException("Lightness value must be between 0 and 100");
        if (a < -128 || a > 128) throw new IllegalArgumentException("a value must be between -128 and 128");
        if (b < -128 || b > 128) throw new IllegalArgumentException("b value must be between -128 and 128");
        this.l = l;
        this.a = a;
        this.b = b;
    }

    /**
     * Get the lightness value of the color (0-100)
     * @return Lightness value
     */
    public double getL() {
        return l;
    }

    /**
     * Get the a value of the color (Red-Green axis) (-128-128)
     * @return a value
     */
    public double getA() {
        return a;
    }

    /**
     * Get the b value of the color (Yellow-Blue axis) (-128-128)
     * @return b value
     */
    public double getB() {
        return b;
    }

    @Override
    public RGBColor toRGB() {
        return ColorConverter.LABtoRGB(this);
    }

    @Override
    public HSLColor toHSL() {
        return ColorConverter.LABtoHSL(this);
    }

    @Override
    public HSVColor toHSV() {
        return ColorConverter.LABtoHSV(this);
    }

    @Override
    public LABColor toLAB() {
        return this;
    }

    @Override
    public XYZColor toXYZ() {
        return ColorConverter.LABtoXYZ(this);
    }

    @Override
    public String toCssString() {
        return String.format("lab(%f, %f, %f)", getL(), getA(), getB());
    }

    @Override
    public String toString() {
        return String.format("lab(%f, %f, %f)", getL(), getA(), getB());
    }

    /**
     * Get the LAB representation of a color from a hex string
     * @param hex Hex representation of the color (e.g. "#ff0000")
     * @return LAB representation of the color
     * @throws IllegalArgumentException If the HEX code is invalid
     */
    public static LABColor fromHex(String hex) throws IllegalArgumentException {
        return RGBColor.fromHex(hex).toLAB();
    }
}
