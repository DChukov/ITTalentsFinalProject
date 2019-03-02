package com.example.demo.controllers;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginDTO;
import com.example.demo.model.User;
import com.example.demo.model.dao.UserDao;
import com.example.demo.model.dao.UserException;

@RestController
public class UsersController {
	
	@Autowired
	private UserDao userDao;
	
	@PostMapping("/login")
	public void login(@RequestBody LoginDTO user, HttpServletRequest request) {
		User u = userDao.login(user);
		HttpSession session = request.getSession();
		session.setAttribute("userId", u.getId());
	}
	
	@PostMapping("/logout")
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
	}

	@GetMapping("/users")
	public List<User> getAllUsers(HttpServletResponse response){
		try {
			return this.userDao.getAllUsers();
		} catch (SQLException e) {
			response.setStatus(404);
			return null;
		}
	}
	
	@GetMapping("/profile")
	public User getUserProfile(HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(401);
			return null;
		}
		
		long id = (long) session.getAttribute("userId"); 
		try {
			return userDao.getUserById(id);
		} catch (UserException e) {
			response.setStatus(401);
			return null;
		}
		catch (SQLException e) {
			response.setStatus(401);
			return null;
		}
		
	}
	
	@PostMapping("/users")
	public void addNewUser(@RequestBody User user){
		try {
			this.userDao.addUser(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	@GetMapping("/users/{userId}")
	public User getUserDetails(@PathVariable long userId,HttpServletResponse response) {
		try {
			return this.userDao.getUserById(userId);
		} catch (UserException | SQLException e) {
			response.setStatus(404);
			return null;
		}
	}
}
