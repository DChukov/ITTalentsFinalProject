package com.example.demo.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="amenities")
public class Amenity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long amenity_id;
	
	private String name;
	
//	@ManyToMany
//	private Set<Room> rooms;
}
