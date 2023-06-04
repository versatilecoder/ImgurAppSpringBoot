package com.synchrony.synchronydemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synchrony.synchronydemo.models.User;

/**
 * @author Pranav.Pandey
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String userName);

	Boolean existsByContact(String contact);
	
	Boolean existsByUsername(String userName);


}

