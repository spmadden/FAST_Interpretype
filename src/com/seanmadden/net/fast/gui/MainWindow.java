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

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JWindow;

import com.seanmadden.net.fast.FastInterpretype;

public class MainWindow extends JFrame implements ActionListener{
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
		
	}
	
	public MainWindow(FastInterpretype fi){
		super();
		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		setSize(200, 200);
		this.fi = fi;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ToolFileExit")){
			
		}else if(e.getActionCommand().equals("ToolOptions")){
			
		}else if(e.getActionCommand().equals("ToolHelpAbout")){
			
		}
		
	}
	
	
}
