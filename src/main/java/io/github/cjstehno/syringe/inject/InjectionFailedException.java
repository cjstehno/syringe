package io.github.cjstehno.syringe.inject;

import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class InjectionFailedException extends RuntimeException {

    private final List<Throwable> exceptions = new LinkedList<>();

    InjectionFailedException(final List<Throwable> exes) {
        this.exceptions.addAll(exes);
    }

    public List<Throwable> getExceptions() {
        return exceptions;
    }

    @Override public String getMessage() {
        return format(
            "There were %d exceptions thrown during injection (%s)",
            exceptions.size(),
            exceptions.stream().map(throwable -> throwable.getClass().getSimpleName() + ": " + throwable.getMessage()).collect(joining(", "))
        );
    }
}
