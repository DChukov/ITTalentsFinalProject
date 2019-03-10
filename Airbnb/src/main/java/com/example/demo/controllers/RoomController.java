package com.example.demo.controllers;

import com.example.demo.dto.RoomAddDTO;
import com.example.demo.dto.RoomBookingDTO;
import com.example.demo.dto.RoomInfoDTO;
import com.example.demo.dao.RoomRepository;
import com.example.demo.dto.BookingListDTO;
import com.example.demo.dto.ReviewsForRoomDTO;
import com.example.demo.dto.RoomListDTO;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.BookingIsOverlapingException;
import com.example.demo.exceptions.RoomNotFoundException;
import com.example.demo.exceptions.UnauthorizedException;
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
	public List<RoomListDTO> addRoomInFavourites(@PathVariable long roomId,HttpServletRequest request,HttpServletResponse response) throws UnauthorizedException, RoomNotFoundException{
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			throw new UnauthorizedException("You must login first");
		}
		
		long id = (long) session.getAttribute("userId"); 
		return roomService.addRoomInFavourites(id, roomId);
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
	
	@GetMapping("/rooms/roomId={roomId}")
	public RoomInfoDTO getRoomById(@PathVariable long roomId) throws RoomNotFoundException {
			return roomService.getRoomById(roomId);
	}
	
	@PostMapping("/rooms/create")
	public long createRoom(@RequestBody RoomAddDTO newRoom,HttpServletRequest request,HttpServletResponse response) throws UserException, SQLException, UnauthorizedException {
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			throw new UnauthorizedException("You must login first");
		}
		
		long id = (long) session.getAttribute("userId"); 
		return roomService.addRoom(newRoom,id);
	}
	
	@PostMapping("/rooms/delete/{roomId}")
	public void deleteRoom(@PathVariable long roomId,HttpServletRequest request,HttpServletResponse response) throws UnauthorizedException, UserException, RoomNotFoundException {
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			response.setStatus(401);
			throw new UnauthorizedException("You must login first");
		}
		long id = (long) session.getAttribute("userId"); 
		roomService.removeRoom(roomId,id);
	}
	
	@GetMapping("/rooms/cityName={cityName}")
	public Set<RoomListDTO> getRoomsByCityName(@PathVariable String cityName , HttpServletResponse response){
		return roomService.getRoomsByCityName(cityName);
	}
	
	@PostMapping("/rooms/booking")
	public long makeReservation(@RequestBody RoomBookingDTO reservation,HttpServletRequest request,HttpServletResponse response) throws UnauthorizedException, BookingIsOverlapingException, RoomNotFoundException, BadRequestException {
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			throw new UnauthorizedException("You must login first");
		}
		
		long id = (long) session.getAttribute("userId"); 
		return roomService.makeReservation(reservation, id);
	}
	
	@GetMapping("/rooms/bookings={roomId}")
	public Set<BookingListDTO> getAllBookingsForRoom(@PathVariable long roomId , HttpServletResponse response) throws RoomNotFoundException{
		return roomService.showAllBookingsForRoom(roomId);
	}
	
}
