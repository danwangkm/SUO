package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.Config;
import main.Main;

public class DatabaseAPI {
	private Connection connection = null;
	private Config config = null;
	
	public DatabaseAPI(Config config) {
		this.config = config;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void openDatabase() {
		try{
			//Load the JDBC Driver
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("JDBC Driver loaded");
			Main.writeLog("DatabaseAPI: JDBC Driver loaded");
			
			//Establish a connection
			String connString = config.getDbAddress();
			
			//System.out.println("Database connected: " + connString);
			connection = DriverManager.getConnection
					(connString, config.getDbUserName(), config.getDbPassword());
			
			//System.out.println("Statement created");
			Statement statement = connection.createStatement();
			statement.execute("SET GLOBAL character_set_database = utf8;");
		}
		catch(SQLException ex) {
			System.out.println("SQL Exception when opened: ");
			Main.writeLog("DatabaseAPI: SQL Exception when opened:"+ex.getMessage());
			ex.printStackTrace();
		}
		catch(ClassNotFoundException ex) { 
		    System.out.println("Driver not found"); 
		    Main.writeLog("DatabaseAPI: Driver not found"+ex.getMessage());
		    ex.printStackTrace();
		}
	}
	
	public void closeDatabase() {
		try{
			connection.close();
			System.out.println("database disconnected");
			Main.writeLog("DatabaseAPI: database disconnected");
		}
		catch(SQLException ex) {
			System.out.println("SQL Exception when closed");
			Main.writeLog("DatabaseAPI: SQL Exception when closed"+ex.getMessage());
		}
	}
	
	/**
	 * insert new exp into server database
	 * @param word
	 * @param user
	 * @param exp
	 */
	public boolean insertExp(String word, String user, String exp) {
		
		String sql = "INSERT INTO suo.dictionary SET word='"+word+
				"', english='"+exp+"', chinese='chinese', category='category', expUser='"+user+
				"', exp_1='exp_1', exp_2='exp_2',exp_3='exp_3', exp_4='exp_4', exp_5='exp_5', confirm=0";
		
		try {
			//System.out.println(sql);
			Statement statement = connection.createStatement();
			statement.execute(sql);
			
			//System.out.println("DatabaseAPI: insert -----> " + sql);
			//Main.writeLog("DatabaseAPI: insert -----> " + sql);
			return true;
		}
		catch(SQLException ex) {
			System.out.println("SQL Exception when insert new explanation");
			Main.writeLog("DatabaseAPI: SQL Exception when insert new explanation"+ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * print all explanation record in dictionary
	 */
	public void printRecords(){
		String sql = "SELECT * FROM dictionary";
		
		try {
			
			//System.out.println("SQL: " + sql);
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			
			while(set.next()) {
				String id = set.getString("expID");
				String word = set.getString("word");
				String english = set.getString("english");
				String chinese = set.getString("chinese");
				String category = set.getString("category");
				String user = set.getString("expUser");
				String exp_1 = set.getString("exp_1");
				String exp_2 = set.getString("exp_2");
				String exp_3 = set.getString("exp_3");
				String exp_4 = set.getString("exp_4");
				String exp_5 = set.getString("exp_5");
				String confirm = set.getString("confirm");
				System.out.println(id+"\t"+word+"\t"+english+"\t"+chinese+"\t"+category+"\t"+user+
						"\t"+exp_1+"\t"+exp_2+"\t"+exp_3+"\t"+exp_4+"\t"+exp_5+"\t"+confirm);
				Main.writeLog("db data print: "+id+"\t"+word+"\t"+english+"\t"+chinese+"\t"+category+"\t"+user+
						"\t"+exp_1+"\t"+exp_2+"\t"+exp_3+"\t"+exp_4+"\t"+exp_5+"\t"+confirm);
			}
			
		}
		catch(SQLException ex) {
			System.out.println("SQL Exception when printing all record");
			Main.writeLog("DatabaseAPI: SQL Exception when printing all record"+ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * create new user
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean newUser(String username, String password, String email) {
		//the username is existed
		if(checkExistUser(username)){
			return false;
		}			
		System.out.print("new user");
		String sql = "INSERT INTO users values (null,'" + username + "','" + password + "','"+email+"');";
		
		try {
			System.out.println(sql);
			Statement statement = connection.createStatement();
			statement.execute(sql);
			
			System.out.println("database: user create success");
			Main.writeLog("DatabaseAPI: user "+username+" create success");
			return true;
		}
		catch(SQLException ex) {
			System.out.println("SQL Exception when insert new user");
			Main.writeLog("DatabaseAPI: SQL Exception when insert new user"+ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Check existing user in the database
	 * @param username
	 * @return
	 */
	public boolean checkExistUser(String username){
		String sql = "SELECT * FROM users where username='" + username + "';";
		System.out.print("check existing user");
		try {
			System.out.println("CheckExistUser: "+sql);
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()){
				System.out.println("DatabaseAPI: new user fail, "+set.getString("username") +" is existed");
				Main.writeLog("DatabaseAPI: new user fail, "+set.getString("username") +" is existed");
				return true;
			}
			
		}
		catch(SQLException ex) {
			System.out.println("SQL Exception when check existed user");
			Main.writeLog("DatabaseAPI: SQL Exception when check existed user "+ex.getMessage());
			ex.printStackTrace();
		}
		System.out.println("Not such user: " + username);
		return false;
	}
	
	/**
	 * check whether a username and password is valid
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkValid(String username, String password){
		String sql ="SELECT * FROM users where (username='" + username + "' or email='" + username + "')"+
					" and password='" + password + "';";
		System.out.print("user login");
		try {
			System.out.println(sql);
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()){
				System.out.println("where come!" + set.getString("userID"));
				Main.writeLog("DatabaseAPI: user "+username+" log in");
				return true;
			}
			
		}
		catch(SQLException ex) {
			System.out.println("SQL Exception when user login");
			Main.writeLog("DatabaseAPI: SQL Exception when user login"+ex.getMessage());
			ex.printStackTrace();
		}
		System.out.println("Not such user: " + username);
		return false;
	}
	
	/**
	 * get userID
	 * @param username
	 * @param password
	 * @return
	 */
	public String getUserID(String username, String password){
		String sql = "SELECT * FROM users where username='" + username + "' and password='" + password + "';";
		System.out.print("get user id");
		try {
			System.out.println("get userID: "+sql);
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()){
				System.out.println("where come!" + set.getString("userID"));
				return set.getString("userID");
			}
			
		}
		catch(SQLException ex) {
			System.out.println("SQL Exception when get user ID");
			ex.printStackTrace();
		}
		System.out.println("Not such user: " + username);
		return "";
	}
	
	/**
	 * show all users
	 */
	public void printUsers(){
		String sql = "SELECT * FROM users";
		
		try {
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			System.out.println("userID\tusername\tpassword");
			Main.writeLog("DatabaseAPI: userID\tusername\tpassword");
			while(set.next()) {
				String userID = set.getString("userID");
				String username = set.getString("username");
				String password = set.getString("password");
				
				System.out.println(userID+"\t"+username+"\t"+password);
				Main.writeLog("print user: "+userID+"\t"+username+"\t"+password);
			}
			
		}
		catch(SQLException ex) {
			System.out.println("SQL Exception when printing all users");
			Main.writeLog("DatabaseAPI: SQL Exception when printing all users"+ex.getMessage());
			ex.printStackTrace();
		}
	}
	
}
