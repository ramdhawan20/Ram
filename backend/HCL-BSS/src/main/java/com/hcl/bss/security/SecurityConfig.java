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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hcl.bss.controllers.UserManagementController;
import com.hcl.bss.domain.Role;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("Content-Type");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
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
    
/*    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.jdbcAuthentication().dataSource(dataSource).withUser("dave")
                .password("secret").roles("USER");
    }*/

	@Autowired
	private UserDetailsService userDetailsService;
    

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;
    
    //@Autowired
    private SimpleUrlAuthenticationSuccessHandler  mySuccessHandler = new SimpleUrlAuthenticationSuccessHandler();

    //private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();
    private CustomAuthenticationFailureHandler myFailureHandler = new CustomAuthenticationFailureHandler();

    
	
	/*
	 * @Bean public PasswordEncoder passwordEncoder(){ PasswordEncoder encoder = new
	 * BCryptPasswordEncoder(); return encoder; }
	 */
    
	
	  @SuppressWarnings("deprecation") public static NoOpPasswordEncoder
	  passwordEncoder() { return (NoOpPasswordEncoder)
	  NoOpPasswordEncoder.getInstance(); }
	 
    
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		auth.authenticationProvider(authProvider());
	}
	
/*	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/*.css");
		web.ignoring().antMatchers("/*.js");
	}*/
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
			
		http.csrf().disable();
		
		http.cors();
		
		http.authorizeRequests()
			.antMatchers("/login/**").permitAll()
			.antMatchers("/product/**").hasAnyRole("Agent","Admin")
			.antMatchers("/rate/**").hasAnyRole("Agent","Admin")
			.antMatchers("/upload/**").hasAnyRole("Agent","Admin")
			.antMatchers("/downloadRecord").hasAnyRole("Admin")			
			.antMatchers("/batch/**").hasAnyRole("Admin")
			.antMatchers("/subscriptions").hasAnyRole("Agent","Business","Admin")
			.antMatchers("/dashboard/**").hasAnyRole("Business","Admin")
			.antMatchers("/userm/**").hasAnyRole("Admin")									 
			.anyRequest().authenticated()
			.and().exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(restAuthenticationEntryPoint)
			.and().addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
		http.sessionManagement()
			.maximumSessions(2);		
		http.logout()
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID");
		}
	
	public AuthenticationProvider authProvider(){
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
	
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/swagger-ui.html","/webjars/**","/swagger-resources/**","/v2/api-docs","/configuration/**");
	}
}
