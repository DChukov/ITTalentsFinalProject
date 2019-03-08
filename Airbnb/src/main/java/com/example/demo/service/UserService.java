package com.example.demo.service;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserRepository;
import com.example.demo.dto.LoginDTO;
import com.example.demo.exceptions.SignUpException;
import com.example.demo.exceptions.UserException;
import com.example.demo.model.Room;
import com.example.demo.model.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public Set<User> getAllUsers() throws SQLException{
		return userRepository.findAll().stream().collect(Collectors.toSet());

	}
	
	public long signup(User user) throws SignUpException {
		
		if ( !this.isPasswordValid(user.getPassword()) || !this.isValidEmailAddress(user.getEmail())) {
			throw new SignUpException("Invalid email or password");
		}
		
		if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
			throw new SignUpException("Email is already used");
		}
		
		User result = new User(null, user.getFirstName(), user.getLastName(), user.getPassword(), user.getEmail(),
				user.getBirthDate(), user.getPhone());
		
		userRepository.saveAndFlush(result);
		
		return result.getId();
	}
	
	public User getUserById(long userId) throws UserException {
		User user = userRepository.findById(userId);
		
		if ( user == null) {
			throw new UserException("User not found");
		}
		
		return user;
	}
	
	public User login(LoginDTO loginDTO) throws UserException {
		User user = null;
		try{
			 user = userRepository.findAll().stream().filter(u -> (u.getEmail().equals(loginDTO.getEmail()) && u.getPassword().equals(loginDTO.getPassword()))).findFirst().get();
		}
		catch(NoSuchElementException e) {
			throw new UserException("User not found");
		}
	
		return user;
	}
	
	public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
	}
	
	// digit, lowercase, uppercase, at least 8 characters
	public boolean isPasswordValid(String password) {
	    String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
	    return password.matches(pattern);
	  }
		
}