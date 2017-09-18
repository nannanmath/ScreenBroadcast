package nan.learnjava.screenbroadcast;


import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClientUI extends JFrame{
	private JLabel label = null; 
	private ImageIcon icon = null;
	
	public ClientUI() {
		init();
	}
	
	public void init(){
		this.setBounds(0, 0, 1440, 900);
		this.setLayout(null);
		
		icon = new ImageIcon("ScreenShot.jpg");
		
		label = new JLabel();
		label.setBounds(0, 0, 1024, 768);
		label.setIcon(icon);
		
		this.add(label);
		
		this.setVisible(true);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void refreshImage(byte[] imageData) {
		icon = new ImageIcon(imageData);
		label.setIcon(icon);
	}
}
