package org.strassburger.colorlab4j.color.spaces;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.colorlab4j.color.ColorConverter;

import java.util.Locale;
import java.util.Objects;

/**
 * Represents a color in the CIE 1931 XYZ color space, relative to the D65 white point.
 * <p>
 * Values use the <b>0-1 scale</b>, where Y is relative luminance: black is (0, 0, 0) and
 * sRGB white is (0.95047, 1.0, 1.08883). All colors converted from sRGB fall within that box.
 * This is the same scale CSS uses for {@code color(xyz-d65 ...)}.
 * <p>
 * Note that some libraries use a 0-100 scale instead (white = 95.047, 100, 108.883).
 * To use such values here, divide them by 100.
 * <p>
 * The constructor only rejects negative values and values above 95.047, 100 and 108.883.
 * These upper limits are deliberately loose: values above the white point are accepted,
 * and conversions to other color spaces clamp them.
 *
 * @see <a href="https://en.wikipedia.org/wiki/CIE_1931_color_space">XYZ color space</a>
 */
public class XYZColor extends Color {
    private final double x;
    private final double y;
    private final double z;

    /**
     * Create an XYZ color. Values use the 0-1 scale, see {@link XYZColor}.
     * @param x X value, typically 0.0-0.95047 (must be 0.0-95.047)
     * @param y Y value (relative luminance), typically 0.0-1.0 (must be 0.0-100.0)
     * @param z Z value, typically 0.0-1.08883 (must be 0.0-108.883)
     * @throws IllegalArgumentException If any value is negative or above its upper limit
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
     * Get the X value of the color. X is a mix of the cone responses that is non-negative
     * everywhere; it has no direct perceptual meaning.
     * @return X value, typically 0.0-0.95047 (0.95047 for sRGB white)
     */
    public double getX() {
        return x;
    }

    /**
     * Get the Y value of the color, which is its relative luminance.
     * @return Y value, typically 0.0-1.0 (1.0 for sRGB white)
     */
    public double getY() {
        return y;
    }

    /**
     * Get the Z value of the color, which roughly follows the short-wavelength (blue) cone response.
     * @return Z value, typically 0.0-1.08883 (1.08883 for sRGB white)
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

    /**
     * CSS has no xyz() function; XYZ colors are written with color() and the D65 XYZ space,
     * which uses the same 0-1 scale as this library.
     * @return CSS color() function, e.g. "color(xyz-d65 0.4125 0.2127 0.0193)"
     */
    @Override
    public String toCssString() {
        return "color(xyz-d65 " + formatCssNumber(getX()) + " "
                + formatCssNumber(getY()) + " "
                + formatCssNumber(getZ()) + ")";
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "xyz(%f, %f, %f)", getX(), getY(), getZ());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof XYZColor other &&
                Double.compare(this.x, other.x) == 0 &&
                Double.compare(this.y, other.y) == 0 &&
                Double.compare(this.z, other.z) == 0;
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
