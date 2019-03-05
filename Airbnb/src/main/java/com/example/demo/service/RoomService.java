package com.example.demo.service;

import com.example.demo.dao.RoomRepository;
import com.example.demo.dto.RoomListDTO;
import com.example.demo.model.Room;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;
	
	public List<RoomListDTO> getRoomsForHomePage(){
		return roomRepository.findAll()
		.stream()
		.map(room -> new RoomListDTO(room.getDetails(), room.getCity().getName(), 1, 1)
		).collect(Collectors.toList());
	}
	
//	public Room getRoomById() {
//		
//	}
}
