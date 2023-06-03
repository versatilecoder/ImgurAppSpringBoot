package com.synchrony.synchronydemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synchrony.synchronydemo.models.ImageDetails;

@Repository
public interface ImageRepository extends JpaRepository<ImageDetails, Long> {
	
	List<ImageDetails> findAllByUserName(String userName);

}
