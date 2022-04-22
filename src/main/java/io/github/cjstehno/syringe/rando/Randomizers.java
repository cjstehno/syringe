/**
 * Copyright (C) 2022 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.cjstehno.syringe.rando;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.ThreadLocalRandom.current;

public final class Randomizers {

    public static <V> Randomizer<V> oneOf(final V... options) {
        return new OneOfRandomizer<>(options);
    }

    public static Randomizer<Integer> intRange(final int min, final int max) {
        return new IntRangeRandomizer(min, max);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class OneOfRandomizer<T> implements Randomizer<T> {

        private final T[] values;

        @Override public T one() {
            return values[current().nextInt(values.length)];
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class IntRangeRandomizer implements Randomizer<Integer> {

        private final int min, max;

        @Override public Integer one() {
            return ThreadLocalRandom.current().nextInt(min, max);
        }
    }
}

