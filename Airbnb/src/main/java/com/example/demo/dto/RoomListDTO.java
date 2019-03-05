package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomListDTO {
	private String details;
	private String city;
	private int rating;
	private int timesRated;
}
