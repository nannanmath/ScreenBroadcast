package nan.learnjava.screenbroadcast;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

public class ScreenBroadcastServer {

	public static void main(String[] args) throws Exception{
		DatagramSocket sock = new DatagramSocket(8888);
		while(true){
			// get Screenshot image.
			Rectangle rect = new Rectangle(0,0,400,300);
			BufferedImage bi = new Robot().createScreenCapture(rect);
			//ImageIO.write(bi, "jpg", new FileOutputStream("ScreenShot.jpg"));
			
			// Write screenshot to OutputStream.
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			byte[] rawData = baos.toByteArray();
			
			//GZIP compress
			byte[] gzipData = gzipCompress(rawData);
			
//			// new DatagramPacket.
//			DatagramPacket pack = new DatagramPacket(data, data.length);
//			pack.setAddress(InetAddress.getByName("127.0.0.1"));
//			pack.setPort(8889);
//			// send packet.
//			sock.send(pack);			
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

}
