package com.example.demo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Embeddable
public class Favourites implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7677099139998080363L;

	@Column(name = "room_id")
    Long roomId;
 
    @Column(name = "user_id")
    Long cuserId;
}


