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
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import com.seanmadden.net.fast.FastInterpretype;

public class MainWindow extends JFrame implements ActionListener, KeyListener {
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
	private JMenuItem toolOptionsClear = new JMenuItem("Clear Windows");
	private JMenuItem toolHelpAbout = new JMenuItem("About");

	private JTextArea mainTextLog = new JTextArea();
	private JPanel lowerPanel = new JPanel();
	private JTextArea sendTextArea = new JTextArea();
	private JScrollPane mainTextScroller = new JScrollPane();
	private JButton sendTextButton = new JButton("Send");

	private String localUserName = "LocalUser";

	private FastInterpretype fi;

	{
		toolbar.add(toolFile);
		toolbar.add(toolOptions);
		toolbar.add(toolHelp);
		toolbar.setVisible(true);

		toolFile.add(toolFileExit);
		toolOptions.add(toolOptionsConfig);
		toolOptions.add(toolOptionsClear);
		toolHelp.add(toolHelpAbout);

		toolFileExit.addActionListener(this);
		toolFileExit.setActionCommand("ToolFileExit");
		toolOptions.addActionListener(this);
		toolOptions.setActionCommand("ToolOptions");
		toolOptionsClear.addActionListener(this);
		toolOptionsClear.setActionCommand("ToolClear");
		toolOptionsConfig.addActionListener(this);
		toolOptionsConfig.setActionCommand("ToolConfig");
		toolHelpAbout.addActionListener(this);
		toolHelpAbout.setActionCommand("ToolHelpAbout");

		lowerPanel.setLayout(new BorderLayout());
		lowerPanel.add(sendTextArea, BorderLayout.CENTER);
		lowerPanel.add(sendTextButton, BorderLayout.EAST);
		sendTextButton.setSize(270, 50);
		sendTextButton.addActionListener(this);
		sendTextButton.setActionCommand("SendTextButton");
		sendTextArea.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		sendTextArea.addKeyListener(this);

		mainTextLog.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		mainTextLog.setAutoscrolls(true);
		mainTextLog.setLineWrap(true);
		mainTextLog.setEditable(false);
		mainTextLog.setWrapStyleWord(true);

		mainTextScroller.setViewportView(mainTextLog);
		mainTextScroller
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		mainTextScroller
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	}

	public MainWindow(FastInterpretype fi) {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		add(mainTextScroller, BorderLayout.CENTER);
		add(lowerPanel, BorderLayout.SOUTH);
		setSize(500, 400);
		this.fi = fi;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ToolFileExit")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("ToolOptionsConfig")) {
		} else if (e.getActionCommand().equals("ToolClear")) {
			fi.clearWindows();
		} else if (e.getActionCommand().equals("ToolHelpAbout")) {
			new AboutWindow();
		} else if (e.getActionCommand().equals("SendTextButton")) {
			System.out.println("Send button clicked.");
			sendTextToFI();
		}

	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\n') {
			sendTextToFI();
		}
	}

	private void sendTextToFI() {
		mainTextLog.append(localUserName + ": \t" + sendTextArea.getText());
		fi.acceptTextSent(sendTextArea.getText());
		sendTextArea.setText("");
	}

	public void acceptText(String line, String username) {
		mainTextLog.append(username + ": \t" + line + "\n");
	}

	public void acceptText(String line) {
		mainTextLog.append(line);
	}

	public void setLocalUserName(String username) {
		this.localUserName = username;
	}
	
	public String getLocalUserName(){
		return localUserName;
	}
	
	public void clearWindow(){
		mainTextLog.setText("");
	}
	
	public void saveWindowToFile(String filename){
		try {
			FileWriter file = new FileWriter(filename);
			file.write(mainTextLog.getText());
			file.close();
			clearWindow();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
