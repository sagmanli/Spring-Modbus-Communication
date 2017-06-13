package com.ves.main.config;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class ComPassEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (StringUtils.isEmpty(rawPassword)) {
			return false;
		}

		Set<String> keys = ComAuthenticationProvider.roleTypes.keySet();
		for (String key : keys) {
			try {
				if (ComCrypto.encrypt(rawPassword.toString(), key).equals(encodedPassword)) {
					return true;
				}
			} catch (Exception e) {
			}
		}
		return false;
	}
}
