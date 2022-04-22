package io.github.cjstehno.syringe.inject;

import lombok.val;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import static java.util.Locale.ROOT;

public interface Injection {

    void injectInto(final Object instance) throws ReflectiveOperationException;

    // FIXME: pull these out into a util
    static Optional<Field> findField(final Class<?> inClass, final String name) {
        try {
            val field = inClass.getDeclaredField(name);
            field.setAccessible(true);
            return Optional.of(field);
        } catch (final NoSuchFieldException nsfe) {
            return inClass.getSuperclass() != Object.class ? findField(inClass.getSuperclass(), name) : Optional.empty();
        }
    }

    static Optional<Method> findSetter(final Class<?> inClass, final String propertyName, final Class<?> valueType) {
        val methodName = "set" + propertyName.substring(0, 1).toUpperCase(ROOT) + propertyName.substring(1);

        try {
            val method = inClass.getDeclaredMethod(methodName, valueType);
            method.setAccessible(true);
            return Optional.of(method);
        } catch (final NoSuchMethodException e) {
            return inClass.getSuperclass() != Object.class
                ? findSetter(inClass.getSuperclass(), propertyName, valueType)
                : Optional.empty();
        }
    }

    static Optional<Method> findGetter(final Class<?> inClass, final String propertyName) {
        val methodName = "get" + propertyName.substring(0, 1).toUpperCase(ROOT) + propertyName.substring(1);

        try {
            val method = inClass.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return Optional.of(method);
        } catch (final NoSuchMethodException e) {
            return inClass.getSuperclass() != Object.class
                ? findGetter(inClass.getSuperclass(), propertyName)
                : Optional.empty();
        }
    }
}
