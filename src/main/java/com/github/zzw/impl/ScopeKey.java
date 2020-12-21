package com.github.zzw.impl;

import java.util.function.Supplier;

/**
 * @author zhangzhewei
 */
public class ScopeKey<T> {

    private T defaultValue;
    private Supplier<T> initializer;

    public ScopeKey(T defaultValue, Supplier<T> initializer) {
        this.defaultValue = defaultValue;
        this.initializer = initializer;
    }

    public ScopeKey(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T get() {
        Scope scope = Scope.getCurrentScope();
        if (scope != null) {
            return scope.get(this);
        }
        return defaultValue;
    }

    public boolean set(T value) {
        Scope scope = Scope.getCurrentScope();
        if (scope != null) {
            scope.set(this, value);
            return true;
        }
        return false;
    }

    public static <T> ScopeKey<T> allocate() {
        return new ScopeKey<>(null);
    }

    public static <T> ScopeKey<T> withInitalizer(Supplier<T> supplier) {
        return new ScopeKey<>(null, supplier);
    }

    public static <T> ScopeKey<T> withAnyDefaultValue(T value) {
        return new ScopeKey<>(value, null);
    }

    public static ScopeKey<Integer> withDefaultValue(int value) {
        return new ScopeKey<>(value);
    }

    public static ScopeKey<String> withDefaultValue(String value) {
        return new ScopeKey<>(value);
    }

    public static ScopeKey<Long> withDefaultValue(long value) {
        return new ScopeKey<>(value);
    }

    public static ScopeKey<Double> withDefaultValue(double value) {
        return new ScopeKey<>(value);
    }

    public static ScopeKey<Boolean> withDefaultValue(boolean value) {
        return new ScopeKey<>(value);
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public Supplier<T> getInitializer() {
        return initializer;
    }

}
