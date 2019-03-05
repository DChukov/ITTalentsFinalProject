package com.example.demo.controllers;

import java.sql.SQLException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.ReviewDao;
import com.example.demo.dao.UserException;
import com.example.demo.dto.ReviewsForRoomDTO;
import com.example.demo.model.Review;
import com.example.demo.model.User;

@RestController
public class ReviewController {

	@Autowired
	private ReviewDao reviewDao;
	
	@GetMapping("/rooms/{roomId}/reviews")
	public Set<ReviewsForRoomDTO> getAllReviewsByRoomId(@PathVariable int roomId,HttpServletResponse response){
		try {
			return reviewDao.getAllReviewsByRoomId(roomId);
		} catch (SQLException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@PostMapping("/rooms/{roomId}/reviews")
	public Set<ReviewsForRoomDTO> addReviewForRoom(@PathVariable int roomId, @RequestBody Review review,HttpServletRequest request,HttpServletResponse response){
		
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(401);
			return null;
		}
		
		long id = (long) session.getAttribute("userId"); 
		try {
			reviewDao.addReviewForRoom(id, roomId,review);
		} catch (SQLException e) {
			response.setStatus(404);
			return null;
		}
		return this.getAllReviewsByRoomId(roomId, response);
		
		
	
	}
}
