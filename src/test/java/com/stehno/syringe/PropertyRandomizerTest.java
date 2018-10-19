package com.stehno.syringe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

import static com.stehno.syringe.Randomizers.intRange;
import static com.stehno.syringe.Randomizers.oneOf;
import static java.lang.String.format;

// TODO: add random injectors to syringe?

class PropertyRandomizerTest {

    // TODO: more complex things: maps, lists, arrays, objects
    // TODO: provide type randomizers so that if no specific randomizer exists it will randomize for that type

    @Test @DisplayName("General Usage")
    void general_usage() {
        Randomizer<SomethingElse> rando = PropertyRandomizer.randomize(SomethingElse.class, config -> {
            config.property("name", oneOf("alpha", "bravo", "charlie"));
            config.property("score", intRange(10, 100));
            config.field("added", oneOf("one", "two"));
        });

        rando.list(5).forEach(System.out::println);
    }

    @Test @DisplayName("General Usage (with global types)")
    void general_usage_with_types() {
        Randomizer<SomethingElse> rando = PropertyRandomizer.randomize(SomethingElse.class, config -> {
            config.propertyType(String.class, oneOf("alpha", "bravo", "charlie"));
//            config.fieldType(int.class, );
        });

        rando.list(5).forEach(System.out::println);
    }

    static class Something {

        private String name;
        private int score;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override public String toString() {
            return format("Something{name='%s', score=%d}", name, score);
        }
    }

    static class SomethingElse extends Something {

        private String added;

        public String getAdded() {
            return added;
        }

        public void setAdded(String added) {
            this.added = "setter-" + added;
        }

        @Override public String toString() {
            return format("SomethingElse{name=%s, score=%d, added=%s}", getName(), getScore(), added);
        }
    }
}