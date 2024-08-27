package com.integral.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class AppConfig {
	// here we configure to remove Default Generated password given by Spring Boot
	// application
	// below is the code here
	// important class try to understand clearly
	@Bean//using this we can access home routh but any other routh
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(//we need to provide which endpoint vaidly access which endpoint should be less accessable
						Authorizae -> Authorizae.requestMatchers("/api/**").authenticated()
						.anyRequest()
						.permitAll())//without jwt token they can get portfolio data and profile data 
				        .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
				        .csrf(csrf -> csrf.disable())//find use of this
				        .cors(cors -> cors.configurationSource(corsConfigrationSource()));
		return http.build();
		

	}

	// simple meaning is that if a particular frontend mean our frontend try to
	// access our backend then only
	// provide data otherwise not...
	private CorsConfigurationSource corsConfigrationSource() {//import for frontend to tackle error for frontend soo 
		return null;
	}
}
