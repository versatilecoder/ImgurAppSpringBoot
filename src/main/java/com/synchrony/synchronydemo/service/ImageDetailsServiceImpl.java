package com.synchrony.synchronydemo.service;

import java.io.IOException;
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
import com.synchrony.synchronydemo.controller.AuthController;
import com.synchrony.synchronydemo.dom.ImageData;
import com.synchrony.synchronydemo.dom.ImageResponseWrapper;
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
	public boolean saveAndUploadImage(ImageData image, String userName) {
		ImageDetails imgDetails = new ImageDetails();
		try {
			Date date = Calendar.getInstance().getTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			ImageResponseWrapper wrapperResponse = imgurAdapter.uploadImage(image.getImage());
			imgDetails.setImageName(image.getTitle());
			imgDetails.setUploadDate(dateFormat.format(date));
			imgDetails.setUserName(userName);
			imgDetails.setImageHash(wrapperResponse.getData().getDeletehash());
			imgDetails.setImageUrl(wrapperResponse.getData().getLink());
			kafkaTemplate.send("synchronyTask", imgDetails);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public List<ImageDetails> getAllImages(String userName) {
		return imageRepository.findAllByUserName(userName);
				
	}
	
	@Override
	public ImageDetails viewImage(String userName,String imageId) {
		try{
		ImageDetails imgDetails = imageRepository.findById(Long.parseLong(imageId))
				.orElseThrow(() -> new RecordNotFoundException("Record Not Found with given image: " + imageId));
		ImageResponseWrapper wrapperResponse = new ImageResponseWrapper();
		
			if(imgDetails.getUserName().equals(userName)){
			String hashId = imgDetails.getImageHash();
		
				wrapperResponse = imgurAdapter.viewImage(hashId);
			
			}
			return imgDetails;
		}catch (Exception e) {
			
		}
		return new ImageDetails();
	}

	@Override
	public boolean deleteImage(String userName,String imageId) {
		Optional<ImageDetails> optional = imageRepository.findById(Long.parseLong(imageId));
		ImageResponseWrapper wrapperResponse = new ImageResponseWrapper();
		if (optional.isPresent()) {
			ImageDetails imgDetails = optional.get();
			
			String hashId = imgDetails.getImageHash();
			try {
				if(imgDetails.getUserName().equals(userName))
				wrapperResponse = imgurAdapter.deleteImage(hashId);
			} catch (Exception e) {

			}
		}
		return true;

	}

	@Override
	public void saveUserImageMapping(ImageDetails image) {
		imageRepository.save(image);

	}

}
