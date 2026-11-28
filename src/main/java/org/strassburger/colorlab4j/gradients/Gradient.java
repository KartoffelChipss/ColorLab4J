package org.strassburger.colorlab4j.gradients;

import org.strassburger.colorlab4j.color.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class Gradient<T extends Color> {
    public final List<T> colors;

    /**
     * Create a new gradient
     * @param colors The list of colors that define the gradient
     * @throws IllegalArgumentException If the list is null, contains fewer than two colors, or contains null
     */
    public Gradient(List<T> colors) {
        this.colors = validateColors(colors);
    }

    /**
     * Create a new gradient
     * @param start The start color
     * @param end The end color
     * @throws IllegalArgumentException If either color is null
     */
    public Gradient(T start, T end) {
        this.colors = validateColors(Arrays.asList(start, end));
    }

    /**
     * Create a new gradient
     * @param colors The colors that define the gradient
     * @throws IllegalArgumentException If the array is null, contains fewer than two colors, or contains null
     */
    @SafeVarargs
    public Gradient(T... colors) {
        this.colors = validateColors(colors == null ? null : Arrays.asList(colors));
    }

    /**
     * Get the colors of the gradient (including the start and end color)
     * @param steps The number of steps (excluding the start and end color)
     * @return The colors of the gradient
     * @throws IllegalArgumentException If steps is negative
     */
    public List<T> getColors(int steps) {
        return getColors(steps, true);
    }

    /**
     * Get the colors of the gradient
     * @param steps The number of steps
     * @param includeStartAndEnd Whether to include the start and end color
     * @return The colors of the gradient
     * @throws IllegalArgumentException If steps is negative
     */
    public abstract List<T> getColors(int steps, boolean includeStartAndEnd);

    /**
     * Print the gradient
     * @param steps The number of steps
     * @throws IllegalArgumentException If steps is negative
     */
    public void printGradient(int steps) {
        List<T> colors = getColors(steps);
        for (T color : colors) {
            System.out.print(color.toAnsi() + "|");
        }
    }

    /**
     * Check that a step count is valid. Implementations of {@link #getColors(int, boolean)} should call this first.
     * @param steps The number of steps
     * @throws IllegalArgumentException If steps is negative
     */
    protected static void validateSteps(int steps) {
        if (steps < 0) {
            throw new IllegalArgumentException("Steps must not be negative, got " + steps);
        }
    }

    /**
     * Parse HEX color codes into colors of a gradient's color space.
     * Used by the {@code fromHex} factories of the gradient implementations.
     * @param hexCodes The HEX color codes (e.g. "#ff0000" or "#f00")
     * @param parser The color space's {@code fromHex} method
     * @return The parsed colors, in order
     * @throws IllegalArgumentException If fewer than two codes are given or any code is null or invalid
     */
    protected static <C extends Color> List<C> parseHexCodes(String[] hexCodes, Function<String, C> parser) {
        if (hexCodes == null || hexCodes.length < 2) {
            throw new IllegalArgumentException("At least two colors are required for the gradient.");
        }
        List<C> parsed = new ArrayList<>(hexCodes.length);
        for (int i = 0; i < hexCodes.length; i++) {
            try {
                parsed.add(parser.apply(hexCodes[i]));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid color at index " + i + ": " + e.getMessage(), e);
            }
        }
        return parsed;
    }

    private static <T> List<T> validateColors(List<T> colors) {
        if (colors == null || colors.size() < 2) {
            throw new IllegalArgumentException("At least two colors are required for the gradient.");
        }
        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i) == null) {
                throw new IllegalArgumentException("Gradient color at index " + i + " must not be null.");
            }
        }
        return List.copyOf(colors);
    }

    @Override
    public int hashCode() {
        return colors.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Gradient<?> gradient)) return false;
        return colors.equals(gradient.colors);
    }

    @Override
    public String toString() {
        return "Gradient{" +
                "colors=" + colors +
                '}';
    }
}
