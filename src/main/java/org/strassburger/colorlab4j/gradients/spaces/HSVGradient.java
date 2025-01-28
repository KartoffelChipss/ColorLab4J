package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.HSVColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.ArrayList;
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

    @Override
    public List<HSVColor> getColors(int steps, boolean includeStartAndEnd) {
        List<HSVColor> gradientColors = new ArrayList<>();

        if (includeStartAndEnd) {
            gradientColors.add(colors.get(0));
        }

        int segmentSteps = steps / (colors.size() - 1);

        for (int i = 0; i < colors.size() - 1; i++) {
            HSVColor startColor = colors.get(i);
            HSVColor endColor = colors.get(i + 1);

            double sDelta = (endColor.getSaturation() - startColor.getSaturation()) / (segmentSteps + 1);
            double vDelta = (endColor.getValue() - startColor.getValue()) / (segmentSteps + 1);

            double startHue = startColor.getHue();
            double endHue = endColor.getHue();
            double hueDelta;

            if (Math.abs(endHue - startHue) > 180) {
                if (endHue > startHue) {
                    startHue += 360;
                } else {
                    endHue += 360;
                }
            }
            hueDelta = (endHue - startHue) / (segmentSteps + 1);

            double h = startHue;
            double s = startColor.getSaturation();
            double v = startColor.getValue();

            for (int j = 1; j <= segmentSteps; j++) {
                h += hueDelta;
                s += sDelta;
                v += vDelta;
                gradientColors.add(new HSVColor(h % 360, s, v));
            }
        }

        if (includeStartAndEnd) {
            gradientColors.add(colors.get(colors.size() - 1));
        }

        return gradientColors;
    }
}
