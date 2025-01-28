package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.RGBColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.ArrayList;
import java.util.List;

public class RGBGradient extends Gradient<RGBColor> {
    public RGBGradient(RGBColor start, RGBColor end) {
        super(start, end);
    }

    @Override
    public List<RGBColor> getColors(int steps, boolean includeStartAndEnd) {
        List<RGBColor> colors = new ArrayList<>();

        if (includeStartAndEnd) colors.add(start);

        int rDelta = (end.getRed() - start.getRed()) / (steps + 1);
        int gDelta = (end.getGreen() - start.getGreen()) / (steps + 1);
        int bDelta = (end.getBlue() - start.getBlue()) / (steps + 1);

        int r = start.getRed();
        int g = start.getGreen();
        int b = start.getBlue();

        for (int i = 0; i < steps; i++) {
            r += rDelta;
            g += gDelta;
            b += bDelta;
            colors.add(new RGBColor(r, g, b));
        }

        if (includeStartAndEnd) colors.add(end);

        return colors;
    }
}
