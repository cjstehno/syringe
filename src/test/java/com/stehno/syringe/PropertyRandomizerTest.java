package com.stehno.syringe;

import com.stehno.syringe.rand.Randomizer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static com.stehno.syringe.rand.ObjectRandomizer.randomize;
import static com.stehno.syringe.rand.Randomizers.intRange;
import static com.stehno.syringe.rand.Randomizers.oneOf;
import static java.lang.String.format;

class PropertyRandomizerTest {

    // TODO: more complex things: maps, lists, arrays (these would just be randomizers)

    @Test @DisplayName("General Usage")
    void general_usage() {
        Randomizer<SomethingElse> rando = randomize(SomethingElse.class, config -> {
            config.property("name", oneOf("alpha", "bravo", "charlie"));
            config.property("score", intRange(10, 100));
            config.field("added", oneOf("one", "two"));
        });

        rando.list(5).forEach(System.out::println);
    }

    @Test @DisplayName("Randomizing nested objects")
    void nested_objects(){
        Randomizer<SomethingElse> randoThing = randomize(SomethingElse.class, config -> {
            config.property("name", oneOf("alpha", "bravo", "charlie"));
            config.property("score", intRange(10, 100));
            config.field("added", oneOf("one", "two"));
        });

        Randomizer<Holder> rando = randomize(Holder.class, cfg->{
            cfg.property("thing", randoThing);
        });

        rando.list(5).forEach(System.out::println);
    }

    @Test @DisplayName("General Usage (with global types)")
    void general_usage_with_types() {
        Randomizer<SomethingElse> rando = randomize(SomethingElse.class, config -> {
            config.propertyType(String.class, oneOf("alpha", "bravo", "charlie"));
            config.property("name", oneOf("Bob", "Joe"));
            config.fieldType(int.class, intRange(25, 50));
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

    static class Holder {

        private SomethingElse thing;

        public SomethingElse getThing() {
            return thing;
        }

        public void setThing(SomethingElse thing) {
            this.thing = thing;
        }

        @Override public String toString() {
            return format("Holder{thing=%s}", thing);
        }
    }

    static class Collector {

        private List<String> strings = new LinkedList<>();

        public List<String> getStrings() {
            return strings;
        }

        public void setStrings(List<String> strings) {
            this.strings = strings;
        }

        @Override public String toString() {
            return "Collector{" + "strings=" + strings + '}';
        }
    }
}