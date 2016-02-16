package com.originteck.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import com.originteck.databaseManager.DatabaseManager;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Variables for out menu buttons
	private JPanel m_buttonMenu;
	private JPanel m_dbPanel;
	private JButton m_button;
	private JButton m_createButton;
	private JButton m_openButton;
	
	// Variables for the database viewer
	private JDesktopPane m_databaseWindow;
	private JInternalFrame m_databaseDisplayFrame;
	
	private File m_currentDatabase;
	
	private OpenWindow m_currentDatabaseWindow = null;
	
	// Variables for out menu bar
	private JMenuBar m_menuBar;
	private JMenu m_menuBarItems;
	private JMenuItem m_menuItem;
	private JMenuItem m_createDBMenuItem;
	private JMenuItem m_openDBMenuItem;
	

	public MainWindow(String name, int width, int height) {
		super(name);
		setLocation(200, 100);
		setLayout(new BorderLayout());
		setSize(width,height);
		// Setting the window title bar image
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/logo.png")));
		create();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	protected void create() {
		createMenu();
		createButtons();
		createDBWindow();
	}
	
	private void createMenu() {
		
		//Create the menu bar.
		m_menuBar = new JMenuBar();

		// Build the File menu.
		m_menuBarItems = new JMenu("File");
		m_menuBarItems.setMnemonic(KeyEvent.VK_F);
		m_menuBar.add(m_menuBarItems);

		// New MenuItem
		m_createDBMenuItem = new JMenuItem("New", new ImageIcon(getClass().getResource("images/newFile.png")));
		m_createDBMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		m_createDBMenuItem.setMnemonic(KeyEvent.VK_N);
		m_createDBMenuItem.addActionListener(new ButtonListener("createDatabase"));
		m_menuBarItems.add(m_createDBMenuItem);

		// Open MeunItem
		m_openDBMenuItem = new JMenuItem("Open", new ImageIcon(getClass().getResource("images/openFile.png")));
		m_openDBMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		m_openDBMenuItem.setMnemonic(KeyEvent.VK_O);
		m_openDBMenuItem.addActionListener(new ButtonListener("openDatabase"));
		m_menuBarItems.add(m_openDBMenuItem);

		// Add Horizontal Separator
		m_menuBarItems.addSeparator();
		
		// Exit MenuItem
		m_menuItem = new JMenuItem("Exit", new ImageIcon(getClass().getResource("images/cancel.GIF")));
		m_menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		m_menuItem.setMnemonic(KeyEvent.VK_X);
		m_menuItem.addActionListener(new ButtonListener("exit"));
		m_menuBarItems.add(m_menuItem);
		
		// Build the Help menu
		m_menuBarItems = new JMenu("Help");
		m_menuBarItems.setMnemonic(KeyEvent.VK_H);
		m_menuBar.add(m_menuBarItems);
		
		m_menuItem = new JMenuItem("About", new ImageIcon(getClass().getResource("images/info.png")));
		m_menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		m_menuItem.setMnemonic(KeyEvent.VK_A);
		m_menuItem.addActionListener(new ButtonListener("about"));;
		m_menuBarItems.add(m_menuItem);

		// Set our menu as the systems default menu for our window
		setJMenuBar(m_menuBar);
	}

	private void createButtons() {
		m_buttonMenu = new JPanel(new FlowLayout(FlowLayout.LEFT));
		m_buttonMenu.setBackground(Color.WHITE);
		
		m_createButton = new JButton("Create", new ImageIcon(getClass().getResource("images/newFile.png")));
		m_createButton.setBackground(Color.WHITE);
		m_createButton.setBorderPainted(false);
		m_createButton.setFocusable(false);
		m_createButton.setToolTipText("Create Database");
		m_createButton.addActionListener((ActionListener) new ButtonListener("createDatabase"));
		m_createButton.setVisible(true);
		m_buttonMenu.add(m_createButton);
				
		m_openButton = new JButton("Open", new ImageIcon(getClass().getResource("images/openFile.png")));
		m_openButton.setBackground(Color.WHITE);
		m_openButton.setBorderPainted(false);
		m_openButton.setFocusable(false);
		m_openButton.setToolTipText("Open Database");
		m_openButton.addActionListener((ActionListener) new ButtonListener("openDatabase"));
		m_openButton.setVisible(true);
		m_buttonMenu.add(m_openButton);
		
		m_button = new JButton("Exit", new ImageIcon(getClass().getResource("images/close.GIF")));
		m_button.setBackground(Color.WHITE);
		m_button.setBorderPainted(false);
		m_button.setFocusable(false);
		m_button.setToolTipText("Terminate");
		m_button.addActionListener((ActionListener) new ButtonListener("exit"));
		m_button.setVisible(true);
		m_buttonMenu.add(m_button);
		
		add(m_buttonMenu, BorderLayout.NORTH);
	}
	
	private void createDBWindow() {
		
		m_dbPanel = new JPanel();
		m_dbPanel.setLayout(new GridLayout());
				
		m_databaseWindow=new JDesktopPane();
    	m_databaseWindow.setBackground(new Color(0x909090));
    	m_dbPanel.add(m_databaseWindow);
    	

	    add(m_dbPanel, BorderLayout.CENTER);
	    
	}
	
	private void createDBInternalFrame(String name) {

		/*
		 * 
		 * Database Display Frame 
		 * 
		 */
		m_databaseDisplayFrame = new JInternalFrame(name , false, false, false, false);	
		m_databaseDisplayFrame.setBounds(0, 0 , 400, 300);
		m_databaseDisplayFrame.setBorder(null);
		m_databaseDisplayFrame.setVisible(false);
		m_databaseDisplayFrame.setLayout(new BorderLayout());
	    
	    m_databaseWindow.add(m_databaseDisplayFrame);	

    	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    
	    m_button = new JButton("Add");
	    m_button.setToolTipText("Add New Record");
		m_button.setMnemonic(KeyEvent.VK_A);
	    m_button.addActionListener(new ButtonListener("addRecord"));
	    buttonPanel.add(m_button);
	    
	    m_button = new JButton("Delete");
	    m_button.setToolTipText("Delete A Record");
		m_button.setMnemonic(KeyEvent.VK_D);
	    m_button.addActionListener(new ButtonListener("deleteRecord"));
	    buttonPanel.add(m_button);
	    
	    m_button = new JButton("Update");
	    m_button.setToolTipText("Update A Record");
		m_button.setMnemonic(KeyEvent.VK_U);
	    m_button.addActionListener(new ButtonListener("updateRecord"));
	    buttonPanel.add(m_button);
	    
	    m_button = new JButton("Refresh");
	    m_button.setToolTipText("Refresh Database Content");
		m_button.setMnemonic(KeyEvent.VK_R);
	    m_button.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e) {
	    		refreshDatabase();
	    	}
	    });
	    buttonPanel.add(m_button);
	    
	    m_button = new JButton("Close");
	    m_button.setToolTipText("Close Database");
		m_button.setMnemonic(KeyEvent.VK_C);
	    m_button.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		m_databaseDisplayFrame.doDefaultCloseAction();
	    		
	    		// Enable the create and open buttons 
		        m_createButton.setEnabled(true);
		        m_openButton.setEnabled(true);
		        // Enable the create and open menu items
		        m_openDBMenuItem.setEnabled(true);
		        m_createDBMenuItem.setEnabled(true);
	    	}
	    });
	    buttonPanel.add(m_button);
	    
	    m_databaseDisplayFrame.add(buttonPanel, BorderLayout.SOUTH);
	    
	}
	
	private void populateTable() {
		Vector<Vector<String>> databaseData = null;
		
		// Get database file and password inorder to open it
		File database = m_currentDatabaseWindow.getDatabase();
		String password = m_currentDatabaseWindow.getPassword();
		
		databaseData = DatabaseManager.openDatabase(database, password);
		
		Vector<String> header; // Used to store data header 
        // Create header for the table
        header = new Vector<String>();
        header.add("ID"); // ID
        header.add("Account"); // Account
        header.add("Username"); // Username
        header.add("Password"); // Password
        
        // Use a JScrollPane to display the table inorder for the scroll bar to appear
        JScrollPane jScrollPane = new JScrollPane();
        
        JTable table = new JTable(databaseData, header);
        table.setEnabled(false);
        jScrollPane.setViewportView(table);	// Add table to the scroll pane to give it a scrolling property       
        m_databaseDisplayFrame.add(jScrollPane, BorderLayout.CENTER);
        m_databaseDisplayFrame.repaint();
        m_databaseDisplayFrame.revalidate(); // Refresh the internal frame in order to display the newly added content -> the table
	}
	
	private void refreshDatabase() {
		// Close the current internal frame which has the table with old data
		m_databaseDisplayFrame.doDefaultCloseAction();
		
		// Get database file and password inorder to validate and open it
		File database = m_currentDatabaseWindow.getDatabase();
		
		// Create a new internal frame for the new database imported
	    createDBInternalFrame(database.toString());		
		
	    populateTable();
	    
        try {
			m_databaseDisplayFrame.setMaximum(true); // Maximize the internal frame
		} catch (PropertyVetoException e1) {
			e1.printStackTrace();
		}
        
        // Display the newly populated database table frame
        m_databaseDisplayFrame.setVisible(true);    
        
	}
	
	private class ButtonListener implements ActionListener {
		private String m_function;
		
		public ButtonListener(String function) {
			m_function = function; 
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(m_function == "createDatabase") {
				// Call Database creating function here
				
				new CreateWindow("Create Database", 500, 375);
								
			}
			else if(m_function == "openDatabase") {
				// Call Database opening function here
				OpenWindow openWindow = new OpenWindow();
				
				// Get database file and password inorder to validate and open it
				File database = openWindow.getDatabase();
				String password = openWindow.getPassword();
				
				// Set our current database equal to the opened database 
				m_currentDatabase = database;
				
				// Set our current database window equal to the opened database window
				m_currentDatabaseWindow = openWindow;
				
				// Opening database and retrieving content
				if(database!=null && password!=null) {
					Vector<Vector<String>> databaseData;
					
					databaseData = DatabaseManager.openDatabase(database, password);
										
					if(databaseData == null) {
						// If wrong password was typed
						JOptionPane.showMessageDialog(null, "Invalid Database Password", "ERROR!", JOptionPane.ERROR_MESSAGE);
						
					} else {
						
						// Create a new internal frame for the new database imported
					    createDBInternalFrame(database.toString());		
						
					    populateTable();
					    
				        try {
							m_databaseDisplayFrame.setMaximum(true); // Maximize the internal frame
						} catch (PropertyVetoException e1) {
							e1.printStackTrace();
						}
				        
				        m_databaseDisplayFrame.setVisible(true);    
				        
				        // Disable the create and open buttons 
				        m_createButton.setEnabled(false);
				        m_openButton.setEnabled(false);
				        // Desable the create and open menu items
				        m_openDBMenuItem.setEnabled(false);
				        m_createDBMenuItem.setEnabled(false);
						
					}	
					
				} 
				
			} 
			else if (m_function == "deleteRecord") {
				String id = JOptionPane.showInputDialog( null, "Enter ID: ");
					
				if ( id != null) {
					if ( !id.isEmpty() ) {
						if (id.contentEquals("0")) {
							JOptionPane.showMessageDialog( null, "Cannot Delete The Specified Record Because It Contains Database Information", "ERROR!", JOptionPane.ERROR_MESSAGE);
						} else {
							File database = m_currentDatabaseWindow.getDatabase();
							DatabaseManager.deleteRecord(database, Integer.parseInt(id));
						}
					} else {
						JOptionPane.showMessageDialog( null, "ID Field Cannot Be Empty", "ERROR!", JOptionPane.ERROR_MESSAGE);
					}
				
				}
				
			}
			
			else if (m_function == "addRecord") {
				new AddWindow("Add A New Record",300, 255, m_currentDatabase);
			}
			
			else if (m_function == "updateRecord") {
				new UpdateWindow("Update A Record", 300, 285, m_currentDatabase);
			}
			
			else if(m_function == "exit") {
				System.exit(0);
			}
			
			else if(m_function == "about"){
				AboutWindow aboutWindow = new AboutWindow("About Password Manager", 385, 250);
				aboutWindow.setLocationRelativeTo(null);
				aboutWindow.create();
			}
			
			else {
				JOptionPane.showMessageDialog( null, "Action Not Found! ", m_function, JOptionPane.PLAIN_MESSAGE );
			}
			
		}
		
	}

	
}
