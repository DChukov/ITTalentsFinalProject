package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.dao.ReviewException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Review {
	
	private String text;
	private int stars;

	Review(String text, int stars) throws ReviewException {
		this.text = text;
		this.setStars(stars);
	}
	
	void setStars(int stars) throws ReviewException{
		if ( stars > 5 || stars < 1 ) {
			throw new ReviewException("Wrong number of stars for the review");
		}
		this.stars = stars;
	}

}
