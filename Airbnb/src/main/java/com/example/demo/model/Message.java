package com.example.demo.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Message extends TextContent{

	private User receiver; 
	
	Message(String content, User author,User receiver){
		super(content,author);
		this.receiver = receiver;
	}
}
