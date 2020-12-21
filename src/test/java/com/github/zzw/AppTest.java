package com.github.zzw;

import static zzw.Scope.runWithNewScope;
import static zzw.ScopeKey.allocate;

import org.junit.Test;

import com.github.zzw.impl.Scope;
import com.github.zzw.impl.ScopeKey;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void test() {
        basicUse();
    }

    private static final ScopeKey<String> TEST_KEY = allocate();

    public void basicUse() {
        Scope.runWithNewScope(() -> {
            TEST_KEY.set("abc");
            String result = TEST_KEY.get(); // get "abc"
            System.out.println(result);

        });
    }
}
