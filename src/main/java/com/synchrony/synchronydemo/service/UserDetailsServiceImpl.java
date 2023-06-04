package com.synchrony.synchronydemo.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.synchrony.synchronydemo.exceptions.RecordNotFoundException;
import com.synchrony.synchronydemo.models.User;
import com.synchrony.synchronydemo.repository.UserRepository;

/**
 * @author Pranav.Pandey
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	/**
	 * Saving person details in repository
	 * 
	 * @param user
	 * @return User Object
	 */
	public User save(User user) {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setContact(user.getContact());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return userRepository.save(newUser);
	}

	/**
	 * Check if registered user exist by contact number
	 * 
	 * @param contact
	 * @return true or false
	 */
	public boolean existsByContact(String contact) {
		return userRepository.existsByContact(contact);
	}

	public User fetchUserProfile(String userName) {
		try {
			User userDetails = userRepository.findByUsername(userName).orElseThrow(
					() -> new RecordNotFoundException("Record Not Found with given userName: " + userName));
			return userDetails;
		} catch (Exception e) {
			logger.error("Exception in userProfile", e);
		}
		return null;
	}
}
