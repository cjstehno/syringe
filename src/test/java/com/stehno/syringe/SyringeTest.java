package com.stehno.syringe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stehno.syringe.inject.Injector.inject;
import static com.stehno.syringe.inject.Injector.injector;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsArrayContainingInOrder.arrayContaining;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

class SyringeTest {

    @Test @DisplayName("General Usage (DSL)")
    void general_usage_dsl() {
        SomeObject someObject = inject(new SomeObject(), inj -> {
            inj.set("text", "Ouch!");
            inj.get("numbers", List.class, list -> list.add(42));
            inj.get("attrs", Map.class, map -> {
                map.put("foo", "bar");
                map.put("baz", "buzz");
            });
        });

        assertThat(someObject.text, equalTo("Ouch!"));
        assertThat(someObject.numbers, hasItem(42));
        assertThat(someObject.attrs, allOf(
            hasEntry("foo", "bar"),
            hasEntry("baz", "buzz")
        ));

        String textValue = injector(someObject).get("text");
        assertThat(textValue, equalTo("Ouch!"));
    }

    @Test @DisplayName("General Usage (superclass)")
    void general_usage_superclass() {
        OtherObject object = inject(new OtherObject(), inj -> {
            inj.set("label", "child");
            inj.set("text", "Oof");
            inj.get("numbers", List.class, list -> list.add(42));
            inj.map("attrs").put("hello", "there");
        });

        assertThat(object.getText(), equalTo("Oof"));
        assertThat(object.label, equalTo("child"));
        assertThat(object.getNumbers(), hasItem(42));
        assertThat(object.getAttrs(), hasEntry("hello", "there"));
    }

    @Test @DisplayName("General Usage (maps, lists, and arrays)")
    void general_usage_special() {
        OtherObject someObject = inject(new OtherObject(), inj -> {
            inj.map("attrs", map -> map.put("foo", "buzz"));
            inj.list("numbers", list -> list.add(202));
            inj.array("flags", array -> {
                array[0] = true;
                array[2] = true;
            });
        });

        assertThat(someObject.getText(), nullValue());
        assertThat(someObject.getNumbers(), hasItem(202));
        assertThat(someObject.getAttrs(), hasEntry("foo", "buzz"));
        assertThat(someObject.flags, arrayContaining(true, false, true));
    }

    @Test @DisplayName("General Usage (Fluid)")
    void general_usage_fluid() {
        SomeObject someObject = new SomeObject();

        injector(someObject)
            .set("text", "Hey!")
            .get("numbers", List.class, list -> list.add(101))
            .get("attrs", Map.class, map -> map.put("alpha", "bravo"));

        assertThat(someObject.text, equalTo("Hey!"));
        assertThat(someObject.numbers, hasItem(101));
        assertThat(someObject.attrs, hasEntry("alpha", "bravo"));
    }

    static class SomeObject {

        private String text;
        private final List<Integer> numbers = new ArrayList<>();
        private Map<String, String> attrs = new HashMap<>();

        public String getText() {
            return text;
        }

        public List<Integer> getNumbers() {
            return numbers;
        }

        public Map<String, String> getAttrs() {
            return attrs;
        }
    }

    static class OtherObject extends SomeObject {

        private String label;
        private Boolean[] flags = {false, false, false};
    }
}
