package com.stehno.syringe.rand;

interface RandomizerConfig {

    <P> RandomizerConfig property(final String name, final Randomizer<P> randomizer);

    <P> RandomizerConfig propertyType(final Class<P> type, final Randomizer<P> randomizer);

    <P> RandomizerConfig field(final String name, final Randomizer<P> randomizer);

    <P> RandomizerConfig fieldType(final Class<P> type, final Randomizer<P> randomizer);
}
