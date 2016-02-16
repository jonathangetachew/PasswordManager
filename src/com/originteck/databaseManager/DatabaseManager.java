package com.originteck.databaseManager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Database.FileFormat;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.TableBuilder;
import com.originteck.cryptography.BasicLevel;
import com.originteck.cryptography.HighLevel;
import com.originteck.cryptography.MediumLevel;

public class DatabaseManager {
	
	public static final String TABLE_NAME = "PasswordManager";
	
	private static Connection m_connection;
	private static PreparedStatement m_prepareStmt;

    public static Database createDatabase(String name, String password, String securityLevel) {
    	Database database = null;
    	
        try {
        	database = DatabaseBuilder.create(FileFormat.V2010, new File(name));
			createTable()
	                .addColumn(new ColumnBuilder("ID").setSQLType(Types.INTEGER).toColumn())
	                .addColumn(new ColumnBuilder("Account").setSQLType(Types.VARCHAR).toColumn())
	                .addColumn(new ColumnBuilder("Username").setSQLType(Types.VARCHAR).toColumn())
	                .addColumn(new ColumnBuilder("Password").setSQLType(Types.VARCHAR).toColumn())
	                .toTable(database);
			
			
			// Inserting information about the database into the database
			
			File databaseFile = database.getFile();
			
			if(securityLevel.contentEquals(SecurityLevel.BASIC)) {
				insertRecord(databaseFile, BasicLevel.encrypt("Current Database"), SecurityLevel.BASIC, BasicLevel.encrypt(password));
			} else if(securityLevel.contentEquals(SecurityLevel.MEDIUM)) {
				insertRecord(databaseFile, MediumLevel.encrypt("Current Database"), SecurityLevel.MEDIUM, MediumLevel.encrypt(password));
			} else if(securityLevel.contentEquals(SecurityLevel.HIGH)){
				insertRecord(databaseFile, HighLevel.encrypt("Current Database"), SecurityLevel.HIGH, HighLevel.encrypt(password));
			}
			
			JOptionPane.showMessageDialog(null, "Database created successfully");
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Unable To Create Database\n" + e.getLocalizedMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Unable To Create Database\n" + e.getLocalizedMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Unable To Create Database\n" + e.getLocalizedMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
        
        return database;
    }
    
    // Method to get data from database
    public static Vector<Vector<String>> openDatabase(File database, String password) {
    	Vector<Vector<String>> databaseContent = new Vector<Vector<String>>();

		String dbPassword = "";
		String dbSecurity = "";
    	
		// Retrieving the security level and password of the database
    	try {
    		
			m_connection = DriverManager.getConnection("jdbc:ucanaccess://" + database.toString(),"","");
			m_prepareStmt = m_connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE ID = 0");

			ResultSet resultSet = m_prepareStmt.executeQuery();
			
			while(resultSet.next()) {
				dbSecurity = resultSet.getString(3);
				dbPassword = resultSet.getString(4);
				break;
			}
						
			// Close the connection to database after use!
			if(m_connection != null)
			m_connection.close();
			m_prepareStmt.close();
			
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "Unable To Open Database\n" + e1.getLocalizedMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
    	
    	String pass = dbPassword;
    	
    	// Decrypt Database Password
    	if (dbSecurity.contentEquals(SecurityLevel.BASIC)) {
				dbPassword = BasicLevel.decrypt(pass);
    	} else if (dbSecurity.contentEquals(SecurityLevel.MEDIUM)) {
				dbPassword = MediumLevel.decrypt(pass);
    	} else if (dbSecurity.contentEquals(SecurityLevel.HIGH)) {
				dbPassword = HighLevel.decrypt(pass);
    	}
    	
    	// Check whether user provided password matches the selected database
    	if(dbPassword.contentEquals(password)) {
		
	    	try {
				
				m_connection = DriverManager.getConnection("jdbc:ucanaccess://" + database.toString(),"","");
				m_prepareStmt = m_connection.prepareStatement("SELECT * FROM " + TABLE_NAME);
	
				ResultSet rs = m_prepareStmt.executeQuery();
								
				while(rs.next()) {
					Vector<String> data = new Vector<String>();
					
					// If the row is not the 0th row, which contains database content, then decrypt and retrieve data
					if (rs.getLong(1) != 0) {
						if(dbSecurity.contentEquals(SecurityLevel.BASIC)) {
							data.add(rs.getString(1)); //ID
							data.add(BasicLevel.decrypt(rs.getString(2))); //Account
							data.add(BasicLevel.decrypt(rs.getString(3))); //Username
							data.add(BasicLevel.decrypt(rs.getString(4))); //Password
						} else if (dbSecurity.contentEquals(SecurityLevel.MEDIUM)) {
							data.add(rs.getString(1)); //ID
							data.add(MediumLevel.decrypt(rs.getString(2))); //Account
							data.add(MediumLevel.decrypt(rs.getString(3))); //Username
							data.add(MediumLevel.decrypt(rs.getString(4))); //Password
//							} 
						} else if (dbSecurity.contentEquals(SecurityLevel.HIGH)){
							data.add(rs.getString(1)); //ID
							data.add(HighLevel.decrypt(rs.getString(2))); //Account
							data.add(HighLevel.decrypt(rs.getString(3))); //Username
							data.add(HighLevel.decrypt(rs.getString(4))); //Password
						}
						
											
						databaseContent.add(data);
					}
				}
	
				// Closing opened connection
				m_prepareStmt.close();			
				if(m_connection != null) m_connection.close();
	
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Unable To Open Database\n" + e.getLocalizedMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
    	
    	} else {
    		// If database password doesn't match return null
    		return null;
    	}
    	
		return databaseContent;
    }

    // Method to create table in the database 
    private static TableBuilder createTable() {
        return new TableBuilder(TABLE_NAME);
    }
    
    // Method to update Records in a database
    public static void updateRecord(File dbName, int id, String accName, String username, String password) {
    	
    	if ( idExists( dbName, id) ) {
	    	try {
	    		m_connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbName.toString(),"","");
				String sql = "UPDATE " + TABLE_NAME + " SET Account = ?, Username = ?, Password = ? WHERE id = " + id;
				
				m_prepareStmt = m_connection.prepareStatement(sql);
				m_prepareStmt.setString(1, accName);
				m_prepareStmt.setString(2, username);
				m_prepareStmt.setString(3, password);
				m_prepareStmt.executeUpdate();
				
				// Closing opened processes
				m_prepareStmt.close();			
				if(m_connection != null) m_connection.close();
				
				JOptionPane.showMessageDialog(null, "Record Updated Successfully");
				
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Unable To Update Record\n" + e.getLocalizedMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
    	} else {
    		JOptionPane.showMessageDialog(null, "Invalid ID\n", "ERROR!", JOptionPane.ERROR_MESSAGE);
    	}
    }

    // Method to insert Records in a database
    public static void insertRecord(File dbName, String accName, String username, String password) {
    	
    	try {
    		int id = getNextAvailableID(dbName);
    		
			m_connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbName.toString(),"","");
    		
			String sql = "INSERT INTO " + TABLE_NAME + " VALUES(?, ?, ?, ?)";
						
			m_prepareStmt = m_connection.prepareStatement(sql);
			m_prepareStmt.setLong(1, id);
			m_prepareStmt.setString(2, accName);
			m_prepareStmt.setString(3, username);
			m_prepareStmt.setString(4, password);
			m_prepareStmt.execute();
			
			// Closing opened processes
			m_prepareStmt.close();			
			if(m_connection != null) m_connection.close();
						
			
			JOptionPane.showMessageDialog(null, "Record Inserted Successfully");
			
    	} catch (SQLException e) {
    		JOptionPane.showMessageDialog(null, "Unable To Insert Record\n" + e.getLocalizedMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }
    
    // Method to delete Records from a database
    public static void deleteRecord(File dbName, int id) {
    	
    	if ( idExists(dbName, id) ) {
	    	
	    	try {
				m_connection = DriverManager.getConnection("jdbc:ucanaccess://" + dbName.toString(),"","");
				String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID = " + id;
				
				m_prepareStmt = m_connection.prepareStatement(sql);
				m_prepareStmt.execute();
			
				// Closing opened processes
				m_prepareStmt.close();			
				if(m_connection != null) m_connection.close();						
				
				JOptionPane.showMessageDialog(null, "Record Deleted Successfully");
				
	    	} catch (SQLException e) {
	    		JOptionPane.showMessageDialog(null, "Invalid ID\n" + e.getLocalizedMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}	
    	} else {
    		JOptionPane.showMessageDialog(null, "Invalid ID\n", "ERROR!", JOptionPane.ERROR_MESSAGE);
    	}
    	
    }    
    
    // Method to get the next available id in the database
    private static int getNextAvailableID(File database) {
    	int nextID = 0;
    	    	
		try {
			m_connection = DriverManager.getConnection("jdbc:ucanaccess://" + database.toString(),"","");
			m_prepareStmt = m_connection.prepareStatement("SELECT ID FROM " + TABLE_NAME);

			ResultSet resultSet = m_prepareStmt.executeQuery();
			
			while(resultSet.next()) {
				nextID = resultSet.getInt(1) + 1; // Increment id counter as long as there is a record row 
			}
						
			// Close the connection to database after use!
			if(m_connection != null)
			m_connection.close();
			m_prepareStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
    	
    	return nextID;
    }
    
    // Method to check a specific id exists in a specified database or not
    private static boolean idExists(File database, int id) {
    	boolean exists = false;
    	try {
			m_connection = DriverManager.getConnection("jdbc:ucanaccess://" + database.toString(),"","");
			m_prepareStmt = m_connection.prepareStatement("SELECT * FROM " + TABLE_NAME);
			
			// Fails if id doesn't exist and throws exception
			ResultSet rs = m_prepareStmt.executeQuery();
			
			while(rs.next()) {
				if ( rs.getLong(1) == id) {
					exists = true;
					break;
				} 
			}		
		
			// Closing opened processes
			m_prepareStmt.close();			
			if(m_connection != null) m_connection.close();	
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	
    	return exists;
    }  

}