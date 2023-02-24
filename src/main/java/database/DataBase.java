package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

import com.mysql.cj.jdbc.Driver;

public class DataBase {
	
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/database";
	
	private static final String PASSWORD = "root";
	
	private static final String NAME = "root";
	
	private static final String GETALLEMPLOYEES = "SELECT fullname, countlessons FROM employee";
	
	private static final String GETALLCHILDREN = "SELECT fullname, balance FROM child";
	
	private static final String GETALLGROUPSTEACHER = "SELECT bunch.name, bunch.date, fullname FROM employee INNER JOIN bunch ON employee.idemployee = bunch.teacher";
	
	private static final String GETGROUPTEACHER = "SELECT fullname FROM employee where idemployee = ?";
	
 
	
	private Connection connection;
	
	private PreparedStatement preparedStatement;
	
	private int lastID = 0;
	
	public DataBase() {
		try {
			Driver driver = new Driver();
			DriverManager.registerDriver(driver);
			connection = DriverManager.getConnection(URL, NAME, PASSWORD);
			if(!connection.isClosed()) {
				System.out.println("Succeed");
			}
			//preparedStatement = connection.prepareStatement(GETALLEMPLOYEES);
			//ResultSet resultSet = preparedStatement.executeQuery();
		//	while(resultSet.next()) {
			//	 lastID = resultSet.getInt(1);
			//}
			//preparedStatement.close();
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public synchronized boolean addNewEmloyee(String usernameOfEmployee) {
		return true;
	}
	
	public synchronized String getSchedule(String usernameOfEmployee) {
		return null;
	}
	
	public synchronized boolean addHoursEmployee(String nameOfEmployee, String nameOfGroup ) {
		return true;
	}
	
	public synchronized boolean checkEmployee(String username) {
		return true;
	}
	
	public synchronized String getEmployees(){
		try {
			StringBuilder listEmployees = new StringBuilder();
			preparedStatement = connection.prepareStatement(GETALLEMPLOYEES);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				listEmployees.append("ИМЯ: " + resultSet.getString(1) +  " НАГРУЗКА В НЕДЕЛЮ: " + resultSet.getInt(2) + "\n" + "\n");
				
				
			}
			//preparedStatement.close();
			return listEmployees.toString();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			
			return "ERROR";
		}
	}
	
	public synchronized String getChildren() {
		try {
			StringBuilder listEmployees = new StringBuilder();
			preparedStatement = connection.prepareStatement(GETALLCHILDREN);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				listEmployees.append("ИМЯ: " + resultSet.getString(1) +  " БАЛАНС: " + resultSet.getInt(2) + "\n" + "\n");
				
				
			}
			//preparedStatement.close();
			return listEmployees.toString();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			
			return "ERROR";
		}
	}
	
	public synchronized String getGroups() {
		try {
			StringBuilder listEmployees = new StringBuilder();
			preparedStatement = connection.prepareStatement(GETALLGROUPSTEACHER);
			System.out.println("before set");
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("before while");
			while(resultSet.next()) {
				
				listEmployees.append("НАЗВАНИЕ: " + resultSet.getString(1) +  " ДАТА: " + resultSet.getDate(2).toString() + "ПРЕПОДАВАТЕЛЬ: " + resultSet.getString(3)  + "\n" + "\n");
				System.out.println("in while ");
				
			}
			//preparedStatement.close();
			return listEmployees.toString();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			
			return "ERROR";
		}
	}
	
	
	
	
	public int getLastID() {
		return lastID;
	}
}
