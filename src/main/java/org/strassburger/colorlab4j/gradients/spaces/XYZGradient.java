package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.XYZColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.ArrayList;
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
    public List<XYZColor> getColors(int steps, boolean includeStartAndEnd) {
        validateSteps(steps);
        List<XYZColor> gradientColors = new ArrayList<>();

        if (includeStartAndEnd) {
            gradientColors.add(colors.get(0));
        }

        int segmentSteps = steps / (colors.size() - 1);

        for (int i = 0; i < colors.size() - 1; i++) {
            XYZColor startColor = colors.get(i);
            XYZColor endColor = colors.get(i + 1);

            double xDelta = (endColor.getX() - startColor.getX()) / (segmentSteps + 1);
            double yDelta = (endColor.getY() - startColor.getY()) / (segmentSteps + 1);
            double zDelta = (endColor.getZ() - startColor.getZ()) / (segmentSteps + 1);

            double x = startColor.getX();
            double y = startColor.getY();
            double z = startColor.getZ();

            for (int j = 1; j <= segmentSteps; j++) {
                x += xDelta;
                y += yDelta;
                z += zDelta;
                gradientColors.add(new XYZColor(x, y, z));
            }
        }

        if (includeStartAndEnd) {
            gradientColors.add(colors.get(colors.size() - 1));
        }

        return gradientColors;
    }
}
