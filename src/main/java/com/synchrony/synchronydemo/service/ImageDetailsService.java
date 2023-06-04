package com.synchrony.synchronydemo.service;

import java.util.List;

import com.synchrony.synchronydemo.dom.ImageData;
import com.synchrony.synchronydemo.dom.ResponseWrapper;
import com.synchrony.synchronydemo.models.ImageDetails;

public interface ImageDetailsService {
	
	public List<ImageDetails> getAllImages(String userName);
	
	public ImageDetails viewImage(String userName,String imageHash);
	
	public ResponseWrapper saveAndUploadImage(ImageData image,String userName);
	
	public ResponseWrapper deleteImage(String userName,String imageId);
	
	public void saveUserImageMapping(ImageDetails image);

}
