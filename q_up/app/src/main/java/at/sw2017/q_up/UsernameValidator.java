package at.sw2017.q_up;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tinag on 29.3.2017..
 */

public class UsernameValidator {
    private Pattern pattern;
    private Matcher matcher;

    private static final String USERNAME_PATTERN = "^[a-z0-9._-]{2,25}$";

    public UsernameValidator() {
        this.pattern = Pattern.compile(USERNAME_PATTERN);
    }

    public boolean validate(final String uname){
        matcher = pattern.matcher(uname);
        return matcher.matches();
    }
}
