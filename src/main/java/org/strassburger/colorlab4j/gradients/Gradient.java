package org.strassburger.colorlab4j.gradients;

import org.strassburger.colorlab4j.color.Color;

import java.util.List;

public abstract class Gradient<T extends Color> {
    public final T start;
    public final T end;

    /**
     * Create a new gradient
     * @param start The start color
     * @param end The end color
     */
    public Gradient(T start, T end) {
        if (start == null || end == null) throw new IllegalArgumentException("Start and end color must not be null");
        this.start = start;
        this.end = end;
    }

    /**
     * Get the start color
     * @return The start color
     */
    public T getStart() {
        return start;
    }

    /**
     * Get the end color
     * @return The end color
     */
    public T getEnd() {
        return end;
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
