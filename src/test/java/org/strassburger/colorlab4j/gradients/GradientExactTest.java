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

public class GradientExactTest {

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

    // --- Size ---

    @Test
    public void alwaysReturnsExactlyTheRequestedNumberOfColors() {
        for (int stops = 2; stops <= 6; stops++) {
            List<RGBColor> colors = new ArrayList<>();
            for (int i = 0; i < stops; i++) colors.add(new RGBColor(i * 40, 0, 0));
            RGBGradient gradient = new RGBGradient(colors);

            for (int steps = 0; steps <= 20; steps++) {
                assertEquals(steps, gradient.getColorsExact(steps).size(), stops + " stops, " + steps + " steps");
            }
        }
    }

    @Test
    public void zeroStepsIsEmpty() {
        assertEquals(List.of(), new RGBGradient(RED, BLUE).getColorsExact(0));
    }

    @Test
    public void oneStepIsTheFirstStop() {
        assertEquals(List.of(RED), new RGBGradient(RED, GREEN, BLUE).getColorsExact(1));
    }

    @Test
    public void twoStepsAreTheFirstAndLastStop() {
        assertEquals(List.of(RED, BLUE), new RGBGradient(RED, GREEN, BLUE).getColorsExact(2));
    }

    @Test
    public void negativeStepsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new RGBGradient(RED, BLUE).getColorsExact(-1));
    }

    // --- Spacing ---

    @Test
    public void twoStopGradientIsEvenlySpaced() {
        List<RGBColor> result = new RGBGradient(BLACK, new RGBColor(100, 100, 100)).getColorsExact(5);
        assertEquals(List.of(
                BLACK,
                new RGBColor(25, 25, 25),
                new RGBColor(50, 50, 50),
                new RGBColor(75, 75, 75),
                new RGBColor(100, 100, 100)
        ), result);
    }

    @Test
    public void blackToWhiteIn256StepsHitsEveryGray() {
        List<RGBColor> result = new RGBGradient(BLACK, WHITE).getColorsExact(256);
        for (int i = 0; i < 256; i++) {
            assertEquals(new RGBColor(i, i, i), result.get(i), "index " + i);
        }
    }

    @Test
    public void middleStopAppearsWhenAPositionLandsOnIt() {
        List<RGBColor> result = new RGBGradient(RED, GREEN, BLUE).getColorsExact(5);
        assertEquals(List.of(
                RED,
                new RGBColor(128, 128, 0),
                GREEN,
                new RGBColor(0, 128, 128),
                BLUE
        ), result);
    }

    @Test
    public void middleStopIsSkippedWhenNoPositionLandsOnIt() {
        // positions 0, 1/3, 2/3, 1 over two segments: 2/3 into the first, 1/3 into the second
        List<RGBColor> result = new RGBGradient(RED, GREEN, BLUE).getColorsExact(4);
        assertEquals(List.of(
                RED,
                new RGBColor(85, 170, 0),
                new RGBColor(0, 170, 85),
                BLUE
        ), result);
    }

    @Test
    public void everyStopAppearsWhenStepsLineUpWithStops() {
        // 4 stops, 3 segments; 7 steps puts a color at every 1/2 segment
        RGBGradient gradient = new RGBGradient(RED, GREEN, BLUE, WHITE);
        List<RGBColor> result = gradient.getColorsExact(7);
        assertEquals(RED, result.get(0));
        assertEquals(GREEN, result.get(2));
        assertEquals(BLUE, result.get(4));
        assertEquals(WHITE, result.get(6));
    }

    // --- Every gradient type ---

    @ParameterizedTest(name = "{0}")
    @MethodSource("hexFactories")
    public void startsAndEndsOnTheStops(String name, Function<String[], Gradient<?>> factory) {
        Gradient<?> gradient = factory.apply(new String[]{"#ff0000", "#00ff00", "#0000ff"});
        List<?> result = gradient.getColorsExact(10);
        assertEquals(10, result.size());
        assertEquals(gradient.colors.get(0), result.get(0));
        assertEquals(gradient.colors.get(2), result.get(9));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("hexFactories")
    public void largeStepCountsDoNotThrow(String name, Function<String[], Gradient<?>> factory) {
        Gradient<?> gradient = factory.apply(new String[]{"#ffffff", "#000000", "#ff00ff", "#00000a"});
        assertEquals(10_000, assertDoesNotThrow(() -> gradient.getColorsExact(10_000)).size());
    }
}
