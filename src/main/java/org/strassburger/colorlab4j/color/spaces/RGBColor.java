package org.strassburger.colorlab4j.color.spaces;

import org.strassburger.colorlab4j.color.Color;
import org.strassburger.colorlab4j.color.ColorConverter;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents a color in the RGB color space
 * @see <a href="https://en.wikipedia.org/wiki/RGB_color_space">RGB color space</a>
 */
public class RGBColor extends Color {
    private static final Pattern HEX_PATTERN = Pattern.compile("[0-9a-fA-F]{3}|[0-9a-fA-F]{6}");

    private final int r;
    private final int g;
    private final int b;

    /**
     * @param r Red value (0-255)
     * @param g Green value (0-255)
     * @param b Blue value (0-255)
     * @throws IllegalArgumentException If any of the values are out of range
     */
    public RGBColor(int r, int g, int b) throws IllegalArgumentException {
        if (r < 0 || r > 255) throw new IllegalArgumentException("Red value must be between 0 and 255");
        if (g < 0 || g > 255) throw new IllegalArgumentException("Green value must be between 0 and 255");
        if (b < 0 || b > 255) throw new IllegalArgumentException("Blue value must be between 0 and 255");
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Get the red value of the color (0-255)
     * @return Red value
     */
    public int getRed() {
        return r;
    }

    /**
     * Get the green value of the color (0-255)
     * @return Green value
     */
    public int getGreen() {
        return g;
    }

    /**
     * Get the blue value of the color (0-255)
     */
    public int getBlue() {
        return b;
    }

    /**
     * Get an instance of RGBColor from a HEX color code
     * @param hex HEX color code with an optional leading "#", either 6 digits (e.g. "#ff0000")
     *            or the 3-digit shorthand (e.g. "#f00")
     * @return RGBColor instance
     * @throws IllegalArgumentException If the HEX code is null or invalid
     */
    public static RGBColor fromHex(String hex) throws IllegalArgumentException {
        if (hex == null) throw new IllegalArgumentException("HEX code must not be null");
        String digits = hex.trim();
        if (digits.startsWith("#")) digits = digits.substring(1);
        if (!HEX_PATTERN.matcher(digits).matches()) {
            throw new IllegalArgumentException("Invalid HEX code: \"" + hex + "\" (expected #rrggbb or #rgb)");
        }
        if (digits.length() == 3) {
            digits = "" + digits.charAt(0) + digits.charAt(0)
                    + digits.charAt(1) + digits.charAt(1)
                    + digits.charAt(2) + digits.charAt(2);
        }
        hex = digits;
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new RGBColor(r, g, b);
    }

    /**
     * Get the hex representation of the color
     * @return Hex representation of the color (e.g. "#ff0000")
     */
    @Override
    public String toHex() {
        return String.format("#%02x%02x%02x", getRed(), getGreen(), getBlue());
    }

    @Override
    public RGBColor toRGB() {
        return this;
    }

    @Override
    public HSLColor toHSL() {
        return ColorConverter.RGBtoHSL(this);
    }

    @Override
    public HSVColor toHSV() {
        return ColorConverter.RGBtoHSV(this);
    }

    @Override
    public LABColor toLAB() {
        return ColorConverter.RGBtoLAB(this);
    }

    @Override
    public XYZColor toXYZ() {
        return ColorConverter.RGBtoXYZ(this);
    }

    @Override
    public String toAnsi() {
        return String.format("\u001B[38;2;%d;%d;%dm", getRed(), getGreen(), getBlue());
    }

    @Override
    public String toAnsiBackground() {
        return String.format("\u001B[48;2;%d;%d;%dm", getRed(), getGreen(), getBlue());
    }

    /**
     * @return CSS rgb() function, e.g. "rgb(255,0,0)"
     */
    @Override
    public String toCssString() {
        return "rgb(" + getRed() + "," + getGreen() + "," + getBlue() + ")";
    }

    @Override
    public String toString() {
        return String.format("rgb(%d,%d,%d)", getRed(), getGreen(), getBlue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof RGBColor other &&
                this.r == other.r &&
                this.g == other.g &&
                this.b == other.b;
    }
}
