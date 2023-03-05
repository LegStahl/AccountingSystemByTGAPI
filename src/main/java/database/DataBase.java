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
	
	private static List<String> USERNAMEOFEMPLOYEE = new ArrayList<>(); 
	
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/database";
	
	private static final String PASSWORD = "root";
	
	private static final String NAME = "root";
	
	private static final String GETALLEMPLOYEES = "SELECT secondName, firstName, surname, countlessons FROM employee";
	
	private static final String GETALLCHILDREN = "SELECT secondName, firstName, surname, balance FROM child";
	
	private static final String GETCHILDRENFROMGROUP = " select secondName, firstName, balance \r\n"
			+ " from child c\r\n"
			+ "	join lesson l   on c.idchild = l.idstudent\r\n"
			+ "    where l.idgroup = (select idbunch from bunch where name = ?);";
	
	private static final String GETALLGROUPSTEACHER = "SELECT bunch.name, secondName, firstName, surname FROM employee INNER JOIN bunch ON employee.idemployee = bunch.teacher";
	
	private static final String ADDNEWEMPLOYEE = "INSERT INTO employee (secondName, firstName, surname) values (?, ?, ?)";

	private static final String GETIDEMPLOYEE = "SELECT idemployee from employee where secondName = ?";
	
	private static final String GETALLUSERNAMES = "SELECT username from employee";
	
	private static final String CHECKUSERNAME = "SELECT secondName, firstName from employee where idemployee = ?";
	
	private static final String ADDUSERNAME = "UPDATE employee set username = ? where idemployee = ?";
	
	
 
	
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
			getUserNames();
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public synchronized int addNewEmloyee(String[] usernameOfEmployee) {
		try {
			preparedStatement = connection.prepareStatement(ADDNEWEMPLOYEE);
			preparedStatement.setString(1, usernameOfEmployee[0]);
			preparedStatement.setString(2, usernameOfEmployee[1]);
			preparedStatement.setString(3, usernameOfEmployee[2]);
			preparedStatement.execute();
			preparedStatement = connection.prepareStatement(GETIDEMPLOYEE);
			preparedStatement.setString(1, usernameOfEmployee[0]);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			int check = 0;
			if(resultSet.next()) {
				check = resultSet.getInt(1);
						
			}
			return check;
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}
		
	}
	
	public synchronized String getSchedule(String usernameOfEmployee) {
		return null;
	}
	
	public synchronized boolean addHoursEmployee(String nameOfEmployee, String nameOfGroup ) {
		return true;
	}
	
//	public synchronized boolean checkEmployee(String username) {
//		return true;
//	}
	
	public synchronized String getEmployees(){
		try {
			StringBuilder listEmployees = new StringBuilder();
			preparedStatement = connection.prepareStatement(GETALLEMPLOYEES);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				listEmployees.append("ИМЯ: " + resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3)  +  " НАГРУЗКА В НЕДЕЛЮ: " + resultSet.getInt(4) + "\n" + "\n");
				
				
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
			StringBuilder listChildren = new StringBuilder();
			preparedStatement = connection.prepareStatement(GETALLCHILDREN);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				listChildren.append("ИМЯ: " + resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) +  " БАЛАНС: " + resultSet.getInt(4) + "\n" + "\n");
				
				
			}
			//preparedStatement.close();
			return listChildren.toString();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			
			return "ERROR";
		}
	}
	
	public synchronized List<String> getGroups() {
		try {
			List<String> list = new ArrayList<String>();
			StringBuilder listEmployees = new StringBuilder();
			preparedStatement = connection.prepareStatement(GETALLGROUPSTEACHER);
			System.out.println("before set");
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("before while");
			while(resultSet.next()) {
				
				listEmployees.append(resultSet.getString(1) + "_ПРЕПОДАВАТЕЛЬ: " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4));
				list.add(listEmployees.toString());
				listEmployees.delete(0, listEmployees.length());
				System.out.println("in while ");
				
			}
			//preparedStatement.close();
			return list;
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			
			return new ArrayList<String>();
		}
	}
	
	public synchronized List<String> getChildrenFromGroup(String nameOfGroup) {
		try {
			List<String> listOfChildren = new ArrayList<>();
			StringBuilder str = new StringBuilder();
			preparedStatement = connection.prepareStatement(GETCHILDRENFROMGROUP);
			preparedStatement.setString(1, nameOfGroup);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				listOfChildren.add(str.append(resultSet.getString(1) + " " + resultSet.getString(2) + " Баланс: " + resultSet.getInt(3)).toString() + "\n \n");
				str.delete(0, str.length());
			}
			return listOfChildren;
		}catch(SQLException e){
			System.out.println(e.getMessage());
			return new ArrayList<>();
		}
	}
	
	public synchronized boolean checkUserName(String userName) {
		if(USERNAMEOFEMPLOYEE.isEmpty()) {
			return false;
		}
		else {
			for(String str : USERNAMEOFEMPLOYEE) {
				if(str.equals(userName)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public synchronized boolean addToDataBase(String[] name, String username) {
		try {
			System.out.println("In dataBase 204");
			if(name.length != 0 && !name[0].equals("Ошибка")) {
				preparedStatement = connection.prepareStatement(CHECKUSERNAME);
				preparedStatement.setInt(1, Integer.parseInt(name[2]));
				ResultSet resultSet = preparedStatement.executeQuery();
				resultSet.next();
				System.out.println("In dataBase 210");
				if(resultSet.getString(1).equals(name[0]) && resultSet.getString(2).equals(name[1])) {
					USERNAMEOFEMPLOYEE.add(username);
					preparedStatement = connection.prepareStatement(ADDUSERNAME);
					preparedStatement.setString(1, username);
					preparedStatement.setInt(2, Integer.parseInt(name[2]));
					preparedStatement.executeQuery();
					System.out.println("In dataBase 216");
					return true;
				}
				else {
					System.out.println("In dataBase 220");
					return false;
				}
			}
			else {
				System.out.println("In dataBase 225");
				return false;
			}
		}catch(SQLException e){
			System.out.println(e.getMessage());
			return false;
		}
	
	}
	
	private synchronized void getUserNames() {
		try {
			List<String> list = new ArrayList<String>();
			StringBuilder listUserNames = new StringBuilder();
			preparedStatement = connection.prepareStatement(GETALLUSERNAMES);
			System.out.println("before set");
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("before while2");
			while(resultSet.next()) {
				String check = resultSet.getString(1);
				if(check != null) {
					listUserNames.append(resultSet.getString(1));
					list.add(listUserNames.toString());
					listUserNames.delete(0, listUserNames.length());
					System.out.println("in while ");
				}
				
			}
			//preparedStatement.close();
			System.out.println(list.toString());
			USERNAMEOFEMPLOYEE = list;  
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			
		}
	}
	
	
	
	public int getLastID() {
		return lastID;
	}
}
