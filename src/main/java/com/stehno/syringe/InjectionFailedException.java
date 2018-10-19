package com.stehno.syringe;

import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;

public class InjectionFailedException extends RuntimeException {

    private final List<Throwable> exceptions = new LinkedList<>();

    InjectionFailedException(final List<Throwable> exes){
        this.exceptions.addAll(exes);
    }

    public List<Throwable> getExceptions() {
        return exceptions;
    }

    @Override public String getMessage() {
        return format("There were %d exceptions thrown during injection.", exceptions.size());
    }
}
