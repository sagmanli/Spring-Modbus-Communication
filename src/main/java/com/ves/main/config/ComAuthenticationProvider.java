package com.ves.main.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import com.ves.main.GlobalVariables;

public class ComAuthenticationProvider implements UserDetailsService {
	@SuppressWarnings("serial")
	public static Map<String, String> roleTypes = new LinkedHashMap<String, String>() {
		                                            {
			                                            put("223b6e1c-a7b4-4567-8d49-ec993b82bb3c", "ROLE_ADMIN");
			                                            put("b9d17ad0-daeb-11e3-9c1a-0800200c9a66", "ROLE_VERIFIED");
			                                            put("b9d17ad0-daeb-11e3-9c1a-0800200c9a65", "ROLE_XX");
		                                            }
	                                            };

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isEmpty(username)) {
			return null;
		}

		User user = null;

		for (com.ves.main.integration.jaxb.user.userlist.User userTMP : GlobalVariables.getUsers()) {
			if (!username.equals(userTMP.getUsername())) {
				continue;
			}

			final String password = userTMP.getPassword();

			@SuppressWarnings("serial")
			List<GrantedAuthority> authority = new ArrayList<GrantedAuthority>() {
				{
					Set<String> keys = roleTypes.keySet();
					for (String key : keys) {
						try {
							ComCrypto.decrypt(password, key);
							add(new SimpleGrantedAuthority(roleTypes.get(key)));
						} catch (Exception e) {
						}
					}
				}
			};
			user = new User(username, password, true, true, true, true, authority);
			break;
		}

		return user;
	}
}
