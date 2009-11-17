/*
 * SerialInterface.java
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
import java.util.Enumeration;
import java.util.Scanner;
import java.util.TooManyListenersException;  

import gnu.io.*;

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class SerialInterface implements Runnable, SerialPortEventListener {
	private String comPort = null;
	private CommPortIdentifier comID;
	private BufferedReader portIn;
	private BufferedWriter portOut;
	private SerialPort thePort;
	
	private boolean openable = false;
	private boolean portOpen = false;

	public SerialInterface() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.startsWith("win")) {
			comPort = "COM1";
			openable = true;
		} else if (os.startsWith("lin")) {
			comPort = "/dev/ttyS0";
			openable = true;
		}
	}

	public SerialInterface(String comPort) {
		this.comPort = comPort;
	}

	@SuppressWarnings("unchecked")
	public boolean open() {
		if (this.comPort == null || openable == false) {
			return false;
		}
		System.out.println("Port set to " + comPort);

		Enumeration<CommPortIdentifier> portList = CommPortIdentifier
				.getPortIdentifiers();
		CommPortIdentifier portID;
		while (portList.hasMoreElements()) {
			portID = portList.nextElement();
			if (portID.getName().equals(comPort)) {
				portOpen = true;
				comID = portID;
			}
		}

		try {
			thePort = (SerialPort) comID.open("SerialInterface", 2000);
			portIn = new BufferedReader(new InputStreamReader(thePort.getInputStream()));
			portOut = new BufferedWriter(new OutputStreamWriter(thePort.getOutputStream()));
			thePort.addEventListener(this);
			thePort.notifyOnDataAvailable(true);
			thePort.setSerialPortParams(19200, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return portOpen;
	}

	public void close() {
		if (thePort != null)
			thePort.close();
	}

	public static void main(String[] args) {
		SerialInterface inter = new SerialInterface();
		inter.open();
		
		System.out.println("Welcome.  Commands with 'help'");
		String cmd = "";
		Scanner scan = new Scanner(System.in);
		do{
			System.out.print("> ");
			cmd = scan.nextLine();
			if(cmd.toLowerCase().equals("help")){
				System.out.println("Commands:");
				System.out.println("help : prints this message");
				System.out.println("send <message> : sends <message> to interpretype.");
				System.out.println("quit : exits this program");
			}else if(cmd.toLowerCase().startsWith("send")){
				String message = cmd.substring(cmd.toLowerCase().indexOf(' ') + 1);
				inter.sendDataToPort(message);
			}
		}while(!cmd.toLowerCase().equals("quit"));
		

		inter.close();
	}

	public void run() {
		// TODO Auto-generated method stub

	}
	
	public void sendDataToPort(String data){
		if(!portOpen){
			System.out.println("Port was unable to be opened.");
			return;
		}
		System.out.println("Writing '" + data + "'");
		System.out.println(DataPacket.toHexString(data));
		try {
			portOut.write(data);
			portOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getDataFromPort(){
		char[] arr = new char[255];
		String toRet = "";
		try {
			if(portIn.ready()){
				int num = portIn.read(arr);
				DataPacket pkt = new DataPacket(String.copyValueOf(arr, 0, num));
				System.out.println(pkt.parseDataPacket());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return toRet;
	}
	
	public void serialEvent(SerialPortEvent arg0) {
		if(arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE){
			System.out.println(getDataFromPort());
		} 
	}
}
