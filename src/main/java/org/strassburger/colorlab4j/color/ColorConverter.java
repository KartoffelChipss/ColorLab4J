package org.strassburger.colorlab4j.color;

import org.strassburger.colorlab4j.color.spaces.*;

public class ColorConverter {

    public static HSLColor RGBtoHSL(RGBColor rgb) {
        // Formula from https://www.rapidtables.com/convert/color/rgb-to-hsl.html

        double r = rgb.getRed() / 255.0;
        double g = rgb.getGreen() / 255.0;
        double b = rgb.getBlue() / 255.0;

        double max = Math.max(r, Math.max(g, b));
        double min = Math.min(r, Math.min(g, b));
        double chroma = max - min;
        double lightness = (max + min) / 2;

        double hue = 0;
        if (chroma > 0) {
            if (max == r) {
                hue = 60 * ((g - b) / chroma % 6);
            } else if (max == g) {
                hue = 60 * ((b - r) / chroma + 2);
            } else {
                hue = 60 * ((r - g) / chroma + 4);
            }
        }

        hue = (hue + 360) % 360;

        double saturation = (chroma == 0) ? 0 : chroma / (1 - Math.abs(2 * lightness - 1));

        return new HSLColor(hue, saturation, lightness);
    }

    public static HSVColor RGBtoHSV(RGBColor rgb) {
        // Formula from https://www.rapidtables.com/convert/color/rgb-to-hsv.html

        double r = rgb.getRed() / 255.0;
        double g = rgb.getGreen() / 255.0;
        double b = rgb.getBlue() / 255.0;

        double max = Math.max(r, Math.max(g, b));
        double min = Math.min(r, Math.min(g, b));

        double chroma = max - min;

        double hue = 0;
        if (chroma > 0) {
            if (max == r) {
                hue = 60 * ((g - b) / chroma % 6);
            } else if (max == g) {
                hue = 60 * ((b - r) / chroma + 2);
            } else {
                hue = 60 * ((r - g) / chroma + 4);
            }
        }

        hue = (hue + 360) % 360;

        double saturation = (max == 0) ? 0 : chroma / max;

        return new HSVColor(hue, saturation, max);
    }

    public static LABColor RGBtoLAB(RGBColor rgb) {
        XYZColor xyz = RGBtoXYZ(rgb);
        return XYZtoLAB(xyz);
    }

    public static XYZColor RGBtoXYZ(RGBColor rgb) {
        double r = rgb.getRed() / 255.0;
        double g = rgb.getGreen() / 255.0;
        double b = rgb.getBlue() / 255.0;

        // Gamma correction
        r = (r <= 0.04045) ? r / 12.92 : Math.pow((r + 0.055) / 1.055, 2.4);
        g = (g <= 0.04045) ? g / 12.92 : Math.pow((g + 0.055) / 1.055, 2.4);
        b = (b <= 0.04045) ? b / 12.92 : Math.pow((b + 0.055) / 1.055, 2.4);

        // Apply transformation matrix
        double x = r * 0.4124564 + g * 0.3575761 + b * 0.1804375;
        double y = r * 0.2126729 + g * 0.7151522 + b * 0.0721750;
        double z = r * 0.0193339 + g * 0.1191920 + b * 0.9503041;

        return new XYZColor(x, y, z);
    }

    public static RGBColor HSLtoRGB(HSLColor hsl) {
        double h = hsl.getHue();
        double s = hsl.getSaturation();
        double l = hsl.getLightness();

        double chroma = (1 - Math.abs(2 * l - 1)) * s;
        double x = chroma * (1 - Math.abs((h / 60) % 2 - 1));
        double m = l - chroma / 2;

        double r = 0, g = 0, b = 0;

        if (h >= 0 && h < 60) {
            r = chroma;
            g = x;
            b = 0;
        } else if (h >= 60 && h < 120) {
            r = x;
            g = chroma;
            b = 0;
        } else if (h >= 120 && h < 180) {
            r = 0;
            g = chroma;
            b = x;
        } else if (h >= 180 && h < 240) {
            r = 0;
            g = x;
            b = chroma;
        } else if (h >= 240 && h < 300) {
            r = x;
            g = 0;
            b = chroma;
        } else if (h >= 300 && h < 360) {
            r = chroma;
            g = 0;
            b = x;
        }

        // Add m to each color to adjust for lightness
        r = (r + m) * 255;
        g = (g + m) * 255;
        b = (b + m) * 255;

        return new RGBColor((int) r, (int) g, (int) b);
    }

    public static RGBColor HSVtoRGB(HSVColor hsv) {
        double h = hsv.getHue();
        double s = hsv.getSaturation();
        double v = hsv.getValue();

        double chroma = v * s;
        double x = chroma * (1 - Math.abs((h / 60) % 2 - 1));
        double m = v - chroma;

        double r = 0, g = 0, b = 0;

        if (h >= 0 && h < 60) {
            r = chroma;
            g = x;
            b = 0;
        } else if (h >= 60 && h < 120) {
            r = x;
            g = chroma;
            b = 0;
        } else if (h >= 120 && h < 180) {
            r = 0;
            g = chroma;
            b = x;
        } else if (h >= 180 && h < 240) {
            r = 0;
            g = x;
            b = chroma;
        } else if (h >= 240 && h < 300) {
            r = x;
            g = 0;
            b = chroma;
        } else if (h >= 300 && h < 360) {
            r = chroma;
            g = 0;
            b = x;
        }

        // Add m to each color to adjust for brightness (value)
        r = (r + m) * 255;
        g = (g + m) * 255;
        b = (b + m) * 255;

        return new RGBColor((int) r, (int) g, (int) b);
    }

