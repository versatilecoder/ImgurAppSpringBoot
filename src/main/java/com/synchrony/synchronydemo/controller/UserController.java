package com.synchrony.synchronydemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.synchrony.synchronydemo.models.User;
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

	/**
	 * This api gives all user information .
	 * 
	 * @param requestTokenHeader
	 * @return
	 * @throws Exception
	 */

	@ApiOperation(value = "User profile details")
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
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
		final User userDetails = userDetailsService.fetchUserProfile(username);
		logger.info("Exiting viewUserProfile method inside UserController");
		if (null != userDetails)
			return ResponseEntity.ok(userDetails);
		else
			return new ResponseEntity<String>("Record Not found!", HttpStatus.OK);
	}

}
