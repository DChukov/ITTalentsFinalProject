package com.example.demo.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EditProfileDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.UserProfileDTO;
import com.example.demo.exceptions.SignUpException;
import com.example.demo.exceptions.UserException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.dao.UserRepository;

@RestController
public class UserController {
	
	
	@Autowired
	private UserService userService;
	
	
	
	
	@PostMapping("/users")
	public long signUp(@RequestBody User user,HttpServletResponse response,HttpServletRequest request){
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(404);
			return 0;
		}
		
		try {
			return userService.signup(user);
		} catch (SignUpException e) {
			response.setStatus(400);
			e.printStackTrace();
		}
		return 0;
	}
	
	@GetMapping("/users")
	public Set<User> getAllUsers(HttpServletResponse response){
		
		try {
			return userService.getAllUsers();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/users/{userId}")
	public UserProfileDTO getUserDetails(@PathVariable long userId,HttpServletResponse response) {
		try {
			return userService.getUserById(userId);
		} catch (UserException e) {
			response.setStatus(404);
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/login")
	public void login(@RequestBody LoginDTO user, HttpServletRequest request,HttpServletResponse response) {
		User u = null;
		try {
			u = userService.login(user);
		} catch (UserException e) {
			response.setStatus(404);
			e.printStackTrace();
			return;
		}
		HttpSession session = request.getSession();
		session.setAttribute("userId", u.getId());
	}
	
	@PostMapping("/logout")
	public void logout(HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(404);
			return;
		}
		session.invalidate();
	}
	
	@PutMapping("/changeInformation")
	public UserProfileDTO changeInformation(@RequestBody EditProfileDTO editProfileDTO,HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(401);
			return null;
		}
		
		long id = (long) session.getAttribute("userId"); 
		try {
			return userService.changeInformation(id, editProfileDTO);
		} catch (UserException e) {
			response.setStatus(404);
			e.printStackTrace();
		}
		return null;
	}
	@GetMapping("/profile")
	public UserProfileDTO getUserProfile(HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(401);
			return null;
		}
		
		long id = (long) session.getAttribute("userId"); 
		try {
			return userService.getUserById(id);
		} catch (UserException e) {
			response.setStatus(404);
			e.printStackTrace();
		}
		return null;
	
	}
}
