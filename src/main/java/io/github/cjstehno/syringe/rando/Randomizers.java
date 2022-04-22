package io.github.cjstehno.syringe.rando;

import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.ThreadLocalRandom.current;

public final class Randomizers {

    public static <V> Randomizer<V> oneOf(final V... options) {
        return new OneOfRandomizer<>(options);
    }

    public static Randomizer<Integer> intRange(final int min, final int max) {
        return new IntRangeRandomizer(min, max);
    }

    private static class OneOfRandomizer<T> implements Randomizer<T> {

        private final T[] values;

        OneOfRandomizer(final T[] values) {
            this.values = values;
        }

        @Override public T one() {
            return values[current().nextInt(values.length)];
        }
    }

    private static class IntRangeRandomizer implements Randomizer<Integer> {

        private final int min, max;

        IntRangeRandomizer(final int min, final int max) {
            this.min = min;
            this.max = max;
        }

        @Override public Integer one() {
            return ThreadLocalRandom.current().nextInt(min, max);
        }
    }
}

