package com.synchrony.synchronydemo.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.synchrony.synchronydemo.adapter.ImgurAdapter;
import com.synchrony.synchronydemo.dom.ImageData;
import com.synchrony.synchronydemo.dom.ImageResponseWrapper;
import com.synchrony.synchronydemo.dom.ResponseWrapper;
import com.synchrony.synchronydemo.exceptions.RecordNotFoundException;
import com.synchrony.synchronydemo.models.ImageDetails;
import com.synchrony.synchronydemo.repository.ImageRepository;

@Service
public class ImageDetailsServiceImpl implements ImageDetailsService {

	Logger logger = LoggerFactory.getLogger(ImageDetailsServiceImpl.class);

	@Autowired
	ImgurAdapter imgurAdapter;

	@Autowired
	private KafkaTemplate<String, ImageDetails> kafkaTemplate;

	@Autowired
	ImageRepository imageRepository;

	@Override
	public ResponseWrapper saveAndUploadImage(ImageData image, String userName) {
		ImageDetails imgDetails = new ImageDetails();
		ResponseWrapper responseWrapper = new ResponseWrapper();
		try {
			Date date = Calendar.getInstance().getTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			ImageResponseWrapper wrapperResponse = imgurAdapter.uploadImage(image.getImage());
			imgDetails.setImageName(image.getTitle());
			imgDetails.setUploadDate(dateFormat.format(date));
			imgDetails.setUserName(userName);
			imgDetails.setImageHash(wrapperResponse.getData().getDeletehash());
			imgDetails.setImageUrl(wrapperResponse.getData().getLink());
			kafkaTemplate.send("synchronyTask", imgDetails);
			responseWrapper.setMessage("Successfully Uploaded Image");
			responseWrapper.setStatus(200);
		} catch (Exception e) {
			responseWrapper.setMessage("Exception Occured in Uploaded Image");
			responseWrapper.setError(e.getMessage());
			responseWrapper.setStatus(500);
		}
		return responseWrapper;
	}

	@Override
	public List<ImageDetails> getAllImages(String userName) {
		return imageRepository.findAllByUserName(userName);

	}

	@Override
	public ImageDetails viewImage(String userName, String imageId) {
		try {
			ImageDetails imgDetails = imageRepository.findById(Long.parseLong(imageId))
					.orElseThrow(() -> new RecordNotFoundException("Record Not Found with given image: " + imageId));
			ImageResponseWrapper wrapperResponse = new ImageResponseWrapper();
			logger.trace("Fetched image", imgDetails.getUserName());
			/**
			 * USer can only view his own image
			 */
			if (imgDetails.getUserName().equals(userName)) {
				String hashId = imgDetails.getImageHash();
				logger.trace("Image hash", hashId);
				/*
				 * Get Image returning 404 from imgur so sending url store in DB
				 */
				// wrapperResponse = imgurAdapter.viewImage(hashId);

			}
			return imgDetails;
		} catch (Exception e) {
			logger.error("Exception in fetching image", e);
		}
		return null;
	}

	@Override
	public ResponseWrapper deleteImage(String userName, String imageId) {
		Optional<ImageDetails> optional = imageRepository.findById(Long.parseLong(imageId));
		ImageResponseWrapper wrapperResponse = new ImageResponseWrapper();
		ResponseWrapper response = new ResponseWrapper();
		if (optional.isPresent()) {
			ImageDetails imgDetails = optional.get();

			String hashId = imgDetails.getImageHash();
			logger.trace("Image hash is",hashId);
			logger.trace("userName is",userName+"--"+imgDetails.getUserName());
			try {
				/**
				 * USer can only delete his own image
				 */
				if (imgDetails.getUserName().equals(userName)) {
					response = imgurAdapter.deleteImage(hashId);
					if (response.getStatus() == 200)
						imageRepository.deleteById(Long.parseLong(imageId));
					response.setMessage("Successfully Deleted");
					response.setStatus(200);
				}
			} catch (Exception e) {
				logger.error("Exception in Deleting image");
				response.setMessage("Exception Occurred in DEletion");
				response.setStatus(500);

			}
		}
		return response;

	}

	@Override
	public void saveUserImageMapping(ImageDetails image) {
		imageRepository.save(image);

	}

}
