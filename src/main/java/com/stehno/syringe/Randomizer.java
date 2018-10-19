package com.stehno.syringe;

import java.util.ArrayList;
import java.util.List;

interface Randomizer<T> {

    T one();

    default List<T> list(int count) {
        // NOTE: not simplifying this method to allow exceptions to propagate properly
        final List<T> values = new ArrayList<>(10);

        for (int i = 0; i < count; i++) {
            values.add(one());
        }

        return values;
    }
}
