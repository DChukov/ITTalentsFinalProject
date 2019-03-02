package com.example.demo.model;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class User {

	private long id;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private LocalDate birthDate;
	private String phone;
	

	@Override
	public boolean equals(Object obj) {
		return this.email.equals(((User)obj).getEmail());
	}
	
}
