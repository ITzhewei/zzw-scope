package zzw;

import java.util.concurrent.ConcurrentHashMap;

import com.github.phantomthief.util.ThrowableRunnable;
import com.github.phantomthief.util.ThrowableSupplier;

/**
 * @author zhangzhewei
 */
public final class Scope {

    private static final ThreadLocal<Scope> SCOPE_THREAD_LOCAL = new ThreadLocal<>();

    private final ConcurrentHashMap<ScopeKey<?>, Object> values = new ConcurrentHashMap<>();

    public <T> void set(ScopeKey<T> key, T value) {
        if (value != null) {
            values.put(key, value);
        } else {
            values.remove(key);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ScopeKey<T> key) {
        T value = (T) values.get(key);
        if (value == null && key.getInitializer() != null) {
            value = key.getInitializer().get();
            if (value != null) {
                values.put(key, value);
            }
        }
        return value != null ? value : key.getDefaultValue();
    }

    public static Scope beginScope() {
        Scope scope = SCOPE_THREAD_LOCAL.get();
        if (scope != null) {
            throw new IllegalStateException("beginScope scope when scope not null");
        }
        scope = new Scope();
        SCOPE_THREAD_LOCAL.set(scope);
        return scope;
    }

    public static void endScope() {
        SCOPE_THREAD_LOCAL.remove();
    }

    public static Scope getCurrentScope() {
        return SCOPE_THREAD_LOCAL.get();
    }

    public static <T, X extends Throwable> T supplyWithNewScope(ThrowableSupplier<T, X> supplier)
            throws X {
        beginScope();
        try {
            return supplier.get();
        } finally {
            endScope();
        }
    }

    public static <X extends Throwable> void runWithNewScope(ThrowableRunnable<X> runnable)
            throws X {
        supplyWithNewScope(() -> {
            runnable.run();
            return null;
        });
    }

    public static <T, X extends Throwable> T supplyWithExistScope(Scope scope,
            ThrowableSupplier<T, X> supplier) throws X {
        Scope oldScope = SCOPE_THREAD_LOCAL.get();
        SCOPE_THREAD_LOCAL.set(scope);
        try {
            return supplier.get();
        } finally {
            if (oldScope != null) {
                SCOPE_THREAD_LOCAL.set(oldScope);
            } else {
                SCOPE_THREAD_LOCAL.remove();
            }
        }
    }

    public static <X extends Throwable> void runWithExistScope(Scope scope,
            ThrowableRunnable<X> runnable) throws X {
        supplyWithExistScope(scope, () -> {
            runnable.run();
            return null;
        });
    }

}
