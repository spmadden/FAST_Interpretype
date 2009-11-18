/*
* MainWindow.java
* 
*    Copyright (C) 2009 Sean P Madden
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*    If you would like to license this code under the GNU LGPL, please see
*    http://www.seanmadden.net/licensing for details.
*
*/
package com.seanmadden.net.fast.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import com.seanmadden.net.fast.FastInterpretype;

public class MainWindow extends JFrame implements ActionListener, DocumentListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6082950077067088553L;
	private JMenuBar toolbar = new JMenuBar();
	private JMenu toolFile = new JMenu("File");
	private JMenu toolOptions = new JMenu("Options");
	private JMenu toolHelp = new JMenu("Help");
	
	private JMenuItem toolFileExit = new JMenuItem("Exit");
	private JMenuItem toolOptionsConfig = new JMenuItem("Settings");
	private JMenuItem toolHelpAbout = new JMenuItem("About");
	
	private JTextArea mainTextLog = new JTextArea();
	private JPanel lowerPanel = new JPanel();
	private JTextArea sendTextArea = new JTextArea();
	private JButton sendTextButton = new JButton("Send");
	
	private FastInterpretype fi;
	
	{
		toolbar.add(toolFile);
		toolbar.add(toolOptions);
		toolbar.add(toolHelp);
		toolbar.setVisible(true);
		
		toolFile.add(toolFileExit);
		toolOptions.add(toolOptionsConfig);
		toolHelp.add(toolHelpAbout);
		
		toolFileExit.addActionListener(this);
		toolFileExit.setActionCommand("ToolFileExit");
		toolOptions.addActionListener(this);
		toolOptions.setActionCommand("ToolOptions");
		toolHelpAbout.addActionListener(this);
		toolHelpAbout.setActionCommand("ToolHelpAbout");
		
		lowerPanel.setLayout(new BorderLayout());
		lowerPanel.add(sendTextArea, BorderLayout.CENTER);
		lowerPanel.add(sendTextButton, BorderLayout.EAST);
		sendTextButton.setSize(270, 50);
		sendTextArea.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		sendTextArea.getDocument().addDocumentListener(this);
		
		mainTextLog.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		mainTextLog.setAutoscrolls(true);
		mainTextLog.setLineWrap(true);
		mainTextLog.setEditable(false);
		mainTextLog.setWrapStyleWord(true);
		
		
	}
	
	public MainWindow(FastInterpretype fi){
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		add(mainTextLog, BorderLayout.CENTER);
		add(lowerPanel, BorderLayout.SOUTH);
		setSize(500, 400);
		this.fi = fi;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ToolFileExit")){
			
		}else if(e.getActionCommand().equals("ToolOptions")){
			
		}else if(e.getActionCommand().equals("ToolHelpAbout")){
			
		}else if(e.getActionCommand().equals("SendTextButton")){
			
		}
		
	}

	public void changedUpdate(DocumentEvent arg0) {
		try {
			String text = arg0.getDocument().getText(0, arg0.getDocument().getLength());
			System.out.println(text.endsWith("\n"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertUpdate(DocumentEvent arg0) {
		
	}

	public void removeUpdate(DocumentEvent arg0) {
		
	}
	
	
}
