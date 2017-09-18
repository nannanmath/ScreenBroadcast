package nan.learnjava.screenbroadcast;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import nan.learnjava.net.Packet;

public class ClientReceiverThread extends Thread {
	private Map<Long, Map<Integer, byte[]>> grpID2imagePatches =
			new HashMap<Long, Map<Integer, byte[]>>(); // All images.
	private Map<Integer, byte[]> imagePatches = null; // All patches of one image.
	
	private DatagramSocket sock = null;
	private int port = 8889;
	ClientUI ui = null;
	
	public ClientReceiverThread(ClientUI ui) {
		try {
			this.ui = ui;
			sock = new DatagramSocket(port);
			System.out.println("Client server start!");
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		byte[] buf = new byte[60 * 1024];
		DatagramPacket dataPack = new DatagramPacket(buf, buf.length);
		try {
			while(true) {
				sock.receive(dataPack);
				Packet pack = new Packet(dataPack); // Get properties of data packet.
				long grpID = pack.getGrpID();
				int index = pack.getIndex();
				byte[] content = pack.getContent();
				if(grpID2imagePatches.containsKey(grpID)) { // 
					imagePatches = grpID2imagePatches.get(grpID);
					imagePatches.put(index, content);
				} else {
					imagePatches  = new HashMap<Integer, byte[]>();
					imagePatches.put(index, content);
					grpID2imagePatches.put(grpID, imagePatches);
				}
				
				if(grpID2imagePatches.get(grpID).size() == pack.getTotalCount()) { // Has got all packets for one image.
					byte[] bytes = mergePackets(grpID2imagePatches.get(grpID));
					//byte[] ungzipData = gzipDecompress(pack.getData(),0, len);
					ui.refreshImage(bytes);
				}
				
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	private byte[] mergePackets(Map<Integer, byte[]> map) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for(Integer index : map.keySet()){
				baos.write(map.get(index));
			}
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// unzip.
	public static byte[] gzipDecompress(byte[] gzipData,
			int offset, int length) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ByteArrayInputStream bais =
					new ByteArrayInputStream(gzipData, offset, length);
			GZIPInputStream gzis = new GZIPInputStream(bais);
			int len = 0;
			byte[] buf = new byte[1024];
			while((len = gzis.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
