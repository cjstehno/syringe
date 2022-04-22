/**
 * Copyright (C) 2022 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.cjstehno.syringe.mapping;

import lombok.Data;
import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.Test;

import static io.github.cjstehno.syringe.mapping.ObjectMapper.mapper;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectMapperTest {

    // FIXME: support for merging two source values into one dest?
    /*
        merge
        mergeFields
        mergeProperties

        map.merge("firstName", "lastName").into,..,.
     */

    @Test void mappings() throws Exception {
        val mapper = mapper(map -> {
            map.property("fullName").intoProperty("lastName").using(full -> {
                String fullName = (String) full;
                return fullName.substring(fullName.lastIndexOf(" ") + 1);
            });
            // TODO: bug when using intoProperty
            map.property("age").intoField("age").using(age -> Integer.parseInt((String) age));
        });

        val mapped = (ObjectB) mapper.apply(new ObjectA("John Smith", "42"), new ObjectB());
        assertEquals("Smith", mapped.getLastName());
        assertEquals(42, mapped.getAge());
    }

    @Value
    static class ObjectA {

        String fullName;
        String age;
    }

    @Data
    static class ObjectB {

        private String lastName;
        private int age;
    }
}