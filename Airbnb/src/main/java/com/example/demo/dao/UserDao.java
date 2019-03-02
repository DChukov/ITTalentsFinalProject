package com.example.demo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.demo.dto.LoginDTO;
import com.example.demo.model.User;

@Component
public class UserDao {
	
	private List<User> users;
	
	private JdbcTemplate jdbcTemplate;
	
	
	public UserDao() {
		this.users = new ArrayList<User>();
	}
	
	public void fromDatabaseToThis() {
		
	}

	public List<User> getAllUsers() throws SQLException{
/*		
		Connection con = jdbcTemplate.getDataSource().getConnection();
		
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users");
		
		while (rs.next()) {
			this.users.add(new User(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate(), rs.getString(7)));
		}
		*/
	 	return this.users ;
	}
	
	public void addUser(User user) throws SQLException {
		Connection con = jdbcTemplate.getDataSource().getConnection();
		Statement statement = con.createStatement();
		statement.executeUpdate("INSERT INTO users " + "VALUES (null, '" +user.getFirstName() + "', '" + user.getLastName() + "' , '" + user.getPassword() + "', '" +user.getEmail() + "' , '" + user.getBirthDate() + "' , '" + user.getPhone() + "')");
		this.users.add(user);
	}
	
	public User getUserById(long userId) throws UserException {
		if(!this.users.stream().anyMatch(user -> user.getId() == userId)) {
			throw new UserException("No such user.");
		}
		
		return this.users.stream()
				.filter(user -> user.getId() == userId)
				.findFirst()
				.get();
	}
	
	public User login(LoginDTO user) {
		return this.users.stream().filter(u -> (u.getEmail().equals(user.getEmail()) && u.getPassword().equals(user.getPassword()))).findFirst()
		.get();
	

	}
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	} 
}
