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

import com.example.demo.dto.ReviewsForRoomDTO;
import com.example.demo.dto.WriteReviewDTO;
import com.example.demo.exceptions.UserException;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.service.ReviewService;
import com.example.demo.service.RoomService;

@RestController
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("/rooms/{roomId}/reviews")
	public Set<ReviewsForRoomDTO> getAllReviewsByRoomId(@PathVariable int roomId,HttpServletResponse response){
		return reviewService.getAllReviewsByRoomId(roomId);
	}
	
	@PostMapping("/rooms/{roomId}/reviews")
	public Set<ReviewsForRoomDTO> addReviewForRoom(@PathVariable int roomId, @RequestBody WriteReviewDTO reviewDTO,HttpServletRequest request,HttpServletResponse response){
		
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(401);
			return null;
		}
		
		long id = (long) session.getAttribute("userId"); 
		 reviewService.addReviewForRoom(id, roomId, reviewDTO);
		 return this.getAllReviewsByRoomId(roomId, response);

	
	}
}
