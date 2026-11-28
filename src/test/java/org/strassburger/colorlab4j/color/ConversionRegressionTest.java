package org.strassburger.colorlab4j.color;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.strassburger.colorlab4j.color.spaces.*;
import org.strassburger.colorlab4j.gradients.spaces.HSLGradient;
import org.strassburger.colorlab4j.gradients.spaces.LABGradient;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class ConversionRegressionTest {

    private static final RGBColor WHITE = new RGBColor(255, 255, 255);
    private static final RGBColor BLACK = new RGBColor(0, 0, 0);
    private static final RGBColor RED = new RGBColor(255, 0, 0);

    /**
     * Converts every 3rd RGB value per channel (86^3 = 636,056 colors, always including 0 and 255)
     * into another space and back, and reports the first color that does not survive.
     */
    private static void assertRoundTrip(String space, Function<RGBColor, Color> to) {
        int[] values = new int[86];
        for (int i = 0; i < 85; i++) values[i] = i * 3;
        values[85] = 255;

        for (int r : values) for (int g : values) for (int b : values) {
            RGBColor color = new RGBColor(r, g, b);
            RGBColor back;
            try {
                back = to.apply(color).toRGB();
            } catch (RuntimeException e) {
                fail(color + " -> " + space + " threw " + e);
                return;
            }
            if (!color.equals(back)) {
                fail(color + " -> " + space + " -> " + back);
            }
        }
    }

    // --- Round trips over the RGB cube ---

    @Test
    public void rgbSurvivesHslRoundTrip() {
        assertRoundTrip("HSL", RGBColor::toHSL);
    }

    @Test
    public void rgbSurvivesHsvRoundTrip() {
        assertRoundTrip("HSV", RGBColor::toHSV);
    }

    @Test
    public void rgbSurvivesXyzRoundTrip() {
        assertRoundTrip("XYZ", RGBColor::toXYZ);
    }

    @Test
    public void rgbSurvivesLabRoundTrip() {
        assertRoundTrip("LAB", RGBColor::toLAB);
    }

    @Test
    public void rgbSurvivesHslToHsvToRgb() {
        assertRoundTrip("HSL->HSV", c -> c.toHSL().toHSV());
    }

    @Test
    public void rgbSurvivesHsvToHslToRgb() {
        assertRoundTrip("HSV->HSL", c -> c.toHSV().toHSL());
    }

    // --- XYZ -> RGB must not normalize by the white point ---

    @Test
    public void d65WhitePointIsWhite() {
        assertEquals(WHITE, new XYZColor(0.95047, 1.0, 1.08883).toRGB());
    }

    @Test
    public void grayKeepsNeutralAfterXyz() {
        RGBColor gray = new RGBColor(128, 128, 128);
        assertEquals(gray, gray.toXYZ().toRGB());
    }

    @Test
    public void neutralLabIsNeutralGray() {
        // L* = 50 with a* = b* = 0 is sRGB 119 gray
        assertEquals(new RGBColor(119, 119, 119), new LABColor(50, 0, 0).toRGB());
    }

    // --- Hue 360 is the same as hue 0 ---

    @Test
    public void hslHue360IsRed() {
        assertEquals(RED, new HSLColor(360, 1, 0.5).toRGB());
        assertEquals(new HSLColor(0, 1, 0.5).toRGB(), new HSLColor(360, 1, 0.5).toRGB());
    }

    @Test
    public void hsvHue360IsRed() {
        assertEquals(RED, new HSVColor(360, 1, 1).toRGB());
        assertEquals(new HSVColor(0, 1, 1).toRGB(), new HSVColor(360, 1, 1).toRGB());
    }

    // --- Channels are rounded, not truncated ---

    @Test
    public void hslChannelsAreRounded() {
        // l = 0.5 gray is 127.5 -> rounds to 128 (truncation gave 127)
        assertEquals(new RGBColor(128, 128, 128), new HSLColor(0, 0, 0.5).toRGB());
    }

    @Test
    public void hsvChannelsAreRounded() {
        assertEquals(new RGBColor(128, 128, 128), new HSVColor(0, 0, 0.5).toRGB());
    }

    // --- Floating-point overshoot must not throw ---

    @ParameterizedTest
    @ValueSource(strings = {"#00000a", "#0a0000", "#fffff5", "#000001", "#fefeff"})
    public void nearBlackAndNearWhiteConvertToHsl(String hex) {
        HSLColor hsl = assertDoesNotThrow(() -> RGBColor.fromHex(hex).toHSL());
        assertTrue(hsl.getSaturation() <= 1 && hsl.getLightness() <= 1, hsl.toString());
    }

    @Test
    public void pureBlueChannelHasFullHslSaturation() {
        assertEquals(1.0, new RGBColor(0, 0, 10).toHSL().getSaturation(), 1e-9);
    }

    @Test
    public void whiteConvertsToLab() {
        LABColor lab = assertDoesNotThrow(WHITE::toLAB);
        assertEquals(100, lab.getL(), 0.01);
        assertEquals(0, lab.getA(), 0.01);
        assertEquals(0, lab.getB(), 0.01);
    }

    @Test
    public void blackConvertsToLab() {
        LABColor lab = BLACK.toLAB();
        assertEquals(0, lab.getL(), 0.01);
        assertEquals(0, lab.getA(), 0.01);
        assertEquals(0, lab.getB(), 0.01);
    }

    @Test
    public void veryBrightXyzConvertsToLabWithoutThrowing() {
        // XYZColor accepts values far above the white point; LAB must stay in range
        LABColor lab = assertDoesNotThrow(() -> new XYZColor(95, 100, 108).toLAB());
        assertEquals(100, lab.getL(), 0.0);
    }

    // --- Out-of-gamut LAB must not throw ---

    @ParameterizedTest
    @ValueSource(doubles = {-128, 128})
    public void extremeLabValuesConvert(double ab) {
        for (double l : new double[]{0, 50, 100}) {
            LABColor lab = new LABColor(l, ab, ab);
            assertDoesNotThrow(lab::toXYZ, lab.toString());
            assertDoesNotThrow(lab::toRGB, lab.toString());
            assertDoesNotThrow(lab::toHSL, lab.toString());
            assertDoesNotThrow(lab::toHSV, lab.toString());
        }
    }

    @Test
    public void outOfGamutLabClampsToNonNegativeXyz() {
        XYZColor xyz = new LABColor(0, -128, 0).toXYZ();
        assertTrue(xyz.getX() >= 0 && xyz.getY() >= 0 && xyz.getZ() >= 0, xyz.toString());
    }

    // --- The gradient factories that previously failed ---

    @Test
    public void gradientFactoriesAcceptPreviouslyFailingColors() {
        assertDoesNotThrow(() -> LABGradient.fromHex("#ffffff", "#000000").getColors(5));
        assertDoesNotThrow(() -> HSLGradient.fromHex("#00000a", "#ffffff").getColors(5));
    }
}
