package com.example.demo.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ChatListDTO;
import com.example.demo.model.dao.MessageDao;
import com.example.demo.model.dao.UserDao;
import com.example.demo.model.dao.UserException;

@RestController
public class MessageController {

	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private UserDao userDao;
	
	@GetMapping("/messages")
	public Set<ChatListDTO> getAllMessages(HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(401);
			return null;
		}
		
		long id = (long) session.getAttribute("userId"); 
		Set<ChatListDTO> messages = new HashSet<ChatListDTO>();
		try {
			messages = messageDao.getAllMessages(id);
			return messages;
		} catch (UserException e) {
			response.setStatus(401);
			return null;
		}
		catch (SQLException e) {
			return messages;
		}
	}
	
	@GetMapping("/messages/{userId}")
	public Set<ChatListDTO> getMessagesWithUserById(HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(401);
			return null;
		}
		
		long id = (long) session.getAttribute("userId"); 
		Set<ChatListDTO> messages = new HashSet<ChatListDTO>();
		try {
			messages = messageDao.getAllMessages(id);
			return messages;
		} catch (UserException e) {
			response.setStatus(401);
			return null;
		}
		catch (SQLException e) {
			return messages;
		}
	}
}
