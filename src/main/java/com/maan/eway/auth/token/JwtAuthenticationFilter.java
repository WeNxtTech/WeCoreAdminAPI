package com.maan.eway.auth.token;

import static com.maan.eway.auth.token.Constants.HEADER_STRING;
import static com.maan.eway.auth.token.Constants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.maan.eway.bean.SessionDetails;
import com.maan.eway.repository.SessionDetailsRepository;




public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Lazy
	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private SessionDetailsRepository sessionRep;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)	throws IOException, ServletException {

		String header = req.getHeader(HEADER_STRING);
		String username = null;
		String authToken = null;
		String companyId = null;
		SessionDetails table = new SessionDetails();
		HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
		if (header != null && header.startsWith(TOKEN_PREFIX)) {
			authToken = header.replace(TOKEN_PREFIX, "");
			table = sessionRep.findByTempTokenid(authToken);
			
			companyId = table.getCompanyId();
			authToken = table.getTokenId();
	        requestWrapper.addHeader(HEADER_STRING, authToken);
			try {
				username = jwtTokenUtil.getUsernameFromToken(authToken);
			} catch (IllegalArgumentException e) {
				logger.error("an error occured during getting username from token", e);
			} catch (Exception e) {
				logger.warn("the token is expired and not valid anymore");
				username = table.getTokenId();
			} 
		} else {
			logger.warn("couldn't find bearer string, will ignore the header");
		}
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			//Added for getting companyid
			String combinedUsername = username + ":" + companyId;
			
			UserDetails userDetails = userDetailsService.loadUserByUsername(combinedUsername);
			boolean validtoken = jwtTokenUtil.validateToken(username, userDetails,req);
			
			if(validtoken && table.getStatus().equals("DE-ACTIVE")) {
				validtoken = false;
				res.setHeader(username, "This session has been terminated. User logged in to a new session");
			}
			
			if (validtoken) {
								
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(requestWrapper));
				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else if (validtoken) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(requestWrapper));
				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(requestWrapper, res);
	}
}