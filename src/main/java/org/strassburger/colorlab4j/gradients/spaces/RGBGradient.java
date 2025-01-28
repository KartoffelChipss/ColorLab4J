package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.RGBColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.ArrayList;
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

    @Override
    public List<RGBColor> getColors(int steps, boolean includeStartAndEnd) {
        List<RGBColor> gradientColors = new ArrayList<>();

        if (includeStartAndEnd) {
            gradientColors.add(colors.get(0));
        }

        int segmentSteps = steps / (colors.size() - 1);

        for (int i = 0; i < colors.size() - 1; i++) {
            RGBColor startColor = colors.get(i);
            RGBColor endColor = colors.get(i + 1);

            int rDelta = (endColor.getRed() - startColor.getRed()) / (segmentSteps + 1);
            int gDelta = (endColor.getGreen() - startColor.getGreen()) / (segmentSteps + 1);
            int bDelta = (endColor.getBlue() - startColor.getBlue()) / (segmentSteps + 1);

            int r = startColor.getRed();
            int g = startColor.getGreen();
            int b = startColor.getBlue();

            for (int j = 0; j < segmentSteps; j++) {
                r += rDelta;
                g += gDelta;
                b += bDelta;
                gradientColors.add(new RGBColor(r, g, b));
            }
        }

        if (includeStartAndEnd) {
            gradientColors.add(colors.get(colors.size() - 1));
        }

        return gradientColors;
    }
}