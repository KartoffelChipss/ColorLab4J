package org.strassburger.colorlab4j.color.spaces;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.colorlab4j.color.ColorConverter;

/**
 * Represents a color in the XYZ color space
 * @see <a href="https://en.wikipedia.org/wiki/CIE_1931_color_space">XYZ color space</a>
 */
public class XYZColor extends Color {
    private final double x;
    private final double y;
    private final double z;

    /**
     * @param x X value (0.0-95.047)
     * @param y Y value (0.0-100.0)
     * @param z Z value (0.0-108.883)
     * @throws IllegalArgumentException If any of the values are out of range
     */
    public XYZColor(double x, double y, double z) throws IllegalArgumentException {
        if (x < 0 || x > 95.047) throw new IllegalArgumentException("X value must be between 0.0 and 95.047");
        if (y < 0 || y > 100.0) throw new IllegalArgumentException("Y value must be between 0.0 and 100.0");
        if (z < 0 || z > 108.883) throw new IllegalArgumentException("Z value must be between 0.0 and 108.883");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Get the X value of the color (Long-wavelength sensitive cones) (0.0-95.047)
     * @return X value
     */
    public double getX() {
        return x;
    }

    /**
     * Get the Y value of the color (Luminance) (0.0-100.0)
     * @return Y value
     */
    public double getY() {
        return y;
    }

    /**
     * Get the Z value of the color (Short-wavelength sensitive cones) (0.0-108.883)
     * @return Z value
     */
    public double getZ() {
        return z;
    }

    @Override
    public RGBColor toRGB() {
        return ColorConverter.XYZtoRGB(this);
    }

    @Override
    public HSLColor toHSL() {
        return ColorConverter.XYZtoHSL(this);
    }

    @Override
    public HSVColor toHSV() {
        return ColorConverter.XYZtoHSV(this);
    }

    @Override
    public LABColor toLAB() {
        return ColorConverter.XYZtoLAB(this);
    }

    @Override
    public XYZColor toXYZ() {
        return this;
    }

    @Override
    public String toCssString() {
        return String.format("xyz(%f, %f, %f)", getX(), getY(), getZ());
    }

    @Override
    public String toString() {
        return String.format("xyz(%f, %f, %f)", getX(), getY(), getZ());
    }

    /**
     * Get the XYZ representation of a color from a hex string
     * @param hex Hex representation of the color (e.g. "#ff0000")
     * @return XYZ representation of the color
     * @throws IllegalArgumentException If the HEX code is invalid
     */
    public static XYZColor fromHex(String hex) throws IllegalArgumentException {
        return RGBColor.fromHex(hex).toXYZ();
    }
}
