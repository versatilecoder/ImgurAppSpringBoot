package com.synchrony.synchronydemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synchrony.synchronydemo.dom.ImageData;
import com.synchrony.synchronydemo.dom.ResponseWrapper;
import com.synchrony.synchronydemo.models.ImageDetails;
import com.synchrony.synchronydemo.security.JwtUtils;
import com.synchrony.synchronydemo.service.ImageDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.ApiOperation;

/**
 * @author Pranav.Pandey
 *
 */
@RequestMapping("/api/v1")
@RestController
@CrossOrigin
public class ImageController {

	Logger logger = LoggerFactory.getLogger(ImageController.class);

	@Autowired
	ImageDetailsService imageDetailsService;

	@Autowired
	private JwtUtils jwtTokenUtil;

	/**
	 * This API will give list of images mapped to username
	 */
	@ApiOperation(value = "View All Image List")
	@RequestMapping(value = "/image", method = RequestMethod.GET)
	public ResponseEntity<?> viewAllImage(@RequestHeader(value = "Authorization") String requestTokenHeader)
			throws Exception {
		logger.info("Entering viewAllImage method inside ImageController");

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

		logger.info("Exiting viewAllImage method inside ImageController");
		return ResponseEntity.ok(imageDetailsService.getAllImages(username));
	}

	/**
	 * 
	 * @param requestTokenHeader
	 * @param imageId
	 * @return
	 * @throws Exception
	 *             This API gives image URL for the given Id
	 */
	@ApiOperation(value = "View Image by Id")
	@RequestMapping(value = "/image/{imageId}", method = RequestMethod.GET)
	public ResponseEntity<?> viewImage(@RequestHeader(value = "Authorization") String requestTokenHeader,
			@PathVariable("imageId") String imageId) throws Exception {
		logger.info("Entering viewImage method inside ImageController for id:" + imageId);

		String username = null;
		String jwtToken = null;

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				logger.info("userName from jwt : "+username);
			} catch (IllegalArgumentException e) {
				logger.error("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				logger.error("JWT Token has expired");
			}
		}

		logger.info("Exiting viewImage method inside ImageController");
		ImageDetails imgDetail = imageDetailsService.viewImage(username, imageId);
		if (null != imgDetail)
			return ResponseEntity.ok(imgDetail);
		else
			return new ResponseEntity<String>("No Record Found!", HttpStatus.OK);
	}

	/**
	 * This API will Upload the image to imgur and pass the details to Kafka for
	 * storing to DB
	 * 
	 * @param requestTokenHeader
	 * @param image
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Upload Image")
	@RequestMapping(value = "/image", method = RequestMethod.POST)
	public ResponseEntity<?> uploadImage(@RequestHeader(value = "Authorization") String requestTokenHeader,
			ImageData image) throws Exception {
		logger.info("Entering uploadImage method inside ImageController");
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

		ResponseWrapper responseWrapper=imageDetailsService.saveAndUploadImage(image, username);
		logger.info("Exiting uploadImage method inside ImageController");
		return new ResponseEntity<ResponseWrapper>(responseWrapper, HttpStatus.OK);
	}

	/**
	 * This API is used to delete the Image
	 * 
	 * @param requestTokenHeader
	 * @param imageId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Delete Image")
	@RequestMapping(value = "/image/{imageId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteImage(@RequestHeader(value = "Authorization") String requestTokenHeader,
			@PathVariable("imageId") String imageId) throws Exception {

		logger.info("Entering deleteImage method inside ImageController");
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

		ResponseWrapper responseWrapper=imageDetailsService.deleteImage(username, imageId);
		logger.info("Exiting deleteImage method inside ImageController");
		return new ResponseEntity<ResponseWrapper>(responseWrapper, HttpStatus.OK);
	}

}
