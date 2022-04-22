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

import lombok.val;

import java.util.LinkedList;
import java.util.List;

import static io.github.cjstehno.syringe.mapping.MappingSourceImpl.SourceMappingType.*;

public class MappingImpl implements Mapping {

    private List<MappingSourceImpl> sources = new LinkedList<>();

    @Override public MappingSource field(String name) {
        val source = new MappingSourceImpl(name, FIELD);
        sources.add(source);
        return source;
    }

    @Override public MappingSource property(String name) {
        val source = new MappingSourceImpl(name, PROPERTY);
        sources.add(source);
        return source;
    }

    @Override public MappingSource map(String name) {
        val source = new MappingSourceImpl(name, MAP);
        sources.add(source);
        return source;
    }

    Object apply(final Object source, final Object dest) throws ReflectiveOperationException {
        for (val mappingSource : sources) {
            mappingSource.apply(source, dest);
        }
        return dest;
    }
}
