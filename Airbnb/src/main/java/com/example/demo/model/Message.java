package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Message extends TextContent{

//	private User receiver; 
	
	public Message(String content, String authorName,LocalDate time){
		super(content,authorName,time);
//		this.receiver = receiver;
	}
}
