package org.strassburger.colorlab4j.gradients;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.strassburger.colorlab4j.color.spaces.*;
import org.strassburger.colorlab4j.gradients.spaces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GradientValidationTest {

    private static final RGBColor RED = new RGBColor(255, 0, 0);
    private static final RGBColor BLUE = new RGBColor(0, 0, 255);

    static Stream<Arguments> hexFactories() {
        return Stream.of(
                Arguments.of("RGB", (Function<String[], Gradient<?>>) RGBGradient::fromHex),
                Arguments.of("HSL", (Function<String[], Gradient<?>>) HSLGradient::fromHex),
                Arguments.of("HSV", (Function<String[], Gradient<?>>) HSVGradient::fromHex),
                Arguments.of("LAB", (Function<String[], Gradient<?>>) LABGradient::fromHex),
                Arguments.of("XYZ", (Function<String[], Gradient<?>>) XYZGradient::fromHex)
        );
    }

    // --- Constructors ---

    @Test
    public void listConstructorRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> new RGBGradient((List<RGBColor>) null));
    }

    @Test
    public void listConstructorRejectsTooFewColors() {
        assertThrows(IllegalArgumentException.class, () -> new RGBGradient(List.of()));
        assertThrows(IllegalArgumentException.class, () -> new RGBGradient(List.of(RED)));
    }

    @Test
    public void listConstructorRejectsNullElement() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new RGBGradient(Arrays.asList(RED, null, BLUE)));
        assertTrue(e.getMessage().contains("index 1"), e.getMessage());
    }

    @Test
    public void twoColorConstructorRejectsNulls() {
        assertThrows(IllegalArgumentException.class, () -> new RGBGradient(null, BLUE));
        assertThrows(IllegalArgumentException.class, () -> new RGBGradient(RED, null));
    }

    @Test
    public void varargsConstructorRejectsTooFewColors() {
        assertThrows(IllegalArgumentException.class, () -> new RGBGradient());
        assertThrows(IllegalArgumentException.class, () -> new RGBGradient(new RGBColor[]{RED}));
        assertThrows(IllegalArgumentException.class, () -> new RGBGradient((RGBColor[]) null));
    }

    @Test
    public void varargsConstructorRejectsNullElement() {
        assertThrows(IllegalArgumentException.class, () -> new RGBGradient(RED, BLUE, null));
    }

    @Test
    public void validConstructorsAreEqual() {
        RGBGradient fromList = new RGBGradient(List.of(RED, BLUE));
        RGBGradient fromPair = new RGBGradient(RED, BLUE);
        RGBGradient fromVarargs = new RGBGradient(new RGBColor[]{RED, BLUE});
        assertEquals(fromList, fromPair);
        assertEquals(fromList, fromVarargs);
    }

    @Test
    public void gradientIsNotAffectedByLaterChangesToTheInputList() {
        List<RGBColor> input = new ArrayList<>(List.of(RED, BLUE));
        RGBGradient gradient = new RGBGradient(input);
        input.clear();
        assertEquals(List.of(RED, BLUE), gradient.colors);
    }

    @Test
    public void colorsListIsUnmodifiable() {
        RGBGradient gradient = new RGBGradient(RED, BLUE);
        assertThrows(UnsupportedOperationException.class, () -> gradient.colors.add(RED));
    }

    // --- Steps ---

    @Test
    public void negativeStepsAreRejected() {
        RGBGradient gradient = new RGBGradient(RED, BLUE);
        assertThrows(IllegalArgumentException.class, () -> gradient.getColors(-1));
        assertThrows(IllegalArgumentException.class, () -> gradient.getColors(-1, false));
    }

    @Test
    public void zeroStepsReturnsOnlyEndpoints() {
        assertEquals(List.of(RED, BLUE), new RGBGradient(RED, BLUE).getColors(0));
        assertEquals(List.of(), new RGBGradient(RED, BLUE).getColors(0, false));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("hexFactories")
    public void everyGradientTypeRejectsNegativeSteps(String name, Function<String[], Gradient<?>> factory) {
        Gradient<?> gradient = factory.apply(new String[]{"#ff0000", "#0000ff"});
        assertThrows(IllegalArgumentException.class, () -> gradient.getColors(-1));
    }

    // --- fromHex ---

    @Test
    public void rgbFromHexMatchesConstructor() {
        assertEquals(new RGBGradient(RED, BLUE), RGBGradient.fromHex("#ff0000", "#0000ff"));
    }

    @Test
    public void fromHexAcceptsShorthandAndMoreThanTwoColors() {
        RGBGradient gradient = RGBGradient.fromHex("#f00", "00ff00", "#00F");
        assertEquals(List.of(RED, new RGBColor(0, 255, 0), BLUE), gradient.colors);
    }

    @Test
    public void fromHexUsesTheGradientsColorSpace() {
        assertEquals(new HSLGradient(RED.toHSL(), BLUE.toHSL()), HSLGradient.fromHex("#ff0000", "#0000ff"));
        assertEquals(new HSVGradient(RED.toHSV(), BLUE.toHSV()), HSVGradient.fromHex("#ff0000", "#0000ff"));
        assertEquals(new LABGradient(RED.toLAB(), BLUE.toLAB()), LABGradient.fromHex("#ff0000", "#0000ff"));
        assertEquals(new XYZGradient(RED.toXYZ(), BLUE.toXYZ()), XYZGradient.fromHex("#ff0000", "#0000ff"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("hexFactories")
    public void fromHexRejectsTooFewCodes(String name, Function<String[], Gradient<?>> factory) {
        assertThrows(IllegalArgumentException.class, () -> factory.apply(null));
        assertThrows(IllegalArgumentException.class, () -> factory.apply(new String[]{}));
        assertThrows(IllegalArgumentException.class, () -> factory.apply(new String[]{"#ff0000"}));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("hexFactories")
    public void fromHexRejectsInvalidCodeAndNamesItsIndex(String name, Function<String[], Gradient<?>> factory) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> factory.apply(new String[]{"#ff0000", "#0000ff", "#nope"}));
        assertTrue(e.getMessage().contains("index 2"), e.getMessage());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("hexFactories")
    public void fromHexRejectsNullCode(String name, Function<String[], Gradient<?>> factory) {
        assertThrows(IllegalArgumentException.class, () -> factory.apply(new String[]{"#ff0000", null}));
    }
}
