package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Amenity;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Integer>{

}
