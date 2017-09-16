package nan.learnjava.screenbroadcast;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

public class ClientReceiverThread extends Thread {
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
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		try {
			while(true){
				sock.receive(pack);
				int len = pack.getLength();
				byte[] ungzipData = gzipDecompress(pack.getData(),
						0, len);
				ui.refreshImage(ungzipData);
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
