package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.HSLColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.List;

public class HSLGradient extends Gradient<HSLColor> {
    public HSLGradient(List<HSLColor> colors) {
        super(colors);
    }

    public HSLGradient(HSLColor start, HSLColor end) {
        super(start, end);
    }

    public HSLGradient(HSLColor... colors) {
        super(colors);
    }

    /**
     * Create a gradient from HEX color codes, interpolated in the HSL color space
     * @param hexCodes The HEX color codes, at least two (e.g. "#ff0000", "#00f")
     * @return HSLGradient instance
     * @throws IllegalArgumentException If fewer than two codes are given or any code is null or invalid
     */
    public static HSLGradient fromHex(String... hexCodes) {
        return new HSLGradient(parseHexCodes(hexCodes, HSLColor::fromHex));
    }

    @Override
    protected HSLColor interpolate(HSLColor start, HSLColor end, double factor) {
        return new HSLColor(
                interpolateHue(start.getHue(), end.getHue(), factor),
                lerp(start.getSaturation(), end.getSaturation(), factor, 0, 1),
                lerp(start.getLightness(), end.getLightness(), factor, 0, 1)
        );
    }
}
