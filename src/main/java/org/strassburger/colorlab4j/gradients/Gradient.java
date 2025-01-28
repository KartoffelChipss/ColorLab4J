package org.strassburger.colorlab4j.gradients;

import org.strassburger.colorlab4j.color.Color;

import java.util.List;

public abstract class Gradient<T extends Color> {
    public final List<T> colors;

    /**
     * Create a new gradient
     * @param colors The list of colors that define the gradient
     */
    public Gradient(List<T> colors) {
        if (colors == null || colors.size() < 2) {
            throw new IllegalArgumentException("At least two colors are required for the gradient.");
        }
        this.colors = colors;
    }

    /**
     * Create a new gradient
     * @param start The start color
     * @param end The end color
     */
    public Gradient(T start, T end) {
        this.colors = List.of(start, end);
    }

    /**
     * Create a new gradient
     * @param colors The colors that define the gradient
     */
    @SafeVarargs
    public Gradient(T... colors) {
        this.colors = List.of(colors);
    }

    /**
     * Get the colors of the gradient (including the start and end color)
     * @param steps The number of steps (excluding the start and end color)
     * @return The colors of the gradient
     */
    public List<T> getColors(int steps) {
        return getColors(steps, true);
    }

    /**
     * Get the colors of the gradient
     * @param steps The number of steps
     * @param includeStartAndEnd Whether to include the start and end color
     * @return The colors of the gradient
     */
    public abstract List<T> getColors(int steps, boolean includeStartAndEnd);

    /**
     * Print the gradient
     * @param steps The number of steps
     */
    public void printGradient(int steps) {
        List<T> colors = getColors(steps);
        for (T color : colors) {
            System.out.print(color.toAnsi() + "|");
        }
    }
}
