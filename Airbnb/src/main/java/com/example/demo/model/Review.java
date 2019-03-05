package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.model.dao.ReviewException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Review extends TextContent{
	
	private Room room;
	private int stars;

	Review(String content, User sender, LocalDateTime localDateTime,Room room,int stars) {
		super(content, sender, localDateTime);
		this.room = room;
		this.stars = stars;
	}
	
	void setStars(int stars) {
		if ( stars > 5 || stars < 1 ) {
			throw new ReviewException();
		}
	}

}
