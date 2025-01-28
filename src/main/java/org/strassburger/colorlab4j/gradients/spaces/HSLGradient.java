package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.HSLColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.ArrayList;
import java.util.List;

public class HSLGradient extends Gradient<HSLColor> {
    public HSLGradient(HSLColor start, HSLColor end) {
        super(start, end);
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
        List<HSLColor> colors = new ArrayList<>();
        if (includeStartAndEnd) colors.add(start);

        for (int i = 1; i <= steps; i++) {
            double factor = (double) i / (steps + 1);
            double h = interpolateHue(start.getHue(), end.getHue(), factor);
            double s = start.getSaturation() + factor * (end.getSaturation() - start.getSaturation());
            double l = start.getLightness() + factor * (end.getLightness() - start.getLightness());
            colors.add(new HSLColor(h, s, l));
        }

        if (includeStartAndEnd) colors.add(end);
        return colors;
    }
}
