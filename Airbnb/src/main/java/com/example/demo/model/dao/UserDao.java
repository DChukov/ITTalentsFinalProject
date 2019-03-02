package com.example.demo.model.dao;

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
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginDTO;
import com.example.demo.model.User;

import lombok.AllArgsConstructor;

@Component
public class UserDao {
	

	private JdbcTemplate jdbcTemplate;
	
	public List<User> getAllUsers() throws SQLException{
		List<User> users = new ArrayList<User>();
		
		Connection con = jdbcTemplate.getDataSource().getConnection();
		
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users");
		
		while (rs.next()) {
			users.add(new User(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate(), rs.getString(7)));
		}
	 	return users ;

	}
	
	public void addUser(User user) throws SQLException {
		Connection con = jdbcTemplate.getDataSource().getConnection();
		Statement statement = con.createStatement();
		statement.executeUpdate("INSERT INTO users " + "VALUES (null, '" +user.getFirstName() + "', '" + user.getLastName() + "' , '" + user.getPassword() + "', '" +user.getEmail() + "' , '" + user.getBirthDate() + "' , '" + user.getPhone() + "')");
//		this.users.add(user);
	}
	
	public User getUserById(long userId) throws UserException, SQLException {
		/*	if(!this.users.stream().anyMatch(user -> user.getId() == userId)) {
			throw new UserException("No such user.");
		}
		
		return this.users.stream()
				.filter(user -> user.getId() == userId)
				.findFirst()
				.get();
*/		
		
		
		Connection con = jdbcTemplate.getDataSource().getConnection();
		
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users where user_id = "+ userId + ";");
		
		if ( rs.next()) {
			User user = new User(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate(), rs.getString(7));
			
			return user;
		}
		else {
			throw new UserException("No such user.");
		}
	}
	
	public User login(LoginDTO user) throws SQLException, UserException {
//		return this.users.stream().filter(u -> (u.getEmail().equals(user.getEmail()) && u.getPassword().equals(user.getPassword()))).findFirst()
//		.get();
	

		Connection con = jdbcTemplate.getDataSource().getConnection();
		
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users where email = \""+ user.getEmail() + "\" and password = \"" + user.getPassword()+ "\";");
		
		if ( rs.next()) {
			User u = new User(rs.getInt(1),rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate(), rs.getString(7));
			
			return u;
		}
		else {
			throw new UserException("No such user.");
		}
	}
/*	
	public void changeUserInformation() {
		Connection con = jdbcTemplate.getDataSource().getConnection();
		Statement statement = con.createStatement();
		statement.executeUpdate("UPDATE userSET column1 = value1, column2 = value2, ...\WHERE condition;");

	}
*/	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	} 
}
