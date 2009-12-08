/*
 * AboutWindow.java
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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class AboutWindow extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1464278109276720250L;

	private static final String copyrightNotice = "Copyright (C) 2009 Sean P Madden \n\n"
			+ "This program is free software: you can redistribute it and/or modify "
			+ "it under the terms of the GNU General Public License as published by "
			+ "the Free Software Foundation, either version 3 of the License, or "
			+ "(at your option) any later version.\n\n"
			+ "This program is distributed in the hope that it will be useful, "
			+ "but WITHOUT ANY WARRANTY; without even the implied warranty of "
			+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the "
			+ "GNU General Public License for more details. \n\n"
			+ "You should have received a copy of the GNU General Public License "
			+ "along with this program.  If not, see <http://www.gnu.org/licenses/>.\n\n\n"
			+ "This software is supported by the FAST Helpdesk 475-FAST (3278)\n\n\n"
			+ "Version 2.0";

	public AboutWindow() {
		setLayout(new BorderLayout());
		JTextArea text = new JTextArea();
		text.setEditable(false);
		text.setWrapStyleWord(true);
		text.setLineWrap(true);
		text.setForeground(Color.black);
		text.setText(copyrightNotice);
		add(text, BorderLayout.CENTER);
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		add(closeButton, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(400, 400);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}
}
