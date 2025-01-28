package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.HSVColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.ArrayList;
import java.util.List;

public class HSVGradient extends Gradient<HSVColor> {
    public HSVGradient(HSVColor start, HSVColor end) {
        super(start, end);
    }

    @Override
    public List<HSVColor> getColors(int steps, boolean includeStartAndEnd) {
        List<HSVColor> colors = new ArrayList<>();

        if (includeStartAndEnd) colors.add(start);

        double sDelta = (end.getSaturation() - start.getSaturation()) / (steps + 1);
        double vDelta = (end.getValue() - start.getValue()) / (steps + 1);

        double startHue = start.getHue();
        double endHue = end.getHue();
        double hueDelta;
        if (Math.abs(endHue - startHue) > 180) {
            if (endHue > startHue) {
                startHue += 360;
            } else {
                endHue += 360;
            }
        }
        hueDelta = (endHue - startHue) / (steps + 1);

        double h = startHue;
        double s = start.getSaturation();
        double v = start.getValue();

        for (int i = 1; i <= steps; i++) {
            h += hueDelta;
            s += sDelta;
            v += vDelta;
            colors.add(new HSVColor(h % 360, s, v));
        }

        if (includeStartAndEnd) colors.add(end);

        return colors;
    }
}
