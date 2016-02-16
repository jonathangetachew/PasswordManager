package com.originteck.gui;

import java.awt.Toolkit;

import javax.swing.JFrame;

public abstract class SubWindow extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Main Constructor to create the window
	public SubWindow(String name, int width, int height) {
		super(name);
		// Setting the window title bar image
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/logo.png")));
		setLocationRelativeTo(null);
		setSize(width,height);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	// Method to create the window
	protected abstract void create();
	
}