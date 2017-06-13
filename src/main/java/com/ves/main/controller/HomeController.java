package com.ves.main.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/")
public class HomeController {
	@RequestMapping(method = RequestMethod.GET)
	public String admin(ModelMap model, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		System.out.println("Authenticated user is `" + name + "`");

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		System.out.println("Roles: ");
		for (GrantedAuthority grantedAuthority : authorities) {
			System.out.println("\t" + grantedAuthority.getAuthority());
		}
		return "home";
	}
}
