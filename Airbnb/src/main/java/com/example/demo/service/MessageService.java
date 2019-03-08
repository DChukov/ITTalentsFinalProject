package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MessageRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.dto.ChatListDTO;
import com.example.demo.dto.ChatWithUserDTO;
import com.example.demo.dto.SendMessageDTO;
import com.example.demo.exceptions.NoMessagesException;
import com.example.demo.model.Message;
import com.example.demo.model.User;

@Service
public class MessageService {

	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public Map<Long, TreeSet<Message>> getUserAllMessages(long userId){
		Map<Long, TreeSet<Message>> userAllMessages = new HashMap<Long, TreeSet<Message>>();
		
		for (Message message : messageRepository.findAll()) {
			Long otherUserId = null;
			if (message.getSenderId().equals(userId)) {
				otherUserId = message.getReceiverId();
			}
			if (message.getReceiverId().equals(userId)) {
				otherUserId = message.getSenderId();
			}
			
			if (otherUserId != null ) {
				if ( userAllMessages.containsKey(otherUserId)) {
					userAllMessages.get(otherUserId).add(message);
				}
				else { 
					
					userAllMessages.put(otherUserId, new TreeSet<Message>((m1,m2) -> m1.getDateTime().compareTo(m2.getDateTime())));
					userAllMessages.get(otherUserId).add(message);
				}
			}
		}
		
		return userAllMessages;

		
	}
	
	public Set<ChatListDTO> getAllMessagesForMessagePage(long userId)  {
		Map<Long, TreeSet<Message>> userAllMessages = new HashMap<Long, TreeSet<Message>>();
		userAllMessages = this.getUserAllMessages(userId);
		
		Set<ChatListDTO> messagesList = new TreeSet<ChatListDTO>((m1,m2) -> m2.getTimeOfLastMessage().compareTo(m1.getTimeOfLastMessage()));
		
		for (Entry<Long, TreeSet<Message>> entry: userAllMessages.entrySet()) {
			Message message = entry.getValue().last();
			messagesList.add(new ChatListDTO(userRepository.findById(entry.getKey()).getAllNames()
					,message.getText(),message.getDateTime()));
		}
		return messagesList;
	}
	
	public Set<ChatWithUserDTO> getMessagesWithUserById(long userId, long otherUserId) throws NoMessagesException{

		
		Set<ChatWithUserDTO> chat = new  TreeSet<ChatWithUserDTO>((m1,m2) -> m1.getTime().compareTo(m2.getTime()));
		Set<Message> messages = new TreeSet<Message>((m1,m2) -> m1.getDateTime().compareTo(m2.getDateTime()));

		messages = this.getUserAllMessages(userId).get(otherUserId);
		if ( messages == null) {
			throw new NoMessagesException("No messages with this user!");
		}
		
		for ( Message m : messages) {
			chat.add(new ChatWithUserDTO(userRepository.findById(m.getSenderId()).getAllNames(),
					m.getText(), m.getDateTime()));
		}
		return chat;
	}
	
	public void sendMessage(long senderId, long receiverId, String text) {
		
		LocalDateTime time = LocalDateTime.now();
		
		Message message = new Message(null,senderId,receiverId,text, time);
		
		
		messageRepository.saveAndFlush(message);
	}
	
}



	
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
/*
		
}


public Set<ChatListDTO> getAllMessagesForMessagePage(long userId) throws SQLException, UserException{
/*		if ( this.userAllMessages.isEmpty()) {
		this.getAllMessagesFromDB(userId);
		
	}*/
/*		
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
/*	}

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
}*/