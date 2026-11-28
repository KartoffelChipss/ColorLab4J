package org.strassburger.colorlab4j.gradients;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.strassburger.colorlab4j.color.spaces.*;
import org.strassburger.colorlab4j.gradients.spaces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GradientInterpolationTest {

    private static final RGBColor RED = new RGBColor(255, 0, 0);
    private static final RGBColor GREEN = new RGBColor(0, 255, 0);
    private static final RGBColor BLUE = new RGBColor(0, 0, 255);
    private static final RGBColor BLACK = new RGBColor(0, 0, 0);
    private static final RGBColor WHITE = new RGBColor(255, 255, 255);

    static Stream<Arguments> hexFactories() {
        return Stream.of(
                Arguments.of("RGB", (Function<String[], Gradient<?>>) RGBGradient::fromHex),
                Arguments.of("HSL", (Function<String[], Gradient<?>>) HSLGradient::fromHex),
                Arguments.of("HSV", (Function<String[], Gradient<?>>) HSVGradient::fromHex),
                Arguments.of("LAB", (Function<String[], Gradient<?>>) LABGradient::fromHex),
                Arguments.of("XYZ", (Function<String[], Gradient<?>>) XYZGradient::fromHex)
        );
    }

    // --- 9a: middle stops are kept ---

    @Test
    public void middleStopAppearsInRgbGradient() {
        List<RGBColor> result = new RGBGradient(RED, GREEN, BLUE).getColors(2);
        assertEquals(List.of(
                RED,
                new RGBColor(128, 128, 0),
                GREEN,
                new RGBColor(0, 128, 128),
                BLUE
        ), result);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("hexFactories")
    public void everyStopAppearsAtItsPosition(String name, Function<String[], Gradient<?>> factory) {
        Gradient<?> gradient = factory.apply(new String[]{"#ff0000", "#00ff00", "#0000ff", "#ffff00"});
        // 6 steps over 3 segments = 2 per segment: stop, 2 colors, stop, 2 colors, stop, 2 colors, stop
        List<?> result = gradient.getColors(6);
        assertEquals(10, result.size());
        assertEquals(gradient.colors.get(0), result.get(0));
        assertEquals(gradient.colors.get(1), result.get(3));
        assertEquals(gradient.colors.get(2), result.get(6));
        assertEquals(gradient.colors.get(3), result.get(9));
    }

    @Test
    public void middleStopsAreKeptWhenStartAndEndAreExcluded() {
        List<RGBColor> result = new RGBGradient(RED, GREEN, BLUE).getColors(2, false);
        assertEquals(List.of(new RGBColor(128, 128, 0), GREEN, new RGBColor(0, 128, 128)), result);
    }

    @Test
    public void zeroStepsReturnsAllStops() {
        assertEquals(List.of(RED, GREEN, BLUE), new RGBGradient(RED, GREEN, BLUE).getColors(0));
        assertEquals(List.of(GREEN), new RGBGradient(RED, GREEN, BLUE).getColors(0, false));
    }

    // --- 9b: no steps are lost ---

    @Test
    public void resultSizeMatchesTheDocumentedFormula() {
        for (int stops = 2; stops <= 5; stops++) {
            List<RGBColor> colors = new ArrayList<>();
            for (int i = 0; i < stops; i++) colors.add(new RGBColor(i * 50, 0, 0));
            RGBGradient gradient = new RGBGradient(colors);

            for (int steps = 0; steps <= 12; steps++) {
                String context = stops + " stops, " + steps + " steps";
                assertEquals(steps + stops, gradient.getColors(steps, true).size(), context);
                assertEquals(steps + stops - 2, gradient.getColors(steps, false).size(), context);
            }
        }
    }

    @Test
    public void remainderStepsGoToTheFirstSegments() {
        // 5 steps over 2 segments: 3 between red and green, 2 between green and blue
        List<RGBColor> result = new RGBGradient(RED, GREEN, BLUE).getColors(5);
        assertEquals(8, result.size());
        assertEquals(GREEN, result.get(4));
    }

    @Test
    public void fewerStepsThanSegmentsStillProducesColors() {
        // previously 1 / 2 = 0 steps per segment, returning just [red, blue]
        List<RGBColor> result = new RGBGradient(RED, GREEN, BLUE).getColors(1);
        assertEquals(List.of(RED, new RGBColor(128, 128, 0), GREEN, BLUE), result);
    }

    // --- 10: RGB steps are not whole numbers ---

    @Test
    public void rgbStepsAreEvenlySpaced() {
        // true values are 2.5, 5, 7.5; whole-number deltas gave 2, 4, 6
        List<RGBColor> result = new RGBGradient(BLACK, new RGBColor(10, 10, 10)).getColors(3);
        assertEquals(List.of(
                BLACK,
                new RGBColor(3, 3, 3),
                new RGBColor(5, 5, 5),
                new RGBColor(8, 8, 8),
                new RGBColor(10, 10, 10)
        ), result);
    }

    @Test
    public void rgbGradientWithMoreStepsThanChannelRangeStillMoves() {
        // previously 255 / 301 = 0, so all 300 colors in between were black
        List<RGBColor> result = new RGBGradient(BLACK, WHITE).getColors(300);
        assertEquals(302, result.size());
        assertNotEquals(BLACK, result.get(1));
        assertEquals(new RGBColor(127, 127, 127), result.get(150));
        assertEquals(new RGBColor(254, 254, 254), result.get(300));

        for (int i = 1; i < result.size(); i++) {
            assertTrue(result.get(i).getRed() >= result.get(i - 1).getRed(),
                    "not monotonic at index " + i + ": " + result.get(i - 1) + " -> " + result.get(i));
        }
    }

    @Test
    public void rgbGradientWorksInBothDirections() {
        List<RGBColor> result = new RGBGradient(new RGBColor(10, 10, 10), BLACK).getColors(3);
        assertEquals(List.of(
                new RGBColor(10, 10, 10),
                new RGBColor(8, 8, 8),
                new RGBColor(5, 5, 5),
                new RGBColor(3, 3, 3),
                BLACK
        ), result);
    }

    // --- Other color spaces ---

    @Test
    public void hslValuesAreEvenlySpaced() {
        List<HSLColor> result = new HSLGradient(new HSLColor(0, 0, 0), new HSLColor(0, 1, 1)).getColors(3);
        assertEquals(0.25, result.get(1).getSaturation(), 1e-9);
        assertEquals(0.50, result.get(2).getSaturation(), 1e-9);
        assertEquals(0.75, result.get(3).getLightness(), 1e-9);
    }

    @Test
    public void hueTakesTheShorterWayAroundTheWheel() {
        // 350 -> 10 passes through 0, not through 180
        HSLColor forward = new HSLGradient(new HSLColor(350, 1, 0.5), new HSLColor(10, 1, 0.5)).getColors(1).get(1);
        HSVColor backward = new HSVGradient(new HSVColor(10, 1, 1), new HSVColor(350, 1, 1)).getColors(1).get(1);
        assertEquals(0, forward.getHue(), 1e-9);
        assertEquals(0, backward.getHue(), 1e-9);
    }

    @Test
    public void hueInterpolationStaysBelow360() {
        List<HSVColor> result = new HSVGradient(new HSVColor(300, 1, 1), new HSVColor(60, 1, 1)).getColors(20);
        for (HSVColor color : result) {
            assertTrue(color.getHue() >= 0 && color.getHue() < 360, color.toString());
        }
    }

    @Test
    public void labGradientBetweenExtremeStopsDoesNotThrow() {
        LABGradient gradient = new LABGradient(new LABColor(0, -128, -128), new LABColor(100, 128, 128));
        assertDoesNotThrow(() -> gradient.getColors(1000));
    }

    @Test
    public void xyzValuesAreEvenlySpaced() {
        List<XYZColor> result = new XYZGradient(new XYZColor(0, 0, 0), new XYZColor(0.4, 0.8, 1.2)).getColors(3);
        assertEquals(0.1, result.get(1).getX(), 1e-9);
        assertEquals(0.4, result.get(2).getY(), 1e-9);
        assertEquals(0.9, result.get(3).getZ(), 1e-9);
    }
}
