package com.integral.config;

import java.io.IOException;
//import java.security.Key;
import java.util.List;

import javax.crypto.SecretKey;

//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
//import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {// inbuild method

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = request.getHeader(JwtConstant.JWT_HEADER);
//     (Bearer )joken 7 word..
		if(jwt!=null)
		{
			jwt=jwt.substring(7);
			try {
				
                 SecretKey key= Keys.hmacShaKeyFor(JwtConstant.SCREACT_KEY.getBytes());
                 Claims claims= Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                 String email = String.valueOf(claims.get("email"));
                 String authorities= String.valueOf(claims.get("authorities"));
                 List<GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                 UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(//changes i have made
                		 email,
                		 null,
                		 authoritiesList
                		 );
                	SecurityContextHolder.getContext().setAuthentication(auth);	 
                		 
			} catch (Exception e) {
				throw new RuntimeException("Invalid Token...");
			}
		}
		filterChain.doFilter(request, response);
	}

}
