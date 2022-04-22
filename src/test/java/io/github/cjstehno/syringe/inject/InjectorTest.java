package io.github.cjstehno.syringe.inject;

import lombok.Getter;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.cjstehno.syringe.inject.Injector.inject;
import static io.github.cjstehno.syringe.inject.Injector.injector;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InjectorTest {

    // FIXME: test exceptions
    // FIXME: test sub-class injecttion

    @Test @DisplayName("Injector - setting (direct)")
    void injectorSetters() throws Exception {
        val injector = injector(inj -> {
            inj.set("text", "some text");
            inj.set("label", "a label");
            inj.set("attrs", Map.of("alpha", "one"));
        });

        val injected = injector.inject(new OtherObject());
        assertEquals("some text", injected.getText());
        assertEquals("a label", injected.label);
        assertEquals(Map.of("alpha", "one"), injected.getAttrs());
    }

    @Test @DisplayName("Inject - setting (direct)")
    void injectSetters() throws Exception {
        val injected = inject(new OtherObject(), inj -> {
            inj.set("text", "some text");
            inj.set("attrs", Map.of("alpha", "one"));
        });

        assertEquals("some text", injected.getText());
        assertEquals(Map.of("alpha", "one"), injected.getAttrs());
    }

    @Test @DisplayName("Injector - setting (setter)")
    void injectSettersWithSetter() throws Exception {
        val injected = inject(new SomeObject(), inj -> {
            inj.set("text", "some text", true);
        });

        assertEquals("setter:some text", injected.getText());
    }

    @Test @DisplayName("Injector - setting (setter)")
    void injectSettersWithSetterOther() throws Exception {
        val injected = inject(new OtherObject(), inj -> {
            inj.set("text", "some text", true);
        });

        assertEquals("setter:some text", injected.getText());
    }

    @Test void injectUpdate() throws Exception {
        val obj = inject(new OtherObject(), inj -> {
            inj.set("text", "alpha");
        });

        val injected = inject(obj, inj -> {
            inj.update("text", txt -> txt + "-updated");
        });

        assertEquals("alpha-updated", injected.getText());
    }

    @Test void modification() throws Exception{
        val injected = inject(new OtherObject(), inj -> {
            inj.modify("attrs", x -> {
                val map = (Map<String, String>) x;
                map.put("one", "1");
            });
        });

        assertEquals(1, injected.getAttrs().size());
        assertEquals("1", injected.getAttrs().get("one"));
    }

    static class SomeObject {

        @Getter private String text;
        private final List<Integer> numbers = new ArrayList<>();
        @Getter private Map<String, String> attrs = new HashMap<>();

        public void setText(final String value) {
            this.text = "setter:" + value;
        }
    }

    static class OtherObject extends SomeObject {

        private String label;
        private Boolean[] flags = {false, false, false};
    }
}
