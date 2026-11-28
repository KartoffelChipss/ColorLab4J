package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.HSVColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.List;

public class HSVGradient extends Gradient<HSVColor> {
    public HSVGradient(List<HSVColor> colors) {
        super(colors);
    }

    public HSVGradient(HSVColor start, HSVColor end) {
        super(start, end);
    }

    public HSVGradient(HSVColor... colors) {
        super(colors);
    }

    /**
     * Create a gradient from HEX color codes, interpolated in the HSV color space
     * @param hexCodes The HEX color codes, at least two (e.g. "#ff0000", "#00f")
     * @return HSVGradient instance
     * @throws IllegalArgumentException If fewer than two codes are given or any code is null or invalid
     */
    public static HSVGradient fromHex(String... hexCodes) {
        return new HSVGradient(parseHexCodes(hexCodes, HSVColor::fromHex));
    }

    @Override
    protected HSVColor interpolate(HSVColor start, HSVColor end, double factor) {
        return new HSVColor(
                interpolateHue(start.getHue(), end.getHue(), factor),
                lerp(start.getSaturation(), end.getSaturation(), factor, 0, 1),
                lerp(start.getValue(), end.getValue(), factor, 0, 1)
        );
    }
}
