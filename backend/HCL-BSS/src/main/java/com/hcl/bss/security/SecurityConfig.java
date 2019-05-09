package com.hcl.bss.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;

/**
 * This class is used to configure spring security for authentication and
 * authorization
 * 
 * @author aditya-gu
 *
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		config.setAllowCredentials(true);
		config.setExposedHeaders(ImmutableList.of("x-auth-token"));
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
		CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter();
		customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
		return customUsernamePasswordAuthenticationFilter;
	}

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	/*
	 * @Autowired AuthenticationSuccessHandler authenticationSuccessHandler;
	 */

	private CustomAuthenticationFailureHandler myFailureHandler = new CustomAuthenticationFailureHandler();

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	/*
	 * @SuppressWarnings("deprecation") public static NoOpPasswordEncoder
	 * passwordEncoder() { return (NoOpPasswordEncoder)
	 * NoOpPasswordEncoder.getInstance(); }
	 */

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();

		http.cors();

		http.authorizeRequests()

				.antMatchers("/login/**").permitAll().antMatchers("/product/**", "/rate/**")
				.hasAnyRole("Agent", "Admin").antMatchers("/download/**", "/batch/**", "/users/**", "/upload/**")
				.hasAnyRole("Admin").antMatchers("/subscription/**").hasAnyRole("Agent", "Business", "Admin")
				.antMatchers("/dashboard/**").hasAnyRole("Business", "Admin").anyRequest().authenticated().and()
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
				.authenticationEntryPoint(restAuthenticationEntryPoint).and()
				.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		http.sessionManagement().maximumSessions(2);
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")).invalidateHttpSession(true).logoutSuccessUrl("/login").deleteCookies("JSESSIONID");
	}

	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		return daoAuthenticationProvider;
	}

	public CustomUsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
		CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationFailureHandler(myFailureHandler);
		return filter;
	}

	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs",
				"/configuration/**", "/subscriptionNotification/**", "/emailSubscriptionDetail/**");
	}
}
