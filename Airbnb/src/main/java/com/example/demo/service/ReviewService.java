package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ReviewRepository;
import com.example.demo.dao.RoomRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.dto.ReviewsForRoomDTO;
import com.example.demo.dto.RoomListDTO;
import com.example.demo.dto.WriteReviewDTO;
import com.example.demo.model.Message;
import com.example.demo.model.Review;

@Service
public class ReviewService {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	public Set<ReviewsForRoomDTO> getAllReviewsByRoomId(int roomId) {
		
		Set<ReviewsForRoomDTO> allReviewsForRoom = new TreeSet<ReviewsForRoomDTO>(new Comparator<ReviewsForRoomDTO>() {

			@Override
			public int compare(ReviewsForRoomDTO o1, ReviewsForRoomDTO o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
			
		});
		
		allReviewsForRoom.add((ReviewsForRoomDTO) reviewRepository.findAll()
				.stream()
				.map(review -> new ReviewsForRoomDTO(review.getUser().getAllNames(), review.getDateTime(), review.getText())));
		
		return allReviewsForRoom;
	}
	
	public void addReviewForRoom(long userId,long roomId, WriteReviewDTO reviewDTO) {
		LocalDateTime time = LocalDateTime.now();
		Review review = new Review(null,time,reviewDTO.getText(),userRepository.findById(userId),roomRepository.findById(roomId), reviewDTO.getStars());
		
		
		reviewRepository.saveAndFlush(review);
		}
/*	public Set<ReviewsForRoomDTO> getAllReviewsByRoomId(int roomId) throws SQLException, UserException {
		
		Set<ReviewsForRoomDTO> allReviewsForRoom = new TreeSet<ReviewsForRoomDTO>(new Comparator<ReviewsForRoomDTO>() {

			@Override
			public int compare(ReviewsForRoomDTO o1, ReviewsForRoomDTO o2) {
				if ( o1.getDate().equals(o2.getDate())) {
					return o1.getUserName().compareTo(o2.getUserName());
				}
				return o1.getDate().compareTo(o2.getDate());
			}
			
		});
		
		Connection con = jdbcTemplate.getDataSource().getConnection();
		
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM reviews where room_id = '" + roomId + "';");
		
		while (rs.next()) {
			allReviewsForRoom.add(new ReviewsForRoomDTO( userDao.getUserById(rs.getInt(4)).getAllNames(), rs.getTimestamp(2).toLocalDateTime() , rs.getString(3) ));
		}
		
		return allReviewsForRoom;
	}
	
	public void addReviewForRoom(long userId,long roomId, Review review) throws SQLException {
		LocalDateTime time = LocalDateTime.now();
		Connection con = jdbcTemplate.getDataSource().getConnection();
		Statement statement = con.createStatement();
		statement.executeUpdate("INSERT INTO reviews VALUES (null, '" + time + "', '" + review.getText() + "' , '" + userId + "', '" + roomId + "', '" + review.getStars() + "');");
	}
	*/
}
