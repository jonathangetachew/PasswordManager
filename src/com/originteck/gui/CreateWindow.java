package com.originteck.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.originteck.databaseManager.DatabaseManager;
import com.originteck.databaseManager.SecurityLevel;

public class CreateWindow extends SubWindow implements ActionListener{

private static final long serialVersionUID = 1L;

	private final int VERTICAL_GAP = 10;
	private final int HORIZONTAL_GAP = 10;
	
	private String m_databaseName = null;
	private String m_databasePassword = "";
	private String m_reenterPassword = "";
	private String m_databaseSecurityLevel = SecurityLevel.MEDIUM;
	
	private JLabel m_label;
	private JButton m_cancelButton;
	private JButton m_doneButton;
	private JTextField m_databaseNameField;
	private JPasswordField m_databasePasswordField;
	private JPasswordField m_databaseRePasswordField;
	private JSlider m_securityLevel;
	private JPanel m_mainContainer;
	private JPanel m_container;
	
	public CreateWindow(String name, int width, int height) {
		super(name, width, height);
		setLocation(250,150);
		setLayout(new FlowLayout());
		create();
		setVisible(true);
	}
	
	public void create() {
		// Create our main container
		m_mainContainer = new JPanel();
		m_mainContainer.setLayout(new GridLayout(5, 1));

		/*
		 * 
		 * Database Name
		 * 
		 * */
				
		m_container = new JPanel();
		m_container.setLayout(new GridLayout(1, 2));
		m_container.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											  HORIZONTAL_GAP, VERTICAL_GAP));		
		
		m_label = new JLabel("Database Name");
		m_container.add(m_label);
		
		m_databaseNameField = new JTextField(20);		
		m_container.add(m_databaseNameField);

		m_mainContainer.add(m_container);
		
		/*
		 * 
		 * Database Password
		 * 
		 * */
		
		m_container = new JPanel();
		m_container.setLayout(new GridLayout(1, 2));
		m_container.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											  HORIZONTAL_GAP, VERTICAL_GAP));		
		

		m_label = new JLabel("Database Password");
		m_container.add(m_label);
				
		m_databasePasswordField = new JPasswordField(20);				
		m_container.add(m_databasePasswordField);

		m_mainContainer.add(m_container);
		
		m_container = new JPanel();
		m_container.setLayout(new GridLayout(1, 2));
		m_container.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											  HORIZONTAL_GAP, VERTICAL_GAP));
		
		m_label = new JLabel("Re-Enter Password");
		m_container.add(m_label);
		
		m_databaseRePasswordField = new JPasswordField(20);				
		m_container.add(m_databaseRePasswordField);
		
		m_mainContainer.add(m_container);
		
		/*
		 * 
		 * Security Level 
		 * 
		 * */
		
		m_container = new JPanel();
		m_container.setLayout(new GridLayout(1, 2));
		m_container.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
															  HORIZONTAL_GAP, VERTICAL_GAP));		

		m_label = new JLabel("Security Level");
		m_container.add(m_label);
		
		m_securityLevel = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
		m_securityLevel.setMajorTickSpacing(50);
		m_securityLevel.setPaintTicks(true);
		m_securityLevel.setPaintLabels(true);
		
		//Create the label table to assign values to our slider ticks
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( 0 ), new JLabel("Basic") );
		labelTable.put( new Integer( 50 ), new JLabel("Medium") );
		labelTable.put( new Integer( 100 ), new JLabel("High") );
		m_securityLevel.setLabelTable( labelTable );
		
		// Creating and implementing an anonymous event listener
		m_securityLevel.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			        int securityLevel = (int)source.getValue();
			        if (securityLevel >= 0 && securityLevel < 25 ) {
			        	m_databaseSecurityLevel = SecurityLevel.BASIC;
			        } else if (securityLevel >= 25 && securityLevel <75) {
			        	m_databaseSecurityLevel = SecurityLevel.MEDIUM;
			        } else {
			        	m_databaseSecurityLevel = SecurityLevel.HIGH;
			        }
			    }
			}
			
		});
		
		m_container.add(m_securityLevel);
		
		m_mainContainer.add(m_container);
				
		/*
		 * 
		 * Buttons
		 * 
		 * */	
		m_container = new JPanel();
		m_container.setLayout(new GridLayout(1, 2));
		m_container.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											  HORIZONTAL_GAP, VERTICAL_GAP));
		
		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new GridLayout(1, 1));
		buttonContainer.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											      HORIZONTAL_GAP, VERTICAL_GAP));
		
		
		
		// Create cancel button
		m_cancelButton = new JButton("Cancel");
		m_cancelButton.setToolTipText("Cancel");
		m_cancelButton.addActionListener(this);
		buttonContainer.add(m_cancelButton);
		
		m_container.add(buttonContainer);
		
		buttonContainer = new JPanel();
		buttonContainer.setLayout(new GridLayout(1, 1));
		buttonContainer.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				  											      HORIZONTAL_GAP, VERTICAL_GAP));
		// Create done button
		m_doneButton = new JButton("Done");
		m_doneButton.setToolTipText("Create Database");
		m_doneButton.addActionListener(this);		
		buttonContainer.add(m_doneButton);
		
		m_container.add(buttonContainer);
		
		m_mainContainer.add(m_container);
		
		add(m_mainContainer);
		
	}
	

	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == m_doneButton) { // If done button was clicked
			
			m_databaseName = m_databaseNameField.getText();
			if( m_databasePasswordField.getPassword() != null ) 
				m_databasePassword = new String(m_databasePasswordField.getPassword());
			if( m_databaseRePasswordField.getPassword() != null )
				m_reenterPassword = new String(m_databaseRePasswordField.getPassword());
			
			if(m_databaseName == null || m_databaseName.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Database Name Field Cannot Be Empty\n", "ERROR!", JOptionPane.ERROR_MESSAGE);
			} else if (!(m_databasePassword.contentEquals(m_reenterPassword))) {
			
					JOptionPane.showMessageDialog(this, "Passwords Don't Match\n", "ERROR!", JOptionPane.ERROR_MESSAGE);
				 				
			} else { // If everything is successful
			
				m_databaseName = m_databaseName + ".accdb";
				
				File f = new File(m_databaseName);
				if(!f.exists()) { // If database isn't already created
				
					if(m_databaseSecurityLevel == SecurityLevel.BASIC) {
						DatabaseManager.createDatabase(m_databaseName, m_databasePassword, m_databaseSecurityLevel);
					} else if (m_databaseSecurityLevel == SecurityLevel.MEDIUM) {
						DatabaseManager.createDatabase(m_databaseName, m_databasePassword, m_databaseSecurityLevel);
					} else if (m_databaseSecurityLevel == SecurityLevel.HIGH){
						DatabaseManager.createDatabase(m_databaseName, m_databasePassword, m_databaseSecurityLevel);
					} else {// Empty
					}
				
				} else {
					JOptionPane.showMessageDialog(this, "Database Already Exists\n", "ERROR!", JOptionPane.ERROR_MESSAGE);
				}
				
				// Close the create database window
				close();
			
			}
		
		} else if (e.getSource() == m_cancelButton) { // If cancel button was clicked
			close();
		}
		
	}

	
	protected String getDatabaseSecurityLevel() {
		return m_databaseSecurityLevel;
	}
	

	protected String getDatabaseName() {
		return m_databaseName;
	}	

	public String getDatabasePassword() {
		return m_databasePassword;
	}
	
	private void close(){
		
		WindowEvent winClosingEvent = new WindowEvent(this,WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
		
	}



}
