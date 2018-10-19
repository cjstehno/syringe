package com.stehno.syringe;

import java.util.Optional;
import java.util.function.Consumer;

public final class Syringe {

    // return is a convenience - some instance passed in
    public static <T> T inject(final T target, final Consumer<Injector> injections) {
        final Injector injector = new Injector(target);
        injections.accept(injector);

        final Optional<InjectionFailedException> exes = injector.exceptions();
        if (exes.isPresent()) {
            throw exes.get();
        }

        return target;
    }

    public static <T> Injector injector(final T target){
        final Injector injector = new Injector(target);
        return injector;
    }
}
