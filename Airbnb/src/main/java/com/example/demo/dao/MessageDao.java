package com.example.demo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.dto.ChatListDTO;
import com.example.demo.dto.ChatWithUserDTO;
import com.example.demo.model.Message;
import com.example.demo.model.User;

@Component
public class MessageDao {

	private JdbcTemplate jdbcTemplate;
	
	private Set<Message> allMessagesFromDB;
	
	
	@Autowired
	private UserDao userDao;
	
	public MessageDao() {
		this.allMessagesFromDB = new HashSet<Message>();

	}
	
	public void getAllMessagesFromDB() throws SQLException, UserException {
		Connection con = jdbcTemplate.getDataSource().getConnection();
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM MESSAGES;");
		
		while ( rs.next()) {
			this.allMessagesFromDB.add(new Message(rs.getString(4),userDao.getUserById(rs.getLong(2)),rs.getTimestamp(5).toLocalDateTime(),userDao.getUserById(rs.getLong(3))));
		}
	}
	
	public Map<Long, TreeSet<Message>> getUserAllMessages(long userId) throws SQLException, UserException{
		
		if ( this.allMessagesFromDB.isEmpty()) {
			this.getAllMessagesFromDB();
		}
		 Map<Long, TreeSet<Message>> userAllMessages = new HashMap<Long, TreeSet<Message>>();
		for ( Message message : this.allMessagesFromDB) {
			User otherUser = null;
			if (message.getAuthor().getId().equals(userId)) {
				otherUser = message.getReceiver();
			}
			if (message.getReceiver().getId().equals(userId)) {
				otherUser = message.getAuthor();
			}
			
			if (otherUser != null ) {
				if ( userAllMessages.containsKey(otherUser.getId())) {
					userAllMessages.get(otherUser.getId()).add(message);
				}
				else { 
					
					userAllMessages.put(otherUser.getId(), new TreeSet<Message>((m1,m2) -> m1.getTime().compareTo(m2.getTime())));
					userAllMessages.get(otherUser.getId()).add(message);
				}
			}
		}
		return userAllMessages;
		
/*		Connection con = jdbcTemplate.getDataSource().getConnection();
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
*/

			
	}
	
	
	public Set<ChatListDTO> getAllMessagesForMessagePage(long userId) throws SQLException, UserException{
/*		if ( this.userAllMessages.isEmpty()) {
			this.getAllMessagesFromDB(userId);
			
		}*/
		
		Map<Long, TreeSet<Message>> userAllMessages = new HashMap<Long, TreeSet<Message>>();
		userAllMessages = this.getUserAllMessages(userId);
		
		Set<ChatListDTO> messagesList = new TreeSet<ChatListDTO>((m1,m2) -> m2.getTimeOfLastMessage().compareTo(m1.getTimeOfLastMessage()));
		
		for (Entry<Long, TreeSet<Message>> entry: userAllMessages.entrySet()) {
			Message message = entry.getValue().last();
			messagesList.add(new ChatListDTO(userDao.getUserById(entry.getKey()).getFirstName() + " " + userDao.getUserById(entry.getKey()).getLastName()
					,message.getContent(),message.getTime()));
		}
		return messagesList;
	}
	
	public Set<ChatWithUserDTO> getMessagesWithUserById(long userId, long otherUserId) throws SQLException, UserException{
		if ( this.allMessagesFromDB.isEmpty()) {
			this.getAllMessagesFromDB();
		}
		
		Set<ChatWithUserDTO> chat = new  TreeSet<ChatWithUserDTO>((m1,m2) -> m1.getTime().compareTo(m2.getTime()));
		Set<Message> messages = this.getUserAllMessages(userId).get(otherUserId);
		for ( Message m : messages) {
			chat.add(new ChatWithUserDTO(m.getAuthor().getFirstName() + " " + m.getAuthor().getLastName(),
					m.getContent(), m.getTime()));
		}
		return chat;
		
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
	
	public void sendMessage(long senderId, long receiverId, String text) throws SQLException, UserException {
		if ( this.allMessagesFromDB.isEmpty()) {
			this.getAllMessagesFromDB();
		}
		
		LocalDateTime time = LocalDateTime.now();
		
		Message message = new Message(text, userDao.getUserById(senderId), time, userDao.getUserById(receiverId));
		this.allMessagesFromDB.add(message);
		
		
		Connection con = jdbcTemplate.getDataSource().getConnection();
		Statement statement = con.createStatement();
		statement.executeUpdate("INSERT INTO messages " + "VALUES (null, '" + senderId + "', '" + receiverId + "' , '" + text + "', '" +time + "');");
	}
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	} 
	
	
}
