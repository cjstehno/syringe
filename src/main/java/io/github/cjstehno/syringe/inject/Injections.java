package io.github.cjstehno.syringe.inject;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Injections {

    // injects a value into a setter or field
    default Injections set(final String name, final Object value) {
        return set(name, value, false);
    }

    Injections set(final String name, final Object value, final boolean preferSetters);

    // FIXME: test
    // sets a field based on a transformation of the current value
    default Injections update(final String name, final Function<Object,Object> updater) {
        return update(name, updater, false);
    }

    Injections update(final String name, final Function<Object,Object> updater, boolean preferProps);

    // FIXME: test
    Injections modify(final String name, final Consumer<Object> modifier);

    // FIXME: randomize?
}
