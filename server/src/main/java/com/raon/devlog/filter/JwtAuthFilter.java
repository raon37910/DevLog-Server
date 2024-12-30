package com.raon.devlog.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.raon.devlog.component.auth.JwtTokenProvider;
import com.raon.devlog.service.auth.model.TokenClaim;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	public JwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
			String accessToken = authorizationHeader.substring(7);
			TokenClaim tokenClaim = jwtTokenProvider.parseToken(accessToken);
			List<SimpleGrantedAuthority> authorities = tokenClaim.roles()
				.stream()
				.map(SimpleGrantedAuthority::new)
				.toList();

			Authentication authentication = new TestingAuthenticationToken(tokenClaim.subject(), "password",
				authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}
