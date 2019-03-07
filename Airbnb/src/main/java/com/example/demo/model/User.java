package com.example.demo.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class User {

	private Long id;
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
