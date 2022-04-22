package com.stehno.syringe.inject;

import com.stehno.syringe.rand.Randomizer;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Utility providing a simple means of injecting field values into objects using reflection, via a builder-style
 * or DSL-style interface.
 */
public final class Injector {

    private final Object target;
    private final Class<?> targetClass;
    private final List<Throwable> exceptions = new LinkedList<>();

    private Injector(final Object target) {
        this.target = target;
        this.targetClass = target.getClass();
    }

    /**
     * Used to inject data into one or more fields using reflection, based on the provided injections.
     *
     * @param target     the target object where properties will be injected
     * @param injections the field injection configuration
     * @param <T>        the type of the target object
     * @return the instance of the target object passed in (for convenience)
     */
    public static <T> T inject(final T target, final Consumer<Injector> injections) {
        final Injector injector = new Injector(target);
        injections.accept(injector);

        final Optional<InjectionFailedException> exes = injector.exceptions();
        if (exes.isPresent()) {
            throw exes.get();
        }

        return target;
    }

    public static <T> Injector injector(final T target) {
        final Injector injector = new Injector(target);
        return injector;
    }

    // FIXME: use setters if available (allow skip)?
    // allow useSetters config (boolean) at global level
    // SetOptions(DEFAULT, USE_SETTER, IGNORE_SETTER)

    /**
     * Sets the value of a named field or property to the provided value.
     *
     * @param name
     * @param value
     * @return
     */
    public Injector set(final String name, final Object value) {
        try {
            findField(name).set(target, value);
        } catch (Exception ex) {
            exceptions.add(ex);
        }
        return this;
    }

    public Injector randomize(final String name, final Randomizer<?> randomizer) {
        return set(name, randomizer.one());
    }

    public <V> Injector get(final String name, final Class<V> type, final Consumer<V> valueConsumer) {
        try {
            valueConsumer.accept((V) findField(name).get(target));
        } catch (Exception ex) {
            exceptions.add(ex);
        }
        return this;
    }

    public <V> V get(final String name) {
        try {
            return (V) findField(name).get(target);
        } catch (Exception ex) {
            exceptions.add(ex);
            return null;
        }
    }

    public <K, V> Injector map(final String name, final Consumer<Map<K, V>> consumer) {
        try {
            consumer.accept((Map<K, V>) findField(name).get(target));
        } catch (Exception ex) {
            exceptions.add(ex);
        }
        return this;
    }

    public <K, V> Map<K, V> map(final String name) {
        try {
            return (Map<K, V>) findField(name).get(target);
        } catch (Exception ex) {
            exceptions.add(ex);
            return null;
        }
    }

    public <V> Injector list(final String name, final Consumer<List<V>> consumer) {
        try {
            consumer.accept((List<V>) findField(name).get(target));
        } catch (Exception ex) {
            exceptions.add(ex);
        }
        return this;
    }

    public <V> List<V> list(final String name) {
        try {
            return (List<V>) findField(name).get(target);
        } catch (Exception ex) {
            exceptions.add(ex);
            return null;
        }
    }

    public <V> Injector array(final String name, final Consumer<V[]> consumer) {
        try {
            consumer.accept((V[]) findField(name).get(target));
        } catch (Exception ex) {
            exceptions.add(ex);
        }
        return this;
    }

    public <V> V[] array(final String name) {
        try {
            return (V[]) findField(name).get(target);
        } catch (Exception ex) {
            exceptions.add(ex);
            return null;
        }
    }

    Optional<InjectionFailedException> exceptions() {
        return exceptions.isEmpty() ? Optional.empty() : Optional.of(new InjectionFailedException(exceptions));
    }

    private Field findField(final String name) throws NoSuchFieldException {
        final Field field = findField(targetClass, name).orElseThrow(() -> new NoSuchFieldException(name));
        field.setAccessible(true);
        return field;
    }

    private static Optional<Field> findField(final Class<?> inClass, final String name) {
        try {
            Field field = inClass.getDeclaredField(name);
            return Optional.of(field);
        } catch (NoSuchFieldException nsfe) {
            return inClass.getSuperclass() != Object.class ? findField(inClass.getSuperclass(), name) : Optional.empty();
        }
    }
}
