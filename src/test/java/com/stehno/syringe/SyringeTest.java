package com.stehno.syringe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stehno.syringe.Syringe.inject;
import static com.stehno.syringe.Syringe.injector;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.nullValue;
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

        String textValue = injector(someObject).get("text", String.class);
        assertThat(textValue, equalTo("Ouch!"));
    }

    @Test @DisplayName("General Usage (maps)")
    void general_usage_maps() {
        SomeObject someObject = inject(new SomeObject(), inj -> {
            inj.map("attrs", String.class, String.class, map -> {
                map.put("foo", "buzz");
            });
        });

        assertThat(someObject.text, nullValue());
        assertThat(someObject.numbers, empty());
        assertThat(someObject.attrs, hasEntry("foo", "buzz"));
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

    }
}
