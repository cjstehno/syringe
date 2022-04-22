package io.github.cjstehno.syringe.inject;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.Consumer;

import static lombok.AccessLevel.PRIVATE;

/**
 * FIXME: document
 */
@RequiredArgsConstructor(access = PRIVATE)
public final class Injector {

    private final InjectionsImpl injections;

    public static Injector injector(final Consumer<Injections> prescription) {
        val injections = new InjectionsImpl();
        prescription.accept(injections);
        return new Injector(injections);
    }

    public static <T> T inject(final T instance, final Consumer<Injections> prescription) throws ReflectiveOperationException {
        return injector(prescription).inject(instance);
    }

    // TODO: better excepotion?
    public <T> T inject(final T instance) throws ReflectiveOperationException {
        return injections.apply(instance);
    }
}
