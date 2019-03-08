package com.example.demo.service;

import com.example.demo.dao.CityRepository;
import com.example.demo.dao.PhotoRepository;
import com.example.demo.dao.RoomRepository;
//import com.example.demo.dao.UserDao;
import com.example.demo.dao.UserRepository;
import com.example.demo.dto.RoomAddDTO;
import com.example.demo.dto.RoomInfoDTO;
import com.example.demo.dto.RoomListDTO;
import com.example.demo.exceptions.RoomNotFoundException;
import com.example.demo.exceptions.UserException;
import com.example.demo.model.Photo;
import com.example.demo.model.Room;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private PhotoRepository photoRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
//	@Autowired
//	private UserRepository userRepository;
//	private UserDao userDao;
	
	public List<RoomListDTO> getRoomsForHomePage(){
		return roomRepository.findAll()
		.stream()
		.map(room -> new RoomListDTO(room.getDetails(), room.getCity().getName(), 1, 1)
		).collect(Collectors.toList());
	}
	
	public RoomInfoDTO getRoomById(long id) throws RoomNotFoundException {
		Room r = roomRepository.findById(id);
		
		if(r == null) {
			throw new RoomNotFoundException("Room not found");
		}
		
		Set<String> photos = photoRepository.findAll()
		.stream()
		.filter(photo -> (photo.getRoom().getId().equals(id)))
		.map(photo -> new String(photo.getUrl()))
		.collect(Collectors.toSet());
		
		RoomInfoDTO result = new RoomInfoDTO(r.getAddress(), r.getGuests(), r.getBedrooms(), r.getBeds(), r.getBaths(),
				r.getPrice(), r.getDetails(), photos );
		
		return result;
	}
	
	public long addRoom(RoomAddDTO room) throws UserException, SQLException {
		Room result = new Room(null, room.getAddress(), room.getGuests(), room.getBedrooms(), room.getBeds(),
				room.getBaths(), room.getPrice(), room.getDetails(), null, null,
				cityRepository.findById(room.getCityId()), room.getUserId());
		
		roomRepository.saveAndFlush(result);
		
		return result.getId();
	}

	public void removeRoom(long roomId) {
		roomRepository.delete(roomRepository.findById(roomId));
	}
}
