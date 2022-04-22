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

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.Function;

import static io.github.cjstehno.syringe.inject.Injection.findField;
import static io.github.cjstehno.syringe.inject.Injection.findSetter;

@RequiredArgsConstructor
public class MappingDestinationImpl implements MappingDestination {

    private final String destinationName;
    private final DestinationMappingType destinationMappingType;
    private Function<Object, Object> transformer = a -> a;

    @Override public void using(Function<Object, Object> transformer) {
        this.transformer = transformer;
    }

    void apply(Object sourceValue, Object destObj) throws ReflectiveOperationException {
        val modifiedValue = transformer.apply(sourceValue);

        switch (destinationMappingType) {
            case FIELD -> setField(destObj, modifiedValue);
            case PROPERTY -> setProperty(destObj, modifiedValue);
            case MAP -> set(destObj, modifiedValue);
        }
    }

    enum DestinationMappingType {
        FIELD, PROPERTY, MAP;
    }

    private void setField(final Object destObj, final Object destValue) throws ReflectiveOperationException {
        val field = findField(destObj.getClass(), destinationName);
        if (field.isPresent()) {
            field.get().set(destObj, destValue);
        } else {
            // FIXME: error
            throw new IllegalArgumentException();
        }
    }

    private void setProperty(final Object destObj, final Object destValue) throws ReflectiveOperationException {
        val setter = findSetter(destObj.getClass(), destinationName, destValue.getClass());
        if (setter.isPresent()) {
            setter.get().invoke(destObj, destValue);
        } else {
            // FIXME: error
            throw new IllegalArgumentException();
        }
    }

    private void set(final Object destObj, final Object destValue) throws ReflectiveOperationException {
        if (findSetter(destObj.getClass(), destinationName, destValue.getClass()).isPresent()) {
            setProperty(destObj, destValue);
        } else {
            setField(destObj, destValue);
        }
    }
}
