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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	private class ConfigOption extends JPanel implements ActionListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5729219379652742265L;

		private String name = "";
		private String value = null;
		private JTextField comp = null;

		public ConfigOption(String name, String value) {
			this.name = name;
			this.value = value;
			setLayout(new GridLayout(1,2));
			add(new JLabel(name));
			comp = new JTextField(value.toString());
			add(comp);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				config.put(name, comp.getText());
				System.out.println("Putting " + name + " : " + comp.getText());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}

	}

	public FastInterpretype() {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = "", jsonData = "";
			while ((line = reader.readLine()) != null) {
				jsonData += line;
			}
			config = new JSONObject(jsonData);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.out
					.println("Configuration not found!  Preloading settings.");
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
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
		try {
			config.put("OperatorName", username);
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
		final JFrame frame = new JFrame();
		final Vector<ConfigOption> components = new Vector<ConfigOption>();
		JPanel buttons = new JPanel(new GridLayout(1,2));
		JButton ok = new JButton("Save");
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for(ConfigOption config : components){
					config.actionPerformed(null);
				}
				frame.dispose();
				try {
					FileWriter writer = new FileWriter(filename);
					config.write(writer);
					writer.flush();
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			}
			
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		buttons.add(ok);
		buttons.add(cancel);
		Iterator<String> rator = config.keys();
		int size = 0;
		try {
			while (rator.hasNext()) {
				String key = (String) rator.next();
				ConfigOption config = new ConfigOption(key, this.config
						.getString(key));
				frame.add(config);
				components.add(config);
				++size;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		frame.setLayout(new GridLayout(size+1,1));
		frame.add(buttons);
		frame.setSize(300,400);
		frame.pack();
		frame.setVisible(true);
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
