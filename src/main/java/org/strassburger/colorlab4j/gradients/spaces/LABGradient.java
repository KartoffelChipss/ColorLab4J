package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.LABColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.List;

public class LABGradient extends Gradient<LABColor> {
    public LABGradient(List<LABColor> colors) {
        super(colors);
    }

    public LABGradient(LABColor start, LABColor end) {
        super(start, end);
    }

    public LABGradient(LABColor... colors) {
        super(colors);
    }

    /**
     * Create a gradient from HEX color codes, interpolated in the LAB color space
     * @param hexCodes The HEX color codes, at least two (e.g. "#ff0000", "#00f")
     * @return LABGradient instance
     * @throws IllegalArgumentException If fewer than two codes are given or any code is null or invalid
     */
    public static LABGradient fromHex(String... hexCodes) {
        return new LABGradient(parseHexCodes(hexCodes, LABColor::fromHex));
    }

    @Override
    protected LABColor interpolate(LABColor start, LABColor end, double factor) {
        return new LABColor(
                lerp(start.getL(), end.getL(), factor, 0, 100),
                lerp(start.getA(), end.getA(), factor, -128, 128),
                lerp(start.getB(), end.getB(), factor, -128, 128)
        );
    }
}
