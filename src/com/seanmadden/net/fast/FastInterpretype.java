/*
 * FastInterpretype.java
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
package com.seanmadden.net.fast;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.seanmadden.net.fast.gui.MainWindow;
import com.seanmadden.xmlconfiguration.XMLConfiguration;

public class FastInterpretype implements Observer {

	private XMLConfiguration config = new XMLConfiguration();
	private SerialInterface si = new SerialInterface();
	private MainWindow mw = null;

	private String remoteUser = "";
	private boolean needToSendDir = true;

	public FastInterpretype() {
		config.setName("FastInterpretype");
		if (!config.parseXMLFile("FastInterpretypeConfig.xml")) {
			System.out
					.println("Configuration not found!  Preloading settings.");
			config.addValue("SerialPort", "COM1");
			config.addValue("SerialBaud", 19200);
			config.addValue("SerialParity", "none");
			config.addValue("SerialBits", 8);
			config.addValue("SerialStopBits", 1);

			config.addValue("OperatorName", "Operator");
		}
		si.open();
		si.addObserver(this);
		mw = new MainWindow(this);
		si.sendRawDataToPort(DataPacket.generateClearConvoPayload());
		String username = JOptionPane.showInputDialog("What is your name?");
		si.sendRawDataToPort(DataPacket.generateSignedOnPayload(username));
		mw.acceptText(username + " (you) has signed on.\n");
		mw.setLocalUserName(username);
		mw.validate();
		mw.setVisible(true);
	}

	public void acceptTextSent(String text) {
		if (needToSendDir) {
			si.sendRawDataToPort(DataPacket.generateUserPayload(mw
					.getLocalUserName()));
		}
		si.sendDataToPort(text);
	}

	public void configEditWindow() {
		config.editUsingGui();
	}

	/**
	 * Main Entrance to the Interpretype Software
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FastInterpretype fi = new FastInterpretype();

	}

	public void clearWindows() {
		boolean windowCleared = false;
		int selected = JOptionPane
				.showConfirmDialog(
						null,
						"Would you like to save the current conversation before clearing?",
						"Save before clearing?",
						JOptionPane.YES_NO_CANCEL_OPTION);
		if (selected == JOptionPane.YES_OPTION) {
			JFileChooser jfc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Text Files", "txt");
			jfc.setFileFilter(filter);
			int retVal = jfc.showSaveDialog(null);
			if (retVal == JFileChooser.APPROVE_OPTION) {
				String filename = jfc.getSelectedFile().getPath();
				mw.saveWindowToFile(filename);
				mw.clearWindow();
				si.sendRawDataToPort(DataPacket.generateClearConvoPayload());
				windowCleared = true;
			}
		} else if (selected == JOptionPane.NO_OPTION) {
			// clear windows
			si.sendRawDataToPort(DataPacket.generateClearConvoPayload());
			mw.clearWindow();
			windowCleared = true;
		}

		if (windowCleared) {
			String username = JOptionPane.showInputDialog("What is your name?");
			si.sendRawDataToPort(DataPacket.generateSignedOnPayload(username));
			mw.acceptText(username + " (you) has signed on.\n");
			mw.setLocalUserName(username);
		}
	}

	public void update(Observable o, Object arg) {
		DataPacket dp = (DataPacket) arg;
		if (dp.directionSwitched()) {
			// mw.acceptText(", username)
			needToSendDir = true;
		} else if (dp.signedOn()) {
			mw.acceptText(dp.parseDataPacket() + " has signed on.\n");
			remoteUser = dp.parseDataPacket();
		} else {
			mw.acceptText(dp.parseDataPacket(), remoteUser);
		}
	}

}
