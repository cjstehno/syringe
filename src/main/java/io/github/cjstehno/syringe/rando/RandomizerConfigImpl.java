package io.github.cjstehno.syringe.rando;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// beware of when prop|field type configs overlap
// property (setter) randomization should be preferred over field-based
class RandomizerConfigImpl implements RandomizerConfig {

    private final Map<Class<?>, Randomizer<?>> propertyTypeRandomizers = new HashMap<>();
    private final Map<String, Randomizer<?>> propertyRandomizers = new HashMap<>();
    private final Map<Class<?>, Randomizer<?>> fieldTypeRandomizers = new HashMap<>();
    private final Map<String, Randomizer<?>> fieldRandomizers = new HashMap<>();

    public <P> RandomizerConfig property(final String name, final Randomizer<P> randomizer) {
        propertyRandomizers.put(name, randomizer);
        return this;
    }

    @Override public <P> RandomizerConfig propertyType(Class<P> type, Randomizer<P> randomizer) {
        propertyTypeRandomizers.put(type, randomizer);
        return this;
    }

    public <P> RandomizerConfig field(final String name, final Randomizer<P> randomizer) {
        fieldRandomizers.put(name, randomizer);
        return this;
    }

    public <P> RandomizerConfig fieldType(final Class<P> type, final Randomizer<P> randomizer) {
        fieldTypeRandomizers.put(type, randomizer);
        return this;
    }

    Optional<Randomizer<?>> propertyTypeRandomizer(final Class<?> type) {
        final Randomizer<?> randomizer = propertyTypeRandomizers.get(type);
        return randomizer != null ? Optional.of(randomizer) : Optional.empty();
    }

    Optional<Randomizer<?>> propertyRandomizer(final String name) {
        final Randomizer<?> randomizer = propertyRandomizers.get(name);
        return randomizer != null ? Optional.of(randomizer) : Optional.empty();
    }

    Optional<Randomizer<?>> fieldRandomizer(final String name) {
        final Randomizer<?> randomizer = fieldRandomizers.get(name);
        return randomizer != null ? Optional.of(randomizer) : Optional.empty();
    }

    Optional<Randomizer<?>> fieldTypeRandomizer(final Class<?> type) {
        final Randomizer<?> randomizer = fieldTypeRandomizers.get(type);
        return randomizer != null ? Optional.of(randomizer) : Optional.empty();
    }
}
