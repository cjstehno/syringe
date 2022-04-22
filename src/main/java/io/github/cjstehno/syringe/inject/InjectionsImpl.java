package io.github.cjstehno.syringe.inject;

import lombok.val;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class InjectionsImpl implements Injections {

    private List<Injection> injections = new LinkedList<>();

    @Override public Injections set(final String name, final Object value, final boolean preferSetter) {
        injections.add(new SetInjection(name, value, preferSetter));
        return this;
    }

    @Override public Injections update(String name, Function<Object,Object> updater, boolean preferProps) {
        injections.add(new UpdateInjection(name, updater, preferProps));
        return this;
    }

    @Override public Injections modify(final String name, final Consumer<Object> modifier) {
        injections.add(new ModifyInjection(name, modifier));
        return this;
    }

    public <T> T apply(final T instance) throws ReflectiveOperationException {
        for (val injection : injections) {
            injection.injectInto(instance);
        }
        return instance;
    }
}
