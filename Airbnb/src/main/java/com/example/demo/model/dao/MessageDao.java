package com.example.demo.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.dto.ChatListDTO;
import com.example.demo.model.Message;
import com.example.demo.model.User;

@Component
public class MessageDao {

	private JdbcTemplate jdbcTemplate;
	
	private Map<Long, TreeSet<Message>> allMessages;
	
	@Autowired
	private UserDao userDao;
	
	public MessageDao() {
		this.allMessages = new HashMap<Long, TreeSet<Message>>();
	}
	
	public void getAllMessagesFromDB(long userId) throws SQLException, UserException{
		
		Connection con = jdbcTemplate.getDataSource().getConnection();
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM MESSAGES where sender_id = "+ userId + " OR receiver_id = "+ userId + ";");
		
		while ( rs.next()) {
			User user = null;
			//other user in the chat 
			if (rs.getInt(2) == userId) {
				user = this.userDao.getUserById(rs.getInt(3));
				
			}
			else {
				user = this.userDao.getUserById(rs.getInt(2));
			}


			if ( this.allMessages.containsKey(user.getId())) {
				this.allMessages.get(user.getId()).add(new Message(rs.getString(4),
						userDao.getUserById(rs.getLong(2)).getFirstName() + " " + userDao.getUserById(rs.getLong(2)).getLastName(),
						rs.getDate(5).toLocalDate()));
			}
			else { 
				
				this.allMessages.put(user.getId(), new TreeSet<Message>((m1,m2) -> m1.getTime().compareTo(m2.getTime())));
				this.allMessages.get(user.getId()).add(new Message(rs.getString(4),
						userDao.getUserById(rs.getLong(2)).getFirstName() + " " + userDao.getUserById(rs.getLong(2)).getLastName(),rs.getDate(5).toLocalDate()));
			}
		}
	}
	
	public Set<ChatListDTO> getAllMessages(long userId) throws SQLException, UserException{
		if ( this.allMessages.isEmpty()) {
			this.getAllMessagesFromDB(userId);
			
		}
		Set<ChatListDTO> messagesList = new HashSet<ChatListDTO>();
		
		for (Entry<Long, TreeSet<Message>> entry: this.allMessages.entrySet()) {
			Message message = entry.getValue().last();
			messagesList.add(new ChatListDTO(userDao.getUserById(entry.getKey()).getFirstName() + " " + userDao.getUserById(entry.getKey()).getLastName()
					,message.getContent(),message.getTime()));
		}
		return messagesList;
	}
	
	public Set<Message> getMessagesWithUserById(long userId, long otherUserId) throws SQLException, UserException{
		if ( this.allMessages.isEmpty()) {
			this.getAllMessagesFromDB(userId);;
		}
		
		return this.allMessages.get(otherUserId);
		
/*		
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
			
			
			messagesList.add(chats);
		}
		return messagesList;
		
		*/
	}
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	} 
	
	
}
