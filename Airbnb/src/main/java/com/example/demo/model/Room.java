package com.example.demo.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "rooms")
@Getter
@Setter
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String address;
	private int guests;
	private int bedrooms;
	private int beds;
	private int baths;
	private int price;
	private String details;
	
//	@ManyToMany
//	private Set<Amenity> amenities;
//	
//	@OneToMany
//	private Set<Booking> bookings;
//	
//	@OneToMany
//	private Set<Photo> photos;
//	
	@ManyToOne
	private City city;
	
}
