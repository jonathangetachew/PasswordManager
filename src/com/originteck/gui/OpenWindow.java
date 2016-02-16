package com.originteck.gui;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private File m_database = null;
	private String m_databasePassword = null;
	
	public OpenWindow() {
		// Setting the window title bar image
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/logo.png")));
		m_database = browseFile();
		
		// If user selected the right file type accept password
		if(m_database != null) {
			m_databasePassword = acceptPassword();			
		} 
		
	}
	
	public OpenWindow(File database, String password) {
		setDatabase(database);
		setPassword(password);
	}
		
	// Method to accept user password 
	private String acceptPassword() {
		
		String returnValue = null;
		
		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel("Enter Password     ");
		passwordPanel.add(label);
		
		JPasswordField password = new JPasswordField(15);
		passwordPanel.add(password);
		
		int rv = JOptionPane.showConfirmDialog(this, passwordPanel, "Authentication Required", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (rv == JOptionPane.OK_OPTION) {
			
			returnValue = new String(password.getPassword());
			
		}
		
		
		return returnValue;
	}
	

	// Getters
	protected String getPassword() {
		return m_databasePassword;
	}
	
	protected File getDatabase() {
		return m_database;
	}	
	
	// Setters
	protected void setPassword(String password) {
		m_databasePassword = password;
	}
	
	protected void setDatabase(File database) {
		m_database = database;
	}

	// Open File Dialog
	private File browseFile() {
		JFileChooser  fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("Encrypted Database File", "accdb"));
		fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
			return  fc.getSelectedFile();
        }else{
        	return null;
        }
	}	
	
}
