package org.strassburger.colorlab4j.gradients.spaces;

import org.strassburger.colorlab4j.color.spaces.RGBColor;
import org.strassburger.colorlab4j.color.spaces.XYZColor;
import org.strassburger.colorlab4j.gradients.Gradient;

import java.util.ArrayList;
import java.util.List;

public class XYZGradient extends Gradient<XYZColor> {
    public XYZGradient(XYZColor start, XYZColor end) {
        super(start, end);
    }

    @Override
    public List<XYZColor> getColors(int steps, boolean includeStartAndEnd) {
        List<XYZColor> colors = new ArrayList<>();

        if (includeStartAndEnd) colors.add(start);

        double xDelta = (end.getX() - start.getX()) / (steps + 1);
        double yDelta = (end.getY() - start.getY()) / (steps + 1);
        double zDelta = (end.getZ() - start.getZ()) / (steps + 1);

        double x = start.getX();
        double y = start.getY();
        double z = start.getZ();

        for (int i = 1; i <= steps; i++) {
            x += xDelta;
            y += yDelta;
            z += zDelta;
            colors.add(new XYZColor(x, y, z));
        }

        if (includeStartAndEnd) colors.add(end);

        return colors;
    }
}
