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

import com.seanmadden.net.fast.gui.MainWindow;
import com.seanmadden.xmlconfiguration.XMLConfiguration;

public class FastInterpretype {

	private XMLConfiguration config = new XMLConfiguration();
	
	public FastInterpretype(){
		config.setName("FastInterpretype"); 
		if(!config.parseXMLFile("FastInterpretypeConfig.xml")){
			System.out.println("Configuration not found!  Preloading settings.");
			config.addValue("SerialPort", "COM!");
			config.addValue("SerialBaud", 19200);
			config.addValue("SerialParity", "none");
			config.addValue("SerialBits", 8);
			config.addValue("SerialStopBits", 1);
			
			config.addValue("OperatorName", "Operator");
		}
	}
	
	/**
	 * Main Entrance to the Interpretype Software
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainWindow mw = new MainWindow(new FastInterpretype());
		mw.validate();
		mw.setVisible(true);
	}

}
