package com.synchrony.synchronydemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.synchrony.synchronydemo.security.JwtUtils;
import com.synchrony.synchronydemo.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/v1")
@RestController
@CrossOrigin
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private JwtUtils jwtTokenUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@ApiOperation(value = "User profile details")
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public ResponseEntity<?> viewUserProfile(@RequestHeader(value = "Authorization") String requestTokenHeader)
			throws Exception {
		logger.info("Entering viewUserProfile method inside UserController");
		String username = null;
		String jwtToken = null;

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				logger.error("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				logger.error("JWT Token has expired");
			}
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		logger.info("Exiting viewUserProfile method inside UserController");
		return ResponseEntity.ok(userDetails);
	}

}
