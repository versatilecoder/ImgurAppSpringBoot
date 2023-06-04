package com.synchrony.synchronydemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.synchrony.synchronydemo.models.ImageDetails;
import com.synchrony.synchronydemo.service.ImageDetailsService;

@Component
public class KafkaListener {

	Logger logger = LoggerFactory.getLogger(KafkaListener.class);

	@Autowired
	ImageDetailsService imageDetailsService;

	@org.springframework.kafka.annotation.KafkaListener(topics = "synchronyTask", containerFactory = "kafkaListenerContainerFactory")
	public void listen(ImageDetails image) {
		imageDetailsService.saveUserImageMapping(image);
		logger.info("Received Messasge in group - group-id: " + image.getImageUrl());
	}

}
