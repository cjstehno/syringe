package io.github.cjstehno.syringe.inject;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.lang.reflect.Method;

import static io.github.cjstehno.syringe.inject.Injection.findField;
import static io.github.cjstehno.syringe.inject.Injection.findSetter;

@RequiredArgsConstructor
public class SetInjection implements Injection {

    private final String name;
    private final Object value;
    private final boolean preferSetter;

    @Override
    public void injectInto(final Object instance) throws ReflectiveOperationException {
        if (preferSetter) {
            // use the setter, if there is one
            val setter = findSetter(instance.getClass(), name, value.getClass());
            if (setter.isPresent()) {
                injectWithSetter(instance, setter.get());
            } else {
                // fallback to direct access
                injectDirectly(instance);
            }

        } else {
            // use the direct field access
            injectDirectly(instance);
        }
    }

    private void injectWithSetter(final Object instance, final Method setter) throws ReflectiveOperationException {
        setter.invoke(instance, value);
    }

    private void injectDirectly(final Object instance) throws ReflectiveOperationException {
        val field = findField(instance.getClass(), name);
        if (field.isPresent()) {
            field.get().set(instance, value);
        } else {
            // FIXME: error - no setter or field
            throw new IllegalArgumentException();
        }
    }
}
