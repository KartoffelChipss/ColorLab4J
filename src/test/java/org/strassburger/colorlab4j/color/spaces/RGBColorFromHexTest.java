package org.strassburger.colorlab4j.color.spaces;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.strassburger.colorlab4j.color.Color;

import static org.junit.jupiter.api.Assertions.*;

public class RGBColorFromHexTest {

    @ParameterizedTest
    @ValueSource(strings = {"#ff8000", "ff8000", "#FF8000", "  #ff8000  ", "#Ff8000"})
    public void parsesSixDigitHex(String hex) {
        assertEquals(new RGBColor(255, 128, 0), RGBColor.fromHex(hex));
    }

    @ParameterizedTest
    @ValueSource(strings = {"#f80", "f80", "#F80"})
    public void parsesThreeDigitShorthand(String hex) {
        assertEquals(new RGBColor(255, 136, 0), RGBColor.fromHex(hex));
    }

    @Test
    public void parsesBlackAndWhite() {
        assertEquals(new RGBColor(0, 0, 0), RGBColor.fromHex("#000000"));
        assertEquals(new RGBColor(255, 255, 255), RGBColor.fromHex("#fff"));
    }

    @Test
    public void roundTripsThroughToHex() {
        RGBColor color = new RGBColor(18, 52, 86);
        assertEquals("#123456", color.toHex());
        assertEquals(color, RGBColor.fromHex(color.toHex()));
    }

    @Test
    public void colorFromHexDelegates() {
        assertEquals(new RGBColor(255, 0, 0), Color.fromHex("#ff0000"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",           // empty
            "#",          // prefix only
            "#ff",        // too short
            "#ffff",      // 4 digits
            "#fffff",     // 5 digits
            "#ff0000zz",  // trailing garbage
            "#ff00000",   // 7 digits
            "#gg0000",    // non-hex digits
            "#+f0000",    // sign accepted by Integer.parseInt
            "#-10000",    // sign accepted by Integer.parseInt
            "##ff0000",   // double prefix
            "#ff 000",    // inner whitespace
    })
    public void rejectsInvalidHex(String hex) {
        assertThrows(IllegalArgumentException.class, () -> RGBColor.fromHex(hex));
    }

    @Test
    public void rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> RGBColor.fromHex(null));
    }

    @Test
    public void subclassFromHexAlsoValidates() {
        assertThrows(IllegalArgumentException.class, () -> HSLColor.fromHex("#fff0"));
    }
}
