package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.LABColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.ArrayList;
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

    @Override
    public List<LABColor> getColors(int steps, boolean includeStartAndEnd) {
        List<LABColor> gradientColors = new ArrayList<>();

        if (includeStartAndEnd) {
            gradientColors.add(colors.get(0));
        }

        int segmentSteps = steps / (colors.size() - 1);

        for (int i = 0; i < colors.size() - 1; i++) {
            LABColor startColor = colors.get(i);
            LABColor endColor = colors.get(i + 1);

            double lDelta = (endColor.getL() - startColor.getL()) / (segmentSteps + 1);
            double aDelta = (endColor.getA() - startColor.getA()) / (segmentSteps + 1);
            double bDelta = (endColor.getB() - startColor.getB()) / (segmentSteps + 1);

            double l = startColor.getL();
            double a = startColor.getA();
            double b = startColor.getB();

            for (int j = 0; j < segmentSteps; j++) {
                l += lDelta;
                a += aDelta;
                b += bDelta;
                gradientColors.add(new LABColor(l, a, b));
            }
        }

        if (includeStartAndEnd) {
            gradientColors.add(colors.get(colors.size() - 1));
        }

        return gradientColors;
    }
}
