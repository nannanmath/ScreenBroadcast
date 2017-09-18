package nan.learnjava.server;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import nan.learnjava.utils.Utils;

public class ScreenBroadcastServer {
	public static void main(String[] args) throws Exception{
		DatagramSocket sock = new DatagramSocket(8888);
		while(true){
			// get Screenshot image.
			Rectangle rect = new Rectangle(0,0,1440,900);
			BufferedImage bi = new Robot().createScreenCapture(rect);
			//ImageIO.write(bi, "jpg", new FileOutputStream("ScreenShot.jpg"));
			
			// Write screenshot to OutputStream.
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			byte[] rawData = baos.toByteArray();
			
			//GZIP compress
			//byte[] gzipData = gzipCompress(rawData);
			
			List<DatagramPacket> packs = SplitPacket(rawData);
			// send packet.
			for(DatagramPacket pack : packs) {
				sock.send(pack);
				System.out.println("A pack has sent to " + pack.getPort());				
			}
		}
	}
	
	public static byte[] gzipCompress(byte[] rawData){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzos = new GZIPOutputStream(baos);
			gzos.write(rawData);
			gzos.close();
			
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Split raw data into a list of small packets.
	 */
	public static List<DatagramPacket> SplitPacket(byte[] rawData) {
		List<DatagramPacket> packs = new ArrayList<DatagramPacket>();
		// Length of raw data.
		int length = rawData.length;
		// Length of small packet (Byte).
		// packet constructor:
		// 
		// group ID	| 8 bytes
		// Index	| 1 byte
		// Count	| 1 byte
		// Content	| <= 50K bytes
		//
		int contentLenPerPack = 50 * 1024; // 50KB.
		int totalLenPerPack = contentLenPerPack + 10;
		// Number of packets for that raw data.
		int count = 0;
		if(length % contentLenPerPack == 0) {
			count = length / contentLenPerPack;
		} else {
			count = length / contentLenPerPack + 1;
		}
		// Group ID.
		long grpID = System.nanoTime();
		
		DatagramPacket pack = null;
		// Group ID.
		byte[] grpIDBytes = Utils.long2bytes(grpID);
		byte[] packBytes = null;
		for(int idx = 0; idx < count; ++idx) {
			if(idx != (count -1)) { // Not the last one.
				packBytes = new byte[totalLenPerPack];
				System.arraycopy(grpIDBytes, 0, packBytes, 0, 8);
				// Index.
				packBytes[8] = (byte)idx;
				// Count.
				packBytes[9] = (byte)count;
				// Content.
				System.arraycopy(rawData, idx * contentLenPerPack,
						packBytes, 10, contentLenPerPack);
				
			} else { // The last one.
				int remain = rawData.length - (count - 1) * contentLenPerPack;
				packBytes = new byte[remain + 10];
				System.arraycopy(grpIDBytes, 0, packBytes, 0, 8);
				// Index.
				packBytes[8] = (byte)idx;
				// Count.
				packBytes[9] = (byte)count;
				// Content.
				System.arraycopy(rawData, idx * contentLenPerPack,
						packBytes, 10, remain);
			}
			
			try {
				// Create a packet for this split.
				pack = new DatagramPacket(packBytes, packBytes.length,
						InetAddress.getByName("127.0.0.1"), 8889);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			packs.add(pack); // Add to List of packet.
		}
		
		return packs;
	}

}
