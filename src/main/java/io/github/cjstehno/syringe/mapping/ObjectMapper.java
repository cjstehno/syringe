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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectMapper {

    /*
        field(source).intoField(field).using()
        field(source).intoProperty(name).using()
        property(soruce).intoField...
        property(source).intoProperty(...
        map(source).into(dest)...                   - field-first, then prop
     */

    private final MappingImpl mapping;

    public static ObjectMapper mapper(final Consumer<Mapping> mappings) {
        val m = new MappingImpl();
        mappings.accept(m);
        return new ObjectMapper(m);
    }

    public Object apply(final Object source, final Object dest) throws ReflectiveOperationException {
        return mapping.apply(source, dest);
    }
}
