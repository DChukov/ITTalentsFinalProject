package com.example.demo.controllers;

import com.example.demo.dto.RoomListDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.demo.model.Room;
import com.example.demo.service.RoomService;;

@RestController
public class RoomController {
	
	@Autowired
	private RoomService roomService;
	
	@GetMapping("/rooms")
	public List<RoomListDTO> getAllRooms(HttpServletResponse response){
		List<RoomListDTO> result = roomService.getRoomsForHomePage();
		if (result.isEmpty()) {
			response.setStatus(404);
			return null;
		} else {
			return result;
		}
		
		
	}
}
