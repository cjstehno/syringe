package com.stehno.syringe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.lang.Character.toLowerCase;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public final class PropertyRandomizer {
    // FIXME: move this method to ObjectRandomizer and make that the main entry point

    public static <T> Randomizer<T> randomize(final Class<T> type, final Consumer<PropertyRandomizerConfig> config) {
        final PropertyRandomizerConfigImpl randomizerConfig = new PropertyRandomizerConfigImpl();
        config.accept(randomizerConfig);
        return new ObjectRandomizer<>(type, randomizerConfig);
    }

}

interface PropertyRandomizerConfig {

    <P> PropertyRandomizerConfig property(final String name, final Randomizer<P> randomizer);

    <P> PropertyRandomizerConfig field(final String name, final Randomizer<P> randomizer);
}

class PropertyRandomizerConfigImpl implements PropertyRandomizerConfig {

    private final Map<String, Randomizer<?>> propertyRandomizers = new HashMap<>();
    private final Map<String, Randomizer<?>> fieldRandomizers = new HashMap<>();

    public <P> PropertyRandomizerConfig property(final String name, final Randomizer<P> randomizer) {
        propertyRandomizers.put(name, randomizer);
        return this;
    }

    public <P> PropertyRandomizerConfig field(final String name, final Randomizer<P> randomizer) {
        fieldRandomizers.put(name, randomizer);
        return this;
    }

    Optional<Randomizer<?>> propertyRandomizer(final String name) {
        final Randomizer<?> randomizer = propertyRandomizers.get(name);
        return randomizer != null ? Optional.of(randomizer) : Optional.empty();
    }

    Optional<Randomizer<?>> fieldRandomizer(final String name) {
        final Randomizer<?> randomizer = fieldRandomizers.get(name);
        return randomizer != null ? Optional.of(randomizer) : Optional.empty();
    }
}

class ObjectRandomizer<T> implements Randomizer<T> {

    private final Class<T> type;
    private final PropertyRandomizerConfigImpl randomizerConfig;

    ObjectRandomizer(final Class<T> type, final PropertyRandomizerConfigImpl randomizerConfig) {
        this.type = type;
        this.randomizerConfig = randomizerConfig;
    }

    @Override public T one() {
        try {
            final T instance = type.newInstance();

            for (final Method setter : findSetters(type)) {
                final String propName = propertyName(setter);
                final Optional<Randomizer<?>> propRandomizer = randomizerConfig.propertyRandomizer(propName);
                if (propRandomizer.isPresent()) {
                    setter.invoke(instance, propRandomizer.get().one());
                }
            }

            for (final Field field : findFields(type)) {
                final Optional<Randomizer<?>> fieldRandomizer = randomizerConfig.fieldRandomizer(field.getName());
                if (fieldRandomizer.isPresent()) {
                    field.setAccessible(true);
                    field.set(instance, fieldRandomizer.get().one());
                }
            }

            return instance;

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static String propertyName(final Method method) {
        final String name = method.getName().substring(3);
        return toLowerCase(name.charAt(0)) + name.substring(1);
    }

    // FIXME: these should be moved to a utility
    private static List<Method> findSetters(final Class<?> inClass) {
        final List<Method> setters = stream(inClass.getDeclaredMethods())
            .filter(method -> method.getName().startsWith("set") && method.getParameterCount() == 1)
            .collect(Collectors.toList());

        Class<?> superclass = inClass.getSuperclass();
        while (superclass != Object.class) {
            stream(superclass.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("set") && method.getParameterCount() == 1)
                .forEach(setters::add);

            superclass = superclass.getSuperclass();
        }

        return setters;
    }

    private static List<Field> findFields(final Class<?> inClass) {
        final List<Field> fields = stream(inClass.getDeclaredFields()).collect(Collectors.toList());

        Class<?> superclass = inClass.getSuperclass();
        while (superclass != Object.class) {
            fields.addAll(asList(superclass.getDeclaredFields()));
            superclass = superclass.getSuperclass();
        }

        return fields;
    }
}
