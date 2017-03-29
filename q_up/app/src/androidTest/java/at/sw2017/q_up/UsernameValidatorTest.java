package at.sw2017.q_up;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by tinag on 29.3.2017..
 */
public class UsernameValidatorTest {

    private static UsernameValidator usernameValidator;

    @BeforeClass
    public static void initialize() {
        usernameValidator = new UsernameValidator();
    }

    @Test
    public void test1() {
        boolean res = usernameValidator.validate("asdfg");
        assertEquals(res, true);
    }

    @Test
    public void test2() {
        boolean res = usernameValidator.validate("ad_r");
        assertEquals(res, true);
    }

    @Test
    public void test3() {
        boolean res = usernameValidator.validate("kjk.kdj-hdh");
        assertEquals(res, true);
    }

    @Test
    public void test4() {
        boolean res = usernameValidator.validate("hhs!k");
        assertEquals(res, false);
    }
}









