package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public abstract class TextContent {

	private String author;
	private String content;
	private LocalDate time;
	
	TextContent (String content, String author,LocalDate time) {
		this.setContent(content);
		this.setAuthor(author);
		this.time = time;
	}
}
