package org.strassburger.colorlab4j.color.spaces;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.colorlab4j.color.ColorConverter;

/**
 * Represents a color in the HSL color space
 * @see <a href="https://en.wikipedia.org/wiki/HSL_and_HSV">HSL color space</a>
 */
public class HSLColor extends Color {
    private final double h;
    private final double s;
    private final double l;

    /**
     * @param h Hue value (Degrees, 0-360)
     * @param s Saturation value (Fraction, 0.0-1.0)
     * @param l Lightness value (Fraction, 0.0-1.0)
     * @throws IllegalArgumentException If any of the values are out of range
     */
    public HSLColor(double h, double s, double l) throws IllegalArgumentException {
        if (h < 0 || h > 360) throw new IllegalArgumentException("Hue value must be between 0 and 360");
        if (s < 0 || s > 1) throw new IllegalArgumentException("Saturation value must be between 0.0 and 1.0");
        if (l < 0 || l > 1) throw new IllegalArgumentException("Lightness value must be between 0.0 and 1.0");
        this.h = h;
        this.s = s;
        this.l = l;
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
     * Get the lightness value of the color (Fraction, 0.0-1.0)
     * @return Lightness value
     */
    public double getLightness() {
        return l;
    }

    @Override
    public RGBColor toRGB() {
        return ColorConverter.HSLtoRGB(this);
    }

    @Override
    public HSLColor toHSL() {
        return this;
    }

    @Override
    public HSVColor toHSV() {
        return ColorConverter.HSLtoHSV(this);
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
        return String.format("hsl(%f, %f%%, %f%%)", getHue(), getSaturation() * 100, getLightness() * 100);
    }

    @Override
    public String toString() {
        return String.format("hsl(%f, %f, %f)", getHue(), getSaturation(), getLightness());
    }

    /**
     * Get the HSL representation of a color from a hex string
     * @param hex Hex representation of the color (e.g. "#ff0000")
     * @return HSL representation of the color
     * @throws IllegalArgumentException If the HEX code is invalid
     */
    public static HSLColor fromHex(String hex) throws IllegalArgumentException {
        return RGBColor.fromHex(hex).toHSL();
    }
}