    public static RGBColor LABtoRGB(LABColor lab) {
        XYZColor xyz = LABtoXYZ(lab);
        return XYZtoRGB(xyz);
    }

    public static RGBColor XYZtoRGB(XYZColor xyz) {
        double x = xyz.getX() / 0.95047;
        double y = xyz.getY();
        double z = xyz.getZ() / 1.08883;

        // Inverse of the transformation matrix
        double rLinear = 3.2404542 * x - 1.5371385 * y - 0.4985314 * z;
        double gLinear = -0.9692660 * x + 1.8760108 * y + 0.0415560 * z;
        double bLinear = 0.0556434 * x - 0.2040259 * y + 1.0572252 * z;

        // Gamma correction
        double r = (rLinear <= 0.0031308) ? 12.92 * rLinear : (1.055 * Math.pow(rLinear, 1 / 2.4)) - 0.055;
        double g = (gLinear <= 0.0031308) ? 12.92 * gLinear : (1.055 * Math.pow(gLinear, 1 / 2.4)) - 0.055;
        double b = (bLinear <= 0.0031308) ? 12.92 * bLinear : (1.055 * Math.pow(bLinear, 1 / 2.4)) - 0.055;

        // Clamp values to [0, 1] and convert to [0, 255]
        r = Math.min(Math.max(r, 0), 1);
        g = Math.min(Math.max(g, 0), 1);
        b = Math.min(Math.max(b, 0), 1);

        int rInt = (int) Math.round(r * 255);
        int gInt = (int) Math.round(g * 255);
        int bInt = (int) Math.round(b * 255);

        return new RGBColor(rInt, gInt, bInt);
    }

    public static HSLColor HSVtoHSL(HSVColor hsv) {
        double h = hsv.getHue();
        double s = hsv.getSaturation();
        double v = hsv.getValue();

        double l = (2 - s) * v / 2;
        double newS = (l == 0 || l == 1) ? 0 : (v - l) / Math.min(l, 1 - l);

        return new HSLColor(h, newS, l);
    }

    public static HSVColor HSLtoHSV(HSLColor hsl) {
        double h = hsl.getHue();
        double s = hsl.getSaturation();
        double l = hsl.getLightness();

        double v = l + s * Math.min(l, 1 - l);
        double newS = (v == 0) ? 0 : 2 * (v - l) / v;

        return new HSVColor(h, newS, v);
    }

    public static LABColor XYZtoLAB(XYZColor xyz) {
        // Normalize XYZ values
        double x = xyz.getX() / 0.95047;
        double y = xyz.getY();
        double z = xyz.getZ() / 1.08883;

        // XYZ to LAB transformation
        double fx = (x > 0.008856) ? Math.cbrt(x) : (7.787 * x + 16.0 / 116.0);
        double fy = (y > 0.008856) ? Math.cbrt(y) : (7.787 * y + 16.0 / 116.0);
        double fz = (z > 0.008856) ? Math.cbrt(z) : (7.787 * z + 16.0 / 116.0);

        double L = 116.0 * fy - 16.0;
        double a = 500.0 * (fx - fy);
        double b = 200.0 * (fy - fz);

        return new LABColor(L, a, b);
    }

    public static XYZColor LABtoXYZ(LABColor lab) {
        double Yp = (lab.getL() + 16.0) / 116.0;
        double Xp = lab.getA() / 500.0 + Yp;
        double Zp = Yp - lab.getB() / 200.0;

        double Y = (Yp > 0.206893034) ? Math.pow(Yp, 3) : (Yp - 16.0 / 116.0) / 7.787;
        double X = (Xp > 0.206893034) ? Math.pow(Xp, 3) : (Xp - 16.0 / 116.0) / 7.787;
        double Z = (Zp > 0.206893034) ? Math.pow(Zp, 3) : (Zp - 16.0 / 116.0) / 7.787;

        X *= 0.95047;
        Y *= 1.00000;
        Z *= 1.08883;

        return new XYZColor(X, Y, Z);
    }

    public static HSLColor LABtoHSL(LABColor lab) {
        XYZColor xyz = LABtoXYZ(lab);
        RGBColor rgb = XYZtoRGB(xyz);
        return RGBtoHSL(rgb);
    }

    public static HSVColor LABtoHSV(LABColor lab) {
        XYZColor xyz = LABtoXYZ(lab);
        RGBColor rgb = XYZtoRGB(xyz);
        return RGBtoHSV(rgb);
    }

    public static HSLColor XYZtoHSL(XYZColor xyz) {
        RGBColor rgb = XYZtoRGB(xyz);
        return RGBtoHSL(rgb);
    }

    public static HSVColor XYZtoHSV(XYZColor xyz) {
        RGBColor rgb = XYZtoRGB(xyz);
        return RGBtoHSV(rgb);
    }
}