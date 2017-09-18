package nan.learnjava.net;

import java.net.DatagramPacket;

import nan.learnjava.utils.Utils;

public class Packet {
	private long grpID; // Group ID.
	private int index; // Index of packet in whole packets group.
	private int totalCount; // Total amount of packets in a group.
	private byte[] content; // Content in one packet.
	public long getGrpID() {
		return grpID;
	}
	public int getIndex() {
		return index;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public byte[] getContent() {
		return content;
	}


	private int validPackLength; // Valid length of whole packet.
	private int contentLength; // Length of content in this packet.
	public int getValidPackLength() {
		return validPackLength;
	}
	public int getContentLength() {
		return contentLength;
	}
	
	/**
	 * Parse a packet into properties. 
	 */
	public Packet(DatagramPacket pack) {
		byte[] packBytes = pack.getData();
		validPackLength = pack.getLength();
		contentLength = validPackLength - 10;
		
		grpID = Utils.bytes2long(packBytes);
		index = packBytes[8];
		totalCount = packBytes[9];
		content = new byte[contentLength];
		System.arraycopy(packBytes, 10, content, 0, contentLength);
	}
	
}
