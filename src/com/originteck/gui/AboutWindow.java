package com.originteck.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class AboutWindow extends SubWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextArea m_aboutText;	
	private JLabel m_aboutLabel;
	private JButton m_button;
	private JButton m_close;
	
	private JPanel m_container;
	
	private String m_info = "\n     Version 1.0\n\n"
			+ "     Password Manager is a free and open source \n     password management application developed\n     by OriginTeck Technology Company.";
	
	public AboutWindow(String name, int width, int height) {
		super(name, width, height);
		setResizable(false);
	}

	@Override
	public void create() {
		m_aboutLabel = new JLabel("Password Manager");
		m_aboutLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(m_aboutLabel, BorderLayout.NORTH);
		
		m_container = new JPanel(new BorderLayout());
		
		m_button = new JButton(new ImageIcon(getClass().getResource("images/logo.png")));
		m_button.setBackground(Color.WHITE);
		m_button.setFocusable(false);
		m_button.setBorderPainted(false);
		m_container.add(m_button, BorderLayout.WEST);
		
		m_aboutText = new JTextArea(m_info);
		m_aboutText.setEditable(false);
		m_container.add(m_aboutText, BorderLayout.CENTER);
		
		add(m_container, BorderLayout.CENTER);
		
		m_container = new JPanel();
		m_container.setLayout(new FlowLayout());
		
		m_close = new JButton("Close");
		m_close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		m_container.add(m_close);
		
		add(m_container, BorderLayout.SOUTH);
		
		setVisible(true);

		
	}
	
	
	private void close(){
	
		WindowEvent winClosingEvent = new WindowEvent(this,WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);
		
	}
}
