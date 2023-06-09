package com.synchrony.synchronydemo.adapter;

import java.io.IOException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synchrony.synchronydemo.dom.ImageResponseWrapper;
import com.synchrony.synchronydemo.dom.ResponseWrapper;

/**
 * @author pranav
 *
 */
/**
 * @author prana
 *
 */
/**
 * @author prana
 *
 */
/**
 * @author prana
 *
 */
@Service
public class ImgurAdapter {

	Logger logger = LoggerFactory.getLogger(ImgurAdapter.class);

	private static String CLIENT_ID = "135fb26f2114353";

	/**
	 * For calling the imgur API to upload image
	 */

	public ImageResponseWrapper uploadImage(MultipartFile file) throws IOException {
		logger.info("Entering uploadImage method inside ImgurAdapter");
		ObjectMapper objectMapper = new ObjectMapper();
		String dataImage = Base64.getEncoder().encodeToString(file.getBytes());

		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer ab7253a75d4eb8f27b1c4bac153799a9a064d8cf");

		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("image", dataImage);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		String url = "https://api.imgur.com/3/image";
		ImageResponseWrapper wrapper = new ImageResponseWrapper();
		try {
			RestTemplate rt = new RestTemplate();
			ResponseEntity<String> response = rt.exchange(url, HttpMethod.POST, request, String.class);
			wrapper = objectMapper.readValue(response.getBody(), ImageResponseWrapper.class);

			logger.trace(wrapper.getData().getDeletehash() + "---" + wrapper.getData().getLink());

		} catch (Exception e) {
			logger.error("Exception occurred", e);
		}
		logger.error("Exiting uploadImage method inside ImgurAdapter");
		return wrapper;
	}

	/**
	 * For calling the imgur API to delete image
	 */
	public ResponseWrapper deleteImage(String imageHash) throws IOException {

		logger.info("Entering deleteImage method inside ImgurAdapter");
		ObjectMapper objectMapper = new ObjectMapper();

		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer ab7253a75d4eb8f27b1c4bac153799a9a064d8cf");

		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<String> request = new HttpEntity<>(headers);
		String url = "https://api.imgur.com/3/image/" + imageHash;
		ResponseWrapper wrapper = new ResponseWrapper();
		try {
			RestTemplate rt = new RestTemplate();
			ResponseEntity<String> response = rt.exchange(url, HttpMethod.DELETE, request, String.class);
			wrapper = objectMapper.readValue(response.getBody(), ResponseWrapper.class);

		} catch (Exception e) {
			logger.error("Exception occurred", e);
		}
		logger.info("Exiting deleteImage method inside ImgurAdapter");
		return wrapper;
	}

	/**
	 * For calling the imgur API to view image
	 */
	public ImageResponseWrapper viewImage(String imageHash) throws IOException {

		logger.info("Entering viewImage method inside ImgurAdapter");
		ObjectMapper objectMapper = new ObjectMapper();

		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer ab7253a75d4eb8f27b1c4bac153799a9a064d8cf");

		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<String> request = new HttpEntity<>(headers);
		String url = "https://api.imgur.com/3/image" + imageHash;
		ImageResponseWrapper wrapper = new ImageResponseWrapper();
		try {
			RestTemplate rt = new RestTemplate();
			ResponseEntity<String> response = rt.exchange(url, HttpMethod.DELETE, request, String.class);
			wrapper = objectMapper.readValue(response.getBody(), ImageResponseWrapper.class);
			logger.trace(wrapper.getData().getDeletehash() + "---" + wrapper.getData().getLink());

		} catch (Exception e) {
			logger.error("Exception occurred", e);
		}
		logger.info("Exiting viewImage method inside ImgurAdapter");
		return wrapper;
	}

}
