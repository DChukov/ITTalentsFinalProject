package com.example.demo.controllers;

import com.example.demo.dto.RoomAddDTO;
import com.example.demo.dto.RoomInfoDTO;
import com.example.demo.dao.RoomRepository;
import com.example.demo.dto.ReviewsForRoomDTO;
import com.example.demo.dto.RoomListDTO;
import com.example.demo.exceptions.RoomNotFoundException;
import com.example.demo.exceptions.UserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.demo.model.Room;
import com.example.demo.service.RoomService;;

@RestController
public class RoomController {
	
	@Autowired
	private RoomService roomService;
	
	@GetMapping("/rooms/{roomId}/addInFavourites")
	public List<RoomListDTO> addRoomInFavourites(@PathVariable long roomId,HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(401);
			return null;
		}
		
		long id = (long) session.getAttribute("userId"); 
		try {
			return roomService.addRoomInFavourites(id, roomId);
		} catch (UserException e) {
			response.setStatus(400);
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/rooms")
	public List<RoomListDTO> getAllRooms(HttpServletResponse response) throws RoomNotFoundException{
		List<RoomListDTO> result = roomService.getRoomsForHomePage();
		if (result.isEmpty()) {
			throw new RoomNotFoundException("No rooms to show");
		} else {
			return result;
		}
	}
	
	@GetMapping("/rooms/{roomId}")
	public RoomInfoDTO getRoomById(@PathVariable long roomId) throws RoomNotFoundException {
			return roomService.getRoomById(roomId);
	}
	
	@PostMapping("/rooms/create")
	public long createRoom(@RequestBody RoomAddDTO newRoom,HttpServletRequest request,HttpServletResponse response) throws UserException, SQLException {
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(401);
			return 0;
		}
		
		long id = (long) session.getAttribute("userId"); 
		return roomService.addRoom(newRoom,id);
	}
	
	@PostMapping("/rooms/delete/{roomId}")
	public void deleteRoom(@PathVariable long roomId,HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(401);
			return;
		}
		long id = (long) session.getAttribute("userId"); 
		try {
			roomService.removeRoom(roomId,id);
		} catch (UserException e) {
			response.setStatus(401);
			e.printStackTrace();
		}
	}
	
	
	
}
