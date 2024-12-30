package com.raon.devlog.component.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.raon.devlog.component.error.DevlogException;
import com.raon.devlog.component.error.ErrorType;

@Component
public class PasswordValidator {

	private final PasswordEncoder passwordEncoder;

	public PasswordValidator(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void validatePassword(String password, String hashedPassword) {
		if (!passwordEncoder.matches(password, hashedPassword)) {
			throw new DevlogException(ErrorType.AUTHENTICATION_ERROR);
		}
	}
}
