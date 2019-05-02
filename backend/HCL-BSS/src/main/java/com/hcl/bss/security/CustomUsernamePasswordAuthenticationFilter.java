package com.hcl.bss.security;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hcl.bss.dto.LoginRequest;
import com.hcl.bss.dto.UserAuthDto;
import com.hcl.bss.services.UserServices;

/**
 * This class is used to read the login information
 * 
 * @author aditya-gu
 *
 */
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private String userId;
	private String password;

	@Autowired
	UserServices userServices;

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		String password = null;

		if ("application/json".equals(request.getHeader("Content-Type"))) {
			password = this.password;
		} else {
			password = super.obtainPassword(request);
		}

		return password;
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		String username = null;

		if ("application/json".equals(request.getHeader("Content-Type"))) {
			username = this.userId;
		} else {
			username = super.obtainUsername(request);
		}

		return username;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		if ("application/json".equals(request.getHeader("Content-Type"))) {
			try {
				/*
				 * HttpServletRequest can be read only once
				 */
				StringBuffer sb = new StringBuffer();
				String line = null;

				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				// json transformation
				ObjectMapper mapper = new ObjectMapper();
				LoginRequest loginRequest = mapper.readValue(sb.toString(), LoginRequest.class);

				this.userId = loginRequest.getUserId();
				this.password = loginRequest.getPassword();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
		// return super.attemptAuthentication(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}
		if (password == null) {
			password = "";
		}

		/*
		 * String usernameDomain = String.format("%s%s%s", username.trim(),
		 * String.valueOf(Character.LINE_SEPARATOR), domain);
		 */
		return new UsernamePasswordAuthenticationToken(username, password);
	}

	public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		/*
		 * response.setHeader("Access-Control-Expose-Headers", "x-uth-token");
		 * response.setHeader("Access-Control-Allow-Credentials", "x-auth-token");
		 */

		if (userServices == null) {
			ServletContext servletContext = request.getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			userServices = webApplicationContext.getBean(UserServices.class);
		}

		UserAuthDto userAuthDto = userServices.getAuthorizationDetail(authResult.getName());

		// response.setHeader("AuthResponse", userAuthDto.toString());

		// json conversion
		response.setContentType("application/json");

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userAuthDto);
		ServletOutputStream out = response.getOutputStream();
		out.print(json);
		SecurityContextHolder.getContext().setAuthentication(authResult);
	}
}
