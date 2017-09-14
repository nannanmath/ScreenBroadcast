package nan.learnjava.screenbroadcast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Sender {
	DatagramSocket socket;
	
	public Sender(){
		try {
			socket = new DatagramSocket(8888);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void pushMessage(){
		int i = 0;
		while(true) {
			byte[] buf = new byte[1024];
			DatagramPacket pack = new DatagramPacket(buf, buf.length);
			
			InetAddress addr;
			try {
				addr = InetAddress.getByName("127.0.0.1");
				pack.setAddress(addr);
				pack.setPort(8888);
				socket.send(pack);
			} catch (Exception e) {
				e.printStackTrace();
			}
			++i;
		}
	}
}
