package org.strassburger.colorlab4j.color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.strassburger.colorlab4j.color.spaces.*;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class LocaleIndependenceTest {

    private static final String[] LOCALES = {"de-DE", "fr-FR", "ar-EG", "th-TH-u-nu-thai", "hi-IN-u-nu-deva"};

    private Locale originalLocale;

    @BeforeEach
    public void rememberLocale() {
        originalLocale = Locale.getDefault();
    }

    @AfterEach
    public void restoreLocale() {
        Locale.setDefault(originalLocale);
    }

    private static List<Color> sampleColors() {
        return List.of(
                new RGBColor(255, 128, 0),
                new HSLColor(120.5, 0.333333, 0.25),
                new HSVColor(200, 0.5, 0.75),
                new LABColor(53.2408, 80.0925, -67.2032),
                new XYZColor(0.41245641, 0.2126729, 0.0193339)
        );
    }

    private static void assertSameInEveryLocale(String what, Function<Color, String> format) {
        Locale.setDefault(Locale.ROOT);
        List<String> expected = sampleColors().stream().map(format).toList();

        for (String tag : LOCALES) {
            Locale.setDefault(Locale.forLanguageTag(tag));
            List<String> actual = sampleColors().stream().map(format).toList();
            assertEquals(expected, actual, what + " in " + tag);
            for (String s : actual) {
                assertTrue(s.chars().allMatch(c -> c < 128), what + " in " + tag + " is not ASCII: " + s);
            }
        }
    }

    // --- Exact output ---

    @Test
    public void toStringUsesDotDecimals() {
        Locale.setDefault(Locale.GERMANY);
        assertEquals("rgb(255,128,0)", new RGBColor(255, 128, 0).toString());
        assertEquals("hsl(120.500000, 0.500000, 0.250000)", new HSLColor(120.5, 0.5, 0.25).toString());
        assertEquals("hsv(200.000000, 0.500000, 0.750000)", new HSVColor(200, 0.5, 0.75).toString());
        assertEquals("lab(53.240800, 80.092500, -67.203200)", new LABColor(53.2408, 80.0925, -67.2032).toString());
        assertEquals("xyz(0.412456, 0.212673, 0.019334)", new XYZColor(0.41245641, 0.2126729, 0.0193339).toString());
    }

    @Test
    public void ansiCodesUseAsciiDigits() {
        Locale.setDefault(Locale.forLanguageTag("th-TH-u-nu-thai"));
        RGBColor color = new RGBColor(255, 128, 0);
        assertEquals("\u001B[38;2;255;128;0m", color.toAnsi());
        assertEquals("\u001B[48;2;255;128;0m", color.toAnsiBackground());
    }

    @Test
    public void hexUsesAsciiDigits() {
        Locale.setDefault(Locale.forLanguageTag("hi-IN-u-nu-deva"));
        assertEquals("#ff8000", new RGBColor(255, 128, 0).toHex());
    }

    // --- Every locale, every color type ---

    @Test
    public void toStringIsLocaleIndependent() {
        assertSameInEveryLocale("toString", Color::toString);
    }

    @Test
    public void toHexIsLocaleIndependent() {
        assertSameInEveryLocale("toHex", Color::toHex);
    }

    @Test
    public void toAnsiIsLocaleIndependent() {
        assertSameInEveryLocale("toAnsi", Color::toAnsi);
    }

    @Test
    public void toAnsiBackgroundIsLocaleIndependent() {
        assertSameInEveryLocale("toAnsiBackground", Color::toAnsiBackground);
    }

    @ParameterizedTest
    @ValueSource(strings = {"de-DE", "th-TH-u-nu-thai"})
    public void gradientToStringIsLocaleIndependent(String tag) {
        var gradient = new org.strassburger.colorlab4j.gradients.spaces.HSLGradient(
                new HSLColor(0, 0.5, 0.5), new HSLColor(120.5, 1, 0.25));
        Locale.setDefault(Locale.ROOT);
        String expected = gradient.toString();
        Locale.setDefault(Locale.forLanguageTag(tag));
        assertEquals(expected, gradient.toString());
    }
}
