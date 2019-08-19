package zzw;

import static zzw.Scope.runWithNewScope;
import static zzw.ScopeKey.allocate;

import org.junit.Test;

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
        runWithNewScope(() -> {
            TEST_KEY.set("abc");
            String result = TEST_KEY.get(); // get "abc"
            System.out.println(result);

        });
    }
}
