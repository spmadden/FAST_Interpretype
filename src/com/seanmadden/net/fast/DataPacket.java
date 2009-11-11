/*
 * DataPacket.java
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

public class DataPacket {
	private String payload = "";
	private String data = "";
	private static char[] header = { 0xAA, 0xAA, 0xFF, 0xFF, 0x14 };
	private static String headerStr = String.copyValueOf(header);
	private static char[] flags = { 0x00, 0x00 };
	private static String flagsStr = String.copyValueOf(flags);
	private static char[] userFlags = {0x03, 0x07};
	private static String userFlgsStr = String.copyValueOf(userFlags);
	private static char[] footer = { 0x00, 0xFF, 0x00 };
	private static String footerStr = String.copyValueOf(footer);
	
	private boolean userSwitch = false;

	public DataPacket(String data) {
		this.payload = data;
	}

	public String parseDataPacket() {
		if (!data.startsWith(headerStr)) {
			return null;
		}
		System.out.println(getFlags());
		if(getFlags().equals(userFlgsStr)){
			userSwitch = true;
			
		}
		return data;
	}

	
	private String getFlags(){
		return data.substring(5, 6);
	}
	
	public static String generateDataPayload(String data) {
		int checksum = 0x65;
		for (char c : data.toCharArray()) {
			checksum += c;
		}
		checksum &= 0xFF;
		checksum = 0x100 - checksum;
		data = headerStr + flagsStr + data + footerStr + (char) checksum;
		return data;
	}
	
	public static  String toHexString(String data){
		String ret = "";
		for(char c : data.toCharArray()){
			ret += "0x" + Integer.toHexString((int)c) + " ";
		}
		
		return ret;
	}
}
