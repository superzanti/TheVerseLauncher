package com.superzanti.launcher.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Utils {
	private static int posX;
	private static int posY;

	public static Image scaleImage(Image srcImg, int w, int h){
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}
	
	public static Image getUserAvatar(String username, int px) {
		Image avatar = null;
		Utils.scaleImage(Toolkit.getDefaultToolkit().getImage(Utils.class.getResource("/com/superzanti/resources/images/steve.png")), px, px);
		try {
			avatar = Utils.scaleImage(ImageIO.read(new URL("https://minepic.org/avatar/" + username)), px, px);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return avatar;
	}
	
	public static Font getRobotoLight() {
		Font robotoLight = null;
		try {
			robotoLight = Font.createFont(Font.PLAIN, LoginFrame.class.getResourceAsStream("/com/superzanti/resources/fonts/Roboto-Light.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(robotoLight); 
		return robotoLight;
	}
	@Deprecated
	public static JTextField getTextField(String ph) {
		JTextField textField = new JTextField();
		textField.setText(ph);
		textField.setFont(getRobotoLight().deriveFont(20f));
		textField.setBorder(new LineBorder(new Color(255, 255, 255), 4));
		
		return textField;
	}
	
	public static void styleTextField(JTextField textField) {
		textField.setFont(getRobotoLight().deriveFont(20f));
		textField.setBorder(new LineBorder(new Color(255, 255, 255), 4));
	}
	
	@Deprecated
	public static JPasswordField getPasswordField(String ph) {
		JPasswordField passwordField = new JPasswordField();
		passwordField.setText(ph);
		passwordField.setFont(getRobotoLight().deriveFont(20f));
		passwordField.setBorder(new LineBorder(new Color(255, 255, 255), 4));
		
		return passwordField;
	}
	
	public static void stylePasswordField(JPasswordField passwordField) {
		passwordField.setFont(getRobotoLight().deriveFont(20f));
		passwordField.setBorder(new LineBorder(new Color(255, 255, 255), 4));
	}

	@Deprecated
	public static JButton getButton(String txt) {
		JButton button = new JButton(txt);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBackground(new Color(26, 154, 54));
		button.setFont(getRobotoLight().deriveFont(20f));
		button.setBorder(new LineBorder(new Color(26, 154, 54), 4));
		
		return button;
	}
	
	public static void styleButton(JButton button) {
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBackground(new Color(26, 154, 54));
		button.setFont(getRobotoLight().deriveFont(20f));
		button.setBorder(new LineBorder(new Color(26, 154, 54), 4));
	}
	
	public static JLabel getCloseLabel() throws IOException {
		JLabel closeLabel = new JLabel(new ImageIcon(Utils.scaleImage(ImageIO.read(LoginFrame.class.getResource("/resources/images/close.png")), 16, 16)));
		closeLabel.setSize(new Dimension(16, 16));
		closeLabel.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    	System.exit(0);
		    }
		});
		return closeLabel;
	}
	
	public static void styleCloseLabel(JLabel closeLabel) {
		closeLabel.setText("");
		try {
			closeLabel.setIcon(new ImageIcon(Utils.scaleImage(ImageIO.read(LoginFrame.class.getResource("/resources/images/close.png")), 16, 16)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		closeLabel.setSize(new Dimension(16, 16));
		closeLabel.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    	System.exit(0);
		    }
		});
	}
	
	@Deprecated
	public static void setDraggable(final JFrame f) {
		posX = 0;
		posY = 0;
		
		f.addMouseListener(new MouseAdapter()
		{
		   public void mousePressed(MouseEvent e)
		   {
		      posX=e.getX();
		      posY=e.getY();
		   }
		});
		
		f.addMouseMotionListener(new MouseAdapter()
		{
		     public void mouseDragged(MouseEvent evt)
		     {		
				f.setLocation(evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
							
		     }
		});
	}
	
	public static void openLink(String link) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(link));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
}
