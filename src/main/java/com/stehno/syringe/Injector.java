package com.stehno.syringe;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

final class Injector {

    private final Object target;
    private final Class<?> targetClass;
    private final List<Throwable> exceptions = new LinkedList<>();

    Injector(final Object target) {
        this.target = target;
        this.targetClass = target.getClass();
    }

    public Injector set(final String name, final Object value) {
        try {
            findField(name).set(target, value);
        } catch (Exception ex) {
            exceptions.add(ex);
        }
        return this;
    }

    public <V> Injector get(final String name, final Class<V> type, final Consumer<V> valueConsumer) {
        try {
            final V value = (V) findField(name).get(target);
            valueConsumer.accept(value);
        } catch (Exception ex) {
            exceptions.add(ex);
        }
        return this;
    }

    public <V> V get(final String name, final Class<V> type) {
        try {
            return (V) findField(name).get(target);
        } catch (Exception ex) {
            exceptions.add(ex);
            return null;
        }
    }

    public <K, V> Injector map(final String name, final Class<K> keyType, final Class<V> valueType, final Consumer<Map<K, V>> consumer) {
        try {
            consumer.accept((Map<K, V>) findField(name).get(target));
        } catch (Exception ex) {
            exceptions.add(ex);
        }
        return this;
    }

    public <K, V> Map<K, V> map(final String name, final Class<K> keyType, final Class<V> valueType) {
        try {
            return (Map<K, V>) findField(name).get(target);
        } catch (Exception ex) {
            exceptions.add(ex);
            return null;
        }
    }

    // TODO: list, array

    Optional<InjectionFailedException> exceptions() {
        return exceptions.isEmpty() ? Optional.empty() : Optional.of(new InjectionFailedException(exceptions));
    }

    private Field findField(final String name) throws NoSuchFieldException {
        final Field field = targetClass.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }
}
