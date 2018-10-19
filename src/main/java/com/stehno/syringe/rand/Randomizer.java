package com.stehno.syringe.rand;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * `Randomizer` implementations are used to produce random instances of the specified object type on-demand.
 *
 * @param <T> the type of random object produced
 */
public interface Randomizer<T> {

    /**
     * Used to generate a single random instance of the target class.
     *
     * @return a random instance of the target class
     */
    T one();

    /**
     * Used to generate a list of `count` randomly generated instances of the target class.
     *
     * @param count the number of items in the list
     * @return an unmodifiable list of randomly generated items
     */
    default List<T> list(int count) {
        // NOTE: not simplifying this method to allow exceptions to propagate properly
        final List<T> values = new ArrayList<>(10);

        for (int i = 0; i < count; i++) {
            values.add(one());
        }

        return unmodifiableList(values);
    }
}
