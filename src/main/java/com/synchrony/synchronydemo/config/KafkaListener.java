package com.synchrony.synchronydemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.synchrony.synchronydemo.models.ImageDetails;
import com.synchrony.synchronydemo.service.ImageDetailsService;

@Component
public class KafkaListener {
	
	@Autowired
	ImageDetailsService imageDetailsService;
	
	 @org.springframework.kafka.annotation.KafkaListener(
			  topics = "synchronyTask", 
			  containerFactory = "kafkaListenerContainerFactory")
    public void listen(ImageDetails image) {
		 imageDetailsService.saveUserImageMapping(image);
       System.out.println("Received Messasge in group - group-id: " + image.getImageUrl());
    }

}
