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

import io.github.cjstehno.syringe.inject.Injection;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import static io.github.cjstehno.syringe.inject.Injection.findField;
import static io.github.cjstehno.syringe.inject.Injection.findGetter;
import static io.github.cjstehno.syringe.mapping.MappingDestinationImpl.DestinationMappingType.*;

@RequiredArgsConstructor
public class MappingSourceImpl implements MappingSource{

    private final String sourceName;
    private final SourceMappingType sourceMappingType;
    private MappingDestinationImpl destination;

    @Override public MappingDestination intoField(String name) {
        destination = new MappingDestinationImpl(name, FIELD);
        return destination;
    }

    @Override public MappingDestination intoProperty(String name) {
        destination = new MappingDestinationImpl(name, PROPERTY);
        return destination;
    }

    @Override public MappingDestination into(String name) {
        destination = new MappingDestinationImpl(name, MAP);
        return destination;
    }

    @Override public MappingDestination intoField() {
        return null;
    }

    @Override public MappingDestination into() {
        return null;
    }

    @Override public MappingDestination intoProperty() {
        return null;
    }

    void apply(Object source, Object dest) throws ReflectiveOperationException {
        val sourceValue = switch (sourceMappingType){
            case FIELD -> fieldValue(source);
            case PROPERTY -> propertyValue(source);
            case MAP -> mapValue(source);
        };

        destination.apply(sourceValue, dest);
    }

    private Object fieldValue(final Object source) throws ReflectiveOperationException {
        val field = findField(source.getClass(), sourceName);
        if( field.isPresent()){
            return field.get().get(source);
        } else {
            // FIXME: error
            throw new IllegalArgumentException();
        }
    }

    private Object propertyValue(final Object source) throws ReflectiveOperationException {
        val getter = findGetter(source.getClass(), sourceName);
        if( getter.isPresent()){
            return getter.get().invoke(source);
        } else {
            // FIXME: error
            throw new IllegalArgumentException();
        }
    }

    // property first
    private Object mapValue(final Object source) throws ReflectiveOperationException {
        if(findGetter(source.getClass(), sourceName).isPresent()){
            return propertyValue(source);
        } else {
            return fieldValue(source);
        }
    }

    enum SourceMappingType {
        FIELD, PROPERTY, MAP;
    }
}
