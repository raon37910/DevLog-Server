package com.raon.devlog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.raon.devlog.component.auth.JwtTokenProvider;
import com.raon.devlog.component.auth.security.CustomAccessDeniedHandler;
import com.raon.devlog.component.auth.security.CustomAuthenticationEntryPoint;
import com.raon.devlog.filter.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private static final String[] AUTH_ALLOWLIST = {
		"/swagger-ui/**", "/login/**", "/images/**", "/app/token/**"
	};

	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final JwtTokenProvider jwtTokenProvider;

	public SecurityConfig(
		CustomAuthenticationEntryPoint authenticationEntryPoint,
		CustomAccessDeniedHandler accessDeniedHandler,
		JwtTokenProvider jwtTokenProvider) {
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.accessDeniedHandler = accessDeniedHandler;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors(Customizer.withDefaults());

		http.sessionManagement(
			sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.formLogin(AbstractHttpConfigurer::disable);

		http.addFilterBefore(new JwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

		http.authorizeHttpRequests(authorize -> authorize
			.requestMatchers(AUTH_ALLOWLIST).permitAll()
			.requestMatchers(HttpMethod.POST, "/api/users").permitAll()
			.requestMatchers(HttpMethod.POST, "/api/auth/token").permitAll()
			.requestMatchers(HttpMethod.POST, "/api/auth/token/refresh").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/category").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/articles").permitAll()
			.requestMatchers("/api/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated()
		);

		http.exceptionHandling((handling) -> handling.authenticationEntryPoint(authenticationEntryPoint)
			.accessDeniedHandler(accessDeniedHandler));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
