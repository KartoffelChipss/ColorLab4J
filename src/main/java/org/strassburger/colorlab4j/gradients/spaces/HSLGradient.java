package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.HSLColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.ArrayList;
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

    private double interpolateHue(double startHue, double endHue, double factor) {
        double diff = endHue - startHue;
        if (Math.abs(diff) > 180) {
            if (diff > 0) {
                startHue += 360;
            } else {
                endHue += 360;
            }
        }
        return (startHue + factor * (endHue - startHue)) % 360;
    }

    @Override
    public List<HSLColor> getColors(int steps, boolean includeStartAndEnd) {
        List<HSLColor> gradientColors = new ArrayList<>();

        if (includeStartAndEnd) {
            gradientColors.add(colors.get(0));
        }

        int segmentSteps = steps / (colors.size() - 1);

        for (int i = 0; i < colors.size() - 1; i++) {
            HSLColor startColor = colors.get(i);
            HSLColor endColor = colors.get(i + 1);

            for (int j = 1; j <= segmentSteps; j++) {
                double factor = (double) j / (segmentSteps + 1);

                double h = interpolateHue(startColor.getHue(), endColor.getHue(), factor);
                double s = startColor.getSaturation() + factor * (endColor.getSaturation() - startColor.getSaturation());
                double l = startColor.getLightness() + factor * (endColor.getLightness() - startColor.getLightness());

                gradientColors.add(new HSLColor(h, s, l));
            }
        }

        if (includeStartAndEnd) {
            gradientColors.add(colors.get(colors.size() - 1));
        }

        return gradientColors;
    }
}
