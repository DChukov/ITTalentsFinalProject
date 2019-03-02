package com.example.demo.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.dto.ChatListDTO;
import com.example.demo.model.Message;
import com.example.demo.model.User;

@Component
public class MessageDao {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UserDao userDao;
	
	public Set<ChatListDTO> getAllMessages(long userId) throws SQLException, UserException{
		Set<ChatListDTO> messagesList = new HashSet<ChatListDTO>();
		
		Connection con = jdbcTemplate.getDataSource().getConnection();
		
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM MESSAGE where sender_id = "+ userId + " OR receiver_id = "+ userId + ";");
		
		while ( rs.next()) {
			User user = null;
			if (rs.getInt(2) == userId) {
				user = this.userDao.getUserById(rs.getInt(3));
			}
			else {
				user = this.userDao.getUserById(rs.getInt(2));
			}
			ChatListDTO chats = new ChatListDTO(user.getFirstName() + " " + user.getLastName());
			
			messagesList.add(chats);
		}
		return messagesList;
	}
	
	public Set<ChatListDTO> getMessagesWithUserById(long userId, long otherUserId) throws SQLException, UserException{
		Set<ChatListDTO> messagesList = new HashSet<ChatListDTO>();
		
		Connection con = jdbcTemplate.getDataSource().getConnection();
		
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM MESSAGE where sender_id = "+ userId + " and receiver_id = "+ otherUserId +
				"OR receiver_id = "+ userId + ";");
		
		while ( rs.next()) {
			User user = null;
			if (rs.getInt(2) == userId) {
				user = this.userDao.getUserById(rs.getInt(3));
			}
			else {
				user = this.userDao.getUserById(rs.getInt(2));
			}
			ChatListDTO chats = new ChatListDTO(user.getFirstName() + " " + user.getLastName());
			
			messagesList.add(chats);
		}
		return messagesList;
	}
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	} 
	
	
}
