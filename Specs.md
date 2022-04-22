# Specification

## Injection

Reflective injection of data into objects.

**preferSetters(boolean)** - global default value for whether setters should be preferred if they exist
**preferGetters(boolean)** - global default value for whether getters should be perferred, if they exist

**set(String name, Object value)** - sets the value of the named field (default setter behavior)
**set(String name, Object value, boolean preferSetters)** - sets the value of the named field

**modify(String name, Class type, Consumer modifier)**
- provided modifiers: list, map, array, boolean flip

**get(String name)** - gets the value of the field (default getter behavior)
**get(String name, boolean preferGetters)** - gets the value of the field


// TBD: randomize(String name, Randomizer rando)




## Randomization

## Mapping