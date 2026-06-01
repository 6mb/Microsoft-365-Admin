package cn.itbat.microsoft.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class ConfigPasswordEncoder implements PasswordEncoder {
    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]?\\$.{56}$");

    private final BCryptPasswordEncoder delegate = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null) {
            return false;
        }
        if (BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
            return delegate.matches(rawPassword, encodedPassword);
        }
        if (rawPassword.toString().equals(encodedPassword)) {
            return true;
        }
        return rawPassword.toString().equals(DigestUtils.md5DigestAsHex(encodedPassword.getBytes(StandardCharsets.UTF_8)));
    }
}
