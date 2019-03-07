package com.example.demo.controllers;

import com.example.demo.dao.UserException;
import com.example.demo.dto.RoomAddDTO;
import com.example.demo.dto.RoomInfoDTO;
import com.example.demo.dto.RoomListDTO;
import com.example.demo.exceptions.RoomNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.demo.model.Room;
import com.example.demo.service.RoomService;;

@RestController
public class RoomController {
	
	@Autowired
	private RoomService roomService;
	
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
	public long createRoom(@RequestBody RoomAddDTO newRoom) throws UserException, SQLException {
		return roomService.addRoom(newRoom);
	}
	
	@PostMapping("/rooms/delete/{roomId}")
	public void deleteRoom(@PathVariable long roomId) {
		roomService.removeRoom(roomId);
	}
}
