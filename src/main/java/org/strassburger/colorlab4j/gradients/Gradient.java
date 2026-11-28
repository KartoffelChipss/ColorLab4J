package org.strassburger.colorlab4j.gradients;

import org.jspecify.annotations.Nullable;
import org.strassburger.colorlab4j.color.Color;

import java.util.ArrayList;
import java.util.Arrays;
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
     * @param steps The number of interpolated colors to generate (see {@link #getColors(int, boolean)})
     * @return The colors of the gradient
     * @throws IllegalArgumentException If steps is negative
     */
    public List<T> getColors(int steps) {
        return getColors(steps, true);
    }

    /**
     * Get the colors of the gradient.
     * <p>
     * {@code steps} is the number of interpolated colors generated between the stops; the stops themselves
     * are not counted. Every middle stop always appears in the result, at its position in the gradient.
     * The steps are spread as evenly as possible over the segments between stops, and any remainder goes
     * to the first segments. The result therefore contains {@code steps + (colors.size() - 2)} colors,
     * plus 2 if {@code includeStartAndEnd} is true.
     * <p>
     * Example: red → green → blue with 3 steps gives
     * red, 2 colors between red and green, green, 1 color between green and blue, blue.
     *
     * @param steps The number of interpolated colors to generate
     * @param includeStartAndEnd Whether to include the first and last stop
     * @return The colors of the gradient
     * @throws IllegalArgumentException If steps is negative
     */
    public List<T> getColors(int steps, boolean includeStartAndEnd) {
        validateSteps(steps);

        int segments = colors.size() - 1;
        int baseSteps = steps / segments;
        int extraSteps = steps % segments;

        List<T> result = new ArrayList<>(steps + colors.size());
        if (includeStartAndEnd) {
            result.add(colors.get(0));
        }

        for (int i = 0; i < segments; i++) {
            T start = colors.get(i);
            T end = colors.get(i + 1);
            int segmentSteps = baseSteps + (i < extraSteps ? 1 : 0);

            for (int j = 1; j <= segmentSteps; j++) {
                double factor = (double) j / (segmentSteps + 1);
                result.add(interpolate(start, end, factor));
            }

            boolean isLastStop = i + 1 == segments;
            if (!isLastStop || includeStartAndEnd) {
                result.add(end);
            }
        }

        return result;
    }

    /**
     * Get exactly {@code steps} colors, evenly spaced along the whole gradient.
     * <p>
     * Unlike {@link #getColors(int)}, the result size never depends on the number of stops.
     * The first color is the first stop and the last color is the last stop. The stops are treated
     * as evenly spaced along the gradient, so a middle stop only appears when one of the evenly
     * spaced positions lands exactly on it. For example, red → green → blue with 5 steps gives
     * red, red/green blend, green, green/blue blend, blue; with 4 steps green is not hit exactly.
     * <p>
     * With 0 steps the result is empty, and with 1 step it contains only the first stop.
     *
     * @param steps The number of colors to return
     * @return Exactly {@code steps} colors
     * @throws IllegalArgumentException If steps is negative
     */
    public List<T> getColorsExact(int steps) {
        validateSteps(steps);
        if (steps == 0) return new ArrayList<>();
        if (steps == 1) return new ArrayList<>(List.of(colors.get(0)));

        int segments = colors.size() - 1;
        int intervals = steps - 1;
        List<T> result = new ArrayList<>(steps);

        for (int k = 0; k < steps; k++) {
            // Position along the gradient is k / intervals, measured in segments.
            // Integer math keeps positions that land exactly on a stop exact.
            long scaled = (long) k * segments;
            int segment = (int) (scaled / intervals);
            long remainder = scaled % intervals;

            if (remainder == 0) {
                result.add(colors.get(segment));
            } else {
                double factor = (double) remainder / intervals;
                result.add(interpolate(colors.get(segment), colors.get(segment + 1), factor));
            }
        }

        return result;
    }

    /**
     * Blend two colors of this gradient's color space.
     * @param start The color at factor 0
     * @param end The color at factor 1
     * @param factor The position between the two colors, strictly between 0 and 1
     * @return The blended color
     */
    protected abstract T interpolate(T start, T end, double factor);

    /**
     * Linear interpolation between two values.
     * @param start The value at factor 0
     * @param end The value at factor 1
     * @param factor The position between the two values
     * @return The interpolated value
     */
    protected static double lerp(double start, double end, double factor) {
        return start + factor * (end - start);
    }

    /**
     * Linear interpolation that is clamped to [min, max], so floating-point overshoot
     * near a bound cannot produce a value the color constructor rejects.
     */
    protected static double lerp(double start, double end, double factor, double min, double max) {
        return Math.max(min, Math.min(max, lerp(start, end, factor)));
    }

    /**
     * Interpolate between two hues (in degrees) along the shorter way around the color wheel.
     * @param startHue The hue at factor 0 (0-360)
     * @param endHue The hue at factor 1 (0-360)
     * @param factor The position between the two hues
     * @return The interpolated hue, in [0, 360)
     */
    protected static double interpolateHue(double startHue, double endHue, double factor) {
        double diff = endHue - startHue;
        if (diff > 180) {
            startHue += 360;
        } else if (diff < -180) {
            endHue += 360;
        }
        double hue = lerp(startHue, endHue, factor) % 360;
        return hue < 0 ? hue + 360 : hue;
    }

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
     * Check that a step count is valid.
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

    private static <T> List<T> validateColors(@Nullable List<T> colors) {
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
    public boolean equals(@Nullable Object obj) {
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
