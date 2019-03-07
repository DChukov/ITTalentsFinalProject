package com.example.demo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.dto.ReviewsForRoomDTO;
import com.example.demo.model.Review;
import com.example.demo.model.User;

@Component
public class ReviewDao {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UserDao userDao;
	
	
	public Set<ReviewsForRoomDTO> getAllReviewsByRoomId(int roomId) throws SQLException, UserException {
		
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
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	} 
	
}
