package io.github.cjstehno.syringe.inject;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ModifyInjection implements Injection {

    private final String name;
    private final Consumer<Object> modifier;

    @Override public void injectInto(Object instance) throws ReflectiveOperationException {
        val field = Injection.findField(instance.getClass(), name);
        if (field.isPresent()) {
            val currentValue = field.get().get(instance);
            modifier.accept(currentValue);

        } else {
            // FIXME: error
            throw new IllegalArgumentException();
        }
    }
}
