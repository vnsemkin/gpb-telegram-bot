package org.vnsemkin.semkintelegrambot.domain.models;

import lombok.NonNull;

import java.util.Optional;
import java.util.function.Consumer;

public final class Result<T> {
    private final T data;
    private final Throwable error;

    private Result(T data, Throwable error) {
        this.data = data;
        this.error = error;
    }

    public static <T> Result<T> success(@NonNull T data) {
        return new Result<>(data, null);
    }

    public static <T> Result<T> failure(@NonNull Throwable error) {
        return new Result<>(null, error);
    }

    public void ifSuccess(Consumer<? super T> consumer) {
        if (isSuccess()) {
            consumer.accept(data);
        }
    }

    public boolean isSuccess() {
        return data != null && error == null;
    }

    public boolean isFailure() {
        return error != null;
    }

    public Optional<T> getData() {
        return isSuccess() ? Optional.of(data) : Optional.empty();
    }

    public Optional<Throwable> getError() {
        return isFailure() ? Optional.of(error) : Optional.empty();
    }
}
