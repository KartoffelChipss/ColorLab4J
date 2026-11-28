package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.XYZColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.List;

public class XYZGradient extends Gradient<XYZColor> {
    public XYZGradient(List<XYZColor> colors) {
        super(colors);
    }

    public XYZGradient(XYZColor start, XYZColor end) {
        super(start, end);
    }

    public XYZGradient(XYZColor... colors) {
        super(colors);
    }

    /**
     * Create a gradient from HEX color codes, interpolated in the XYZ color space
     * @param hexCodes The HEX color codes, at least two (e.g. "#ff0000", "#00f")
     * @return XYZGradient instance
     * @throws IllegalArgumentException If fewer than two codes are given or any code is null or invalid
     */
    public static XYZGradient fromHex(String... hexCodes) {
        return new XYZGradient(parseHexCodes(hexCodes, XYZColor::fromHex));
    }

    @Override
    protected XYZColor interpolate(XYZColor start, XYZColor end, double factor) {
        return new XYZColor(
                lerp(start.getX(), end.getX(), factor, 0, 95.047),
                lerp(start.getY(), end.getY(), factor, 0, 100.0),
                lerp(start.getZ(), end.getZ(), factor, 0, 108.883)
        );
    }
}
