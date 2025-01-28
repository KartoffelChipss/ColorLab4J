package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.LABColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.ArrayList;
import java.util.List;

public class LABGradient extends Gradient<LABColor> {
    public LABGradient(LABColor start, LABColor end) {
        super(start, end);
    }

    @Override
    public List<LABColor> getColors(int steps, boolean includeStartAndEnd) {
        List<LABColor> colors = new ArrayList<>();
        if (includeStartAndEnd) colors.add(start);

        double lDelta = (end.getL() - start.getL()) / (steps + 1);
        double aDelta = (end.getA() - start.getA()) / (steps + 1);
        double bDelta = (end.getB() - start.getB()) / (steps + 1);

        double l = start.getL();
        double a = start.getA();
        double b = start.getB();

        for (int i = 1; i <= steps; i++) {
            l += lDelta;
            a += aDelta;
            b += bDelta;
            colors.add(new LABColor(l, a, b));
        }

        if (includeStartAndEnd) colors.add(end);
        return colors;
    }
}
