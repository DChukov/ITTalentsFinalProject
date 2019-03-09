package com.example.demo.service;

import com.example.demo.dao.AmenityRepository;
import com.example.demo.dao.BookingRepository;
import com.example.demo.dao.CityRepository;
import com.example.demo.dao.PhotoRepository;
import com.example.demo.dao.ReviewRepository;
import com.example.demo.dao.RoomRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.dto.BookingListDTO;
import com.example.demo.dto.ReviewsForRoomDTO;
import com.example.demo.dto.RoomAddDTO;
import com.example.demo.dto.RoomBookingDTO;
import com.example.demo.dto.RoomInfoDTO;
import com.example.demo.dto.RoomListDTO;
import com.example.demo.exceptions.BookingIsOverlapingException;
import com.example.demo.exceptions.RoomNotFoundException;
import com.example.demo.model.Booking;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.model.City;
import com.example.demo.exceptions.UserException;
import com.example.demo.model.Photo;
import com.example.demo.model.Room;
import com.example.demo.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collector;
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
	
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AmenityRepository amenityRepository;
	
	@Autowired
	private BookingRepository bookingRepository;

	public List<RoomListDTO> getRoomsForHomePage() {
		return roomRepository.findAll().stream()
				.map(room -> new RoomListDTO(room.getDetails(), room.getCity().getName(), reviewService.getRoomRating(room.getId()), reviewService.getRoomTimesRated(room.getId())))
				.collect(Collectors.toList());

	}

	public RoomInfoDTO getRoomById(long id) throws RoomNotFoundException {
		Room r = roomRepository.findById(id);

		if (r == null) {
			throw new RoomNotFoundException("Room not found");
		}

		Set<String> photos = photoRepository.findAll().stream().filter(photo -> (photo.getRoom().getId().equals(id)))
				.map(photo -> new String(photo.getUrl())).collect(Collectors.toSet());

		Set<String> amenities = roomRepository.findById(id).getAmenities().stream().map(amenity -> amenity.getName())
				.collect(Collectors.toSet());

		RoomInfoDTO result = new RoomInfoDTO(r.getAddress(), r.getGuests(), r.getBedrooms(), r.getBeds(), r.getBaths(),
				r.getPrice(), r.getDetails(), photos, amenities);

		return result;
	}

	public long addRoom(RoomAddDTO room, Long userId) throws UserException, SQLException {
		if (cityRepository.findByName(room.getCity()) == null) {
			City c = new City();
			c.setName(room.getCity());
			cityRepository.save(c);
		}
		Room result = new Room(null, room.getAddress(), room.getGuests(), room.getBedrooms(), room.getBeds(),
				room.getBaths(), room.getPrice(), room.getDetails(), room.getAmenities(), 
				cityRepository.findByName(room.getCity().toLowerCase()), userId,null);

		roomRepository.saveAndFlush(result);

		return result.getId();
	}

	public void removeRoom(long roomId, long userId) throws UserException, RoomNotFoundException {
		Room r = roomRepository.findById(roomId);

		if (r == null) {
			throw new RoomNotFoundException("Room not found");
		}
		if ( roomRepository.findById(roomId).getUserId() != userId) {
			throw new UserException("User is not authorised");
		}
		roomRepository.delete(roomRepository.findById(roomId));
	}
	public Set<RoomListDTO> getUserRooms(long userId) throws UserException{

		if (userRepository.findById(userId) == null) {
			throw new UserException("User not found");
		}
		return roomRepository.findAll().stream().filter(room -> room.getUserId().equals(userId))
				.map(room -> new RoomListDTO(room.getDetails(), room.getCity().getName(), reviewService.getRoomRating(room.getId()), reviewService.getRoomTimesRated(room.getId())))
				.collect(Collectors.toSet());
	}
	
	public Set<ReviewsForRoomDTO> getUserReviews(long userId) throws UserException{
		if (userRepository.findById(userId) == null) {
			throw new UserException("User not found");
		}
		return roomRepository.findAll().stream().filter(room -> room.getUserId().equals(userId))
				.map(room -> {
					try {
						return reviewService.getAllReviewsByRoomId(room.getId());
					} catch (RoomNotFoundException e) {
						e.printStackTrace();
					}
					return null;
				}).flatMap(Set::stream)
				.collect(Collectors.toSet());
	}
	
	public List<RoomListDTO> addRoomInFavourites(long userId, long roomId) throws RoomNotFoundException, UnauthorizedException{
		Room room = roomRepository.findById(roomId);
		if ( room == null) {
			throw new RoomNotFoundException("Room not found!");
		}
		if ( roomRepository.findById(roomId).getUserId() == userId) {
			throw new UnauthorizedException("User can not put his own room in Favourites!");
		}
		
		
		userRepository.findById(userId).getFavourites().add(room);
		userRepository.saveAndFlush(userRepository.findById(userId));
		return userService.viewFavouritesRoom(userId);

		
	}
	
	public Set<RoomListDTO> getRoomsByCityName(String cityName) {
		return roomRepository.findAll().stream()
				.filter(room -> room.getCity().getName().equalsIgnoreCase(cityName))
				.map(room -> new RoomListDTO(room.getDetails(), room.getCity().getName(), 0, 0))
				.collect(Collectors.toSet());
	}

	public long makeReservation(RoomBookingDTO reservation, Long userId) throws BookingIsOverlapingException {
		if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
			LocalDate temp = reservation.getStartDate();
			reservation.setStartDate(reservation.getEndDate());
			reservation.setEndDate(temp);
		}
		
		Booking result = new Booking(null, reservation.getStartDate(), reservation.getEndDate(),
				userRepository.findById(userId), roomRepository.findById(reservation.getRoomId()));

		boolean isOverlapping = bookingRepository.findAll().stream()
				.filter(booking -> booking.getRoom().getId().equals(reservation.getRoomId()))
				.anyMatch(booking -> booking.overlap(result));
		
		if(isOverlapping) {
			throw new BookingIsOverlapingException("Overlaping dates");
		}
		
		bookingRepository.saveAndFlush(result);
		
		return result.getId();
		
	}
	
	public Set<BookingListDTO> showAllBookingsForRoom(long roomId){
		return bookingRepository.findAll().stream()
		.filter(b -> b.getRoom().getId().equals(roomId))
		.map(b -> new BookingListDTO(b.getStartDate(), b.getEndDate()))
		.collect(Collectors.toSet());
	}
}
