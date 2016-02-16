package com.originteck.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.originteck.cryptography.BasicLevel;
import com.originteck.cryptography.HighLevel;
import com.originteck.cryptography.MediumLevel;
import com.originteck.databaseManager.DatabaseManager;
import com.originteck.databaseManager.SecurityLevel;

public class AddWindow extends SubWindow implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int VERTICAL_GAP = 10;
	private final int HORIZONTAL_GAP = 10;
		
	private File m_database;
	
	private Connection m_connection;
	private PreparedStatement m_prepareStmt;
	
	private String m_tableName = DatabaseManager.TABLE_NAME;
	
	private JPanel m_mainContainer;
	private JPanel m_container;
	private JButton m_done;
	private JButton m_cancel;
	private JLabel m_label;
	private JTextField m_accountField;
	private JTextField m_usernameField;
	private JTextField m_passwordField;
	

	public AddWindow(String name, int width, int height, File database) {
		super(name, width, height);
		setLocation(350,235);
		setResizable(false);
		m_database = database;
		create();

		setVisible(true);
	}

	@Override
	protected void create() {
		m_mainContainer = new JPanel();
		m_mainContainer.setLayout(new GridLayout(4, 2));
		
		// Account
		m_container = new JPanel();
		m_container.setLayout(new GridLayout(1, 2));
		m_container.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											  HORIZONTAL_GAP, VERTICAL_GAP));
		
		m_label = new JLabel("Account");
		m_container.add(m_label);
		
		m_accountField = new JTextField();
		m_container.add(m_accountField);

		m_mainContainer.add(m_container);
		
		// Username
		m_container = new JPanel();
		m_container.setLayout(new GridLayout(1, 2));
		m_container.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											  HORIZONTAL_GAP, VERTICAL_GAP));
		
		m_label = new JLabel("Username");
		m_container.add(m_label);
		
		m_usernameField = new JTextField();
		m_container.add(m_usernameField);

		m_mainContainer.add(m_container);
		
		// Password
		m_container = new JPanel();
		m_container.setLayout(new GridLayout(1, 2));
		m_container.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											  HORIZONTAL_GAP, VERTICAL_GAP));
		
		m_label = new JLabel("Password");
		m_container.add(m_label);
		
		m_passwordField = new JTextField();
		m_container.add(m_passwordField);
		
		m_mainContainer.add(m_container);
					
		// Cancel
		m_container = new JPanel();
		m_container.setLayout(new GridLayout(1, 2));
		m_container.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											  HORIZONTAL_GAP, VERTICAL_GAP));
		
		JPanel buttonContainer = new JPanel();
		m_container.setLayout(new GridLayout(1, 1));
		m_container.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											  HORIZONTAL_GAP, VERTICAL_GAP));
		m_cancel = new JButton("Cancel");
		m_cancel.addActionListener(this);
		
		buttonContainer.add(m_cancel);
		
		m_container.add(buttonContainer);
		
		// Done
		buttonContainer = new JPanel();
		m_container.setLayout(new GridLayout(1, 1));
		m_container.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											  HORIZONTAL_GAP, VERTICAL_GAP));
		
		m_done = new JButton("Done");
		m_done.setToolTipText("Add Record");
		m_done.addActionListener(this);
		
		buttonContainer.add(m_done);
		
		m_container.add(buttonContainer);
		
		m_mainContainer.add(m_container);
		
		add(m_mainContainer, BorderLayout.CENTER);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_cancel) {
				close();
		} else if (e.getSource() == m_done) {
			String account = m_accountField.getText();
			String username = m_usernameField.getText();
			String password = m_passwordField.getText();
			
			if (account.isEmpty()) {
				JOptionPane.showMessageDialog( this, "Account Field Cannot Be Empty", "ERROR!", JOptionPane.ERROR_MESSAGE);
			} else if (username.isEmpty()) {
				JOptionPane.showMessageDialog( this, "Username Field Cannot Be Empty", "ERROR!", JOptionPane.ERROR_MESSAGE);
			} else if (password.isEmpty()) {
				JOptionPane.showMessageDialog( this, "Password Field Cannot Be Empty", "ERROR!", JOptionPane.ERROR_MESSAGE);
			} else {
				

				String dbSecurity = "";
		    	
				// Retrieving the security level of the database
		    	try {
		    		
					m_connection = DriverManager.getConnection("jdbc:ucanaccess://" + m_database.toString(),"","");
					m_prepareStmt = m_connection.prepareStatement("SELECT * FROM " + m_tableName + " WHERE ID = 0");

					ResultSet resultSet = m_prepareStmt.executeQuery();
					
					while(resultSet.next()) {
						dbSecurity = resultSet.getString(3);
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
		    	
				
				// Insert Record
				if(dbSecurity.contentEquals(SecurityLevel.BASIC)) {
						DatabaseManager.insertRecord(m_database, BasicLevel.encrypt(account), BasicLevel.encrypt(username), BasicLevel.encrypt(password));					
				} else if(dbSecurity.contentEquals(SecurityLevel.MEDIUM)) {
						DatabaseManager.insertRecord(m_database, MediumLevel.encrypt(account), MediumLevel.encrypt(username), MediumLevel.encrypt(password));
				} else if(dbSecurity.contentEquals(SecurityLevel.HIGH)){
						DatabaseManager.insertRecord(m_database, HighLevel.encrypt(account), HighLevel.encrypt(username), HighLevel.encrypt(password));
				}
				
				// Close the Add record window
				close();
			}
			
		}	
	}
	
	private void close(){
		
		WindowEvent winClosingEvent = new WindowEvent(this,WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
		
	}

}
