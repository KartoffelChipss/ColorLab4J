package org.strassburger.colorlab4j.color;

import org.junit.jupiter.api.Test;
import org.strassburger.colorlab4j.color.spaces.*;

import static org.junit.jupiter.api.Assertions.*;

public class ColorConverterTest {

    @Test
    public void testRGBtoHSL() {
        RGBColor rgb = new RGBColor(255, 0, 0);
        HSLColor hsl = ColorConverter.RGBtoHSL(rgb);
        assertEquals(0, hsl.getHue(), 0.01);
        assertEquals(1, hsl.getSaturation(), 0.01);
        assertEquals(0.5, hsl.getLightness(), 0.01);
    }

    @Test
    public void testRGBtoHSV() {
        RGBColor rgb = new RGBColor(255, 0, 0);
        HSVColor hsv = ColorConverter.RGBtoHSV(rgb);
        assertEquals(0, hsv.getHue(), 0.01);
        assertEquals(1, hsv.getSaturation(), 0.01);
        assertEquals(1, hsv.getValue(), 0.01);
    }

    @Test
    public void testRGBtoLAB() {
        RGBColor rgb = new RGBColor(255, 0, 0);
        LABColor lab = ColorConverter.RGBtoLAB(rgb);
        assertEquals(53.24, lab.getL(), 0.01);
        assertEquals(80.09, lab.getA(), 0.01);
        assertEquals(67.20, lab.getB(), 0.01);
    }

    @Test
    public void testRGBtoXYZ() {
        RGBColor rgb = new RGBColor(255, 0, 0);
        XYZColor xyz = ColorConverter.RGBtoXYZ(rgb);
        assertEquals(0.4124564, xyz.getX(), 0.01);
        assertEquals(0.2126729, xyz.getY(), 0.01);
        assertEquals(0.0193339, xyz.getZ(), 0.01);
    }

    @Test
    public void testHSLtoRGB() {
        HSLColor hsl = new HSLColor(0, 1, 0.5);
        RGBColor rgb = ColorConverter.HSLtoRGB(hsl);
        assertEquals(255, rgb.getRed());
        assertEquals(0, rgb.getGreen());
        assertEquals(0, rgb.getBlue());
    }

    @Test
    public void testHSVtoRGB() {
        HSVColor hsv = new HSVColor(0, 1, 1);
        RGBColor rgb = ColorConverter.HSVtoRGB(hsv);
        assertEquals(255, rgb.getRed());
        assertEquals(0, rgb.getGreen());
        assertEquals(0, rgb.getBlue());
    }

    @Test
    public void testLABtoRGB() {
        LABColor lab = new LABColor(53.24, 80.09, 67.20);
        RGBColor rgb = ColorConverter.LABtoRGB(lab);
        assertEquals(255, rgb.getRed());
        assertEquals(0, rgb.getGreen());
        assertEquals(0, rgb.getBlue());
    }

    @Test
    public void testXYZtoRGB() {
        XYZColor xyz = new XYZColor(0.4124564, 0.2126729, 0.0193339);
        RGBColor rgb = ColorConverter.XYZtoRGB(xyz);
        assertEquals(255, rgb.getRed());
        assertEquals(0, rgb.getGreen());
        assertEquals(0, rgb.getBlue());
    }

    @Test
    public void testHSVtoHSL() {
        HSVColor hsv = new HSVColor(0, 1, 1);
        HSLColor hsl = ColorConverter.HSVtoHSL(hsv);
        assertEquals(0, hsl.getHue(), 0.01);
        assertEquals(1, hsl.getSaturation(), 0.01);
        assertEquals(0.5, hsl.getLightness(), 0.01);
    }

    @Test
    public void testHSLtoHSV() {
        HSLColor hsl = new HSLColor(0, 1, 0.5);
        HSVColor hsv = ColorConverter.HSLtoHSV(hsl);
        assertEquals(0, hsv.getHue(), 0.01);
        assertEquals(1, hsv.getSaturation(), 0.01);
        assertEquals(1, hsv.getValue(), 0.01);
    }

    @Test
    public void testXYZtoLAB() {
        XYZColor xyz = new XYZColor(0.4124564, 0.2126729, 0.0193339);
        LABColor lab = ColorConverter.XYZtoLAB(xyz);
        assertEquals(53.24, lab.getL(), 0.01);
        assertEquals(80.09, lab.getA(), 0.01);
        assertEquals(67.20, lab.getB(), 0.01);
    }

    @Test
    public void testLABtoXYZ() {
        LABColor lab = new LABColor(53.24, 80.09, 67.20);
        XYZColor xyz = ColorConverter.LABtoXYZ(lab);
        assertEquals(0.4124564, xyz.getX(), 0.01);
        assertEquals(0.2126729, xyz.getY(), 0.01);
        assertEquals(0.0193339, xyz.getZ(), 0.01);
    }
}