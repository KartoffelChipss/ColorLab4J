package org.strassburger.colorlab4j.color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.strassburger.colorlab4j.color.spaces.*;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class CssStringTest {

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

    // --- Exact output ---

    @Test
    public void rgb() {
        assertEquals("rgb(255,128,0)", new RGBColor(255, 128, 0).toCssString());
    }

    @Test
    public void hslUsesPercentagesAndDropsTrailingZeros() {
        assertEquals("hsl(120, 50%, 25%)", new HSLColor(120, 0.5, 0.25).toCssString());
    }

    @Test
    public void hslRoundsToFourDecimals() {
        assertEquals("hsl(120.5, 33.3333%, 66.6667%)", new HSLColor(120.5, 1.0 / 3, 2.0 / 3).toCssString());
    }

    @Test
    public void hsvIsWrittenAsEquivalentHsl() {
        assertEquals("hsl(0, 100%, 50%)", new HSVColor(0, 1, 1).toCssString());
        assertEquals(new HSVColor(200, 0.5, 0.75).toHSL().toCssString(), new HSVColor(200, 0.5, 0.75).toCssString());
    }

    @Test
    public void labIsSpaceSeparated() {
        assertEquals("lab(53.2408 80.0925 -67.2032)", new LABColor(53.2408, 80.0925, -67.2032).toCssString());
        assertEquals("lab(100 0 0)", new LABColor(100, 0, 0).toCssString());
    }

    @Test
    public void xyzUsesColorFunction() {
        assertEquals("color(xyz-d65 0.4125 0.2127 0.0193)", new XYZColor(0.41245641, 0.2126729, 0.0193339).toCssString());
        assertEquals("color(xyz-d65 0 0 0)", new XYZColor(0, 0, 0).toCssString());
    }

    @Test
    public void tinyValuesAreNotWrittenInExponentNotation() {
        String css = new XYZColor(0.00000001, 0.00005, 0).toCssString();
        assertEquals("color(xyz-d65 0 0.0001 0)", css);
    }

    @Test
    public void tinyNegativeValuesDoNotProduceNegativeZero() {
        assertEquals("lab(50 0 0)", new LABColor(50, -0.00001, -0.0).toCssString());
    }

    // --- Locale independence ---

    @ParameterizedTest
    @ValueSource(strings = {"de-DE", "fr-FR", "ar-EG", "th-TH-u-nu-thai", "hi-IN-u-nu-deva"})
    public void outputDoesNotDependOnDefaultLocale(String languageTag) {
        Locale.setDefault(Locale.ROOT);
        List<String> expected = sampleColors().stream().map(Color::toCssString).toList();

        Locale.setDefault(Locale.forLanguageTag(languageTag));
        List<String> actual = sampleColors().stream().map(Color::toCssString).toList();

        assertEquals(expected, actual);
    }

    @Test
    public void germanLocaleUsesDotsNotCommas() {
        Locale.setDefault(Locale.GERMANY);
        assertEquals("hsl(120.5, 50%, 25%)", new HSLColor(120.5, 0.5, 0.25).toCssString());
    }

    @Test
    public void outputContainsOnlyAsciiCharacters() {
        Locale.setDefault(Locale.forLanguageTag("th-TH-u-nu-thai"));
        for (Color color : sampleColors()) {
            String css = color.toCssString();
            assertTrue(css.chars().allMatch(c -> c < 128), css);
        }
    }
}
