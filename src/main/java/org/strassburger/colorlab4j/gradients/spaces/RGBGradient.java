package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.RGBColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.List;

public class RGBGradient extends Gradient<RGBColor> {
    public RGBGradient(List<RGBColor> colors) {
        super(colors);
    }

    public RGBGradient(RGBColor start, RGBColor end) {
        super(start, end);
    }

    public RGBGradient(RGBColor... colors) {
        super(colors);
    }

    /**
     * Create a gradient from HEX color codes, interpolated in the RGB color space
     * @param hexCodes The HEX color codes, at least two (e.g. "#ff0000", "#00f")
     * @return RGBGradient instance
     * @throws IllegalArgumentException If fewer than two codes are given or any code is null or invalid
     */
    public static RGBGradient fromHex(String... hexCodes) {
        return new RGBGradient(parseHexCodes(hexCodes, RGBColor::fromHex));
    }

    @Override
    protected RGBColor interpolate(RGBColor start, RGBColor end, double factor) {
        return new RGBColor(
                (int) Math.round(lerp(start.getRed(), end.getRed(), factor)),
                (int) Math.round(lerp(start.getGreen(), end.getGreen(), factor)),
                (int) Math.round(lerp(start.getBlue(), end.getBlue(), factor))
        );
    }
}
