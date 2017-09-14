package nan.learnjava.screenbroadcast;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class ScreenBroadcastServer {

	public static void main(String[] args) {
		Rectangle rect = new Rectangle(0,0,1400,900);
		try {
			BufferedImage bi = new Robot().createScreenCapture(rect);
			ImageIO.write(bi, "jpg", new FileOutputStream("ScreenShot.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
