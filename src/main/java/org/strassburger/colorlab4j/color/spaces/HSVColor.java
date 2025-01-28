package org.strassburger.colorlab4j.color.spaces;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.colorlab4j.color.ColorConverter;

/**
 * Represents a color in the HSV color space
 * @see <a href="https://en.wikipedia.org/wiki/HSL_and_HSV">HSV color space</a>
 */
public class HSVColor extends Color {
    private final double h;
    private final double s;
    private final double v;

    /**
     * @param h Hue value (Degrees, 0-360)
     * @param s Saturation value (Fraction, 0.0-1.0)
     * @param v Value value (Fraction, 0.0-1.0)
     * @throws IllegalArgumentException If any of the values are out of range
     */
    public HSVColor(double h, double s, double v) throws IllegalArgumentException {
        if (h < 0 || h > 360) throw new IllegalArgumentException("Hue value must be between 0 and 360");
        if (s < 0 || s > 1) throw new IllegalArgumentException("Saturation value must be between 0.0 and 1.0");
        if (v < 0 || v > 1) throw new IllegalArgumentException("Value value must be between 0.0 and 1.0");
        this.h = h;
        this.s = s;
        this.v = v;
    }

    /**
     * Get the hue value of the color (Degrees, 0-360)
     * @return Hue value
     */
    public double getHue() {
        return h;
    }

    /**
     * Get the saturation value of the color (Fraction, 0.0-1.0)
     * @return Saturation value
     */
    public double getSaturation() {
        return s;
    }

    /**
     * Get the value value (bruh) of the color (Fraction, 0.0-1.0)
     * @return Value value
     */
    public double getValue() {
        return v;
    }

    @Override
    public RGBColor toRGB() {
        return ColorConverter.HSVtoRGB(this);
    }

    @Override
    public HSLColor toHSL() {
        return ColorConverter.HSVtoHSL(this);
    }

    @Override
    public HSVColor toHSV() {
        return this;
    }

    @Override
    public LABColor toLAB() {
        return ColorConverter.RGBtoLAB(toRGB());
    }

    @Override
    public XYZColor toXYZ() {
        return ColorConverter.RGBtoXYZ(toRGB());
    }

    @Override
    public String toCssString() {
        return String.format("hsv(%f, %f%%, %f%%)", getHue(), getSaturation() * 100, getValue() * 100);
    }

    @Override
    public String toString() {
        return String.format("hsv(%f, %f, %f)", getHue(), getSaturation(), getValue());
    }

    /**
     * Get the HSV representation of a color from a hex string
     * @param hex Hex representation of the color (e.g. "#ff0000")
     * @return HSV representation of the color
     * @throws IllegalArgumentException If the HEX code is invalid
     */
    public static HSVColor fromHex(String hex) throws IllegalArgumentException {
        return RGBColor.fromHex(hex).toHSV();
    }
}
