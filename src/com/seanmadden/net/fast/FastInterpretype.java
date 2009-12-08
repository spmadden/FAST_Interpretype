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

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONException;
import org.json.JSONObject;

import com.seanmadden.net.fast.gui.MainWindow;

public class FastInterpretype implements Observer {

	private JSONObject config = new JSONObject();
	private SerialInterface si = null;
	private MainWindow mw = null;

	private String filename = "FastInterpretype.json";

	private String remoteUser = "";
	private boolean needToSendDir = true;

	public FastInterpretype() {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = "", jsonData = "";
			while ((line = reader.readLine()) != null) {
				jsonData += line;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Configuration not found!  Preloading settings.");
		try {
			config.put("SerialPort", "COM1");
			config.put("SerialBaud", 19200);
			config.put("SerialParity", 0);
			config.put("SerialBits", 8);
			config.put("SerialStopBits", 1);
			config.put("StringBreakLength", 50);
			config.put("OperatorName", "Operator");
			FileWriter writer = new FileWriter(filename);
			config.write(writer);
			writer.flush();
			writer.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		si = new SerialInterface(config);
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
		try {
			int stringBreakLength = config.getInt("StringBreakLength");
			if (text.length() > stringBreakLength) {
				int chunks = text.length() / stringBreakLength;
				for (int i = 0; i < chunks; i++) {
					acceptTextSent(text.substring(stringBreakLength * i,
							stringBreakLength * (i + 1)));
				}
				acceptTextSent(text.substring(stringBreakLength * chunks));
				return;
			}
			if (needToSendDir) {
				si.sendRawDataToPort(DataPacket.generateUserPayload(mw
						.getLocalUserName()));
			}
			si.sendDataToPort(text);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void configEditWindow() {
		JFrame frame = new JFrame();

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
			windowCleared = saveLogToFile();
		} else if (selected == JOptionPane.NO_OPTION) {
			windowCleared = true;
		}

		if (windowCleared) {
			si.sendRawDataToPort(DataPacket.generateClearConvoPayload());
			mw.clearWindow();
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

	public boolean saveLogToFile() {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Text Files", "txt");
		jfc.setFileFilter(filter);
		int retVal = jfc.showSaveDialog(null);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			String filename = jfc.getSelectedFile().getPath();
			mw.saveWindowToFile(filename);
			return true;
		}
		return false;
	}

}
