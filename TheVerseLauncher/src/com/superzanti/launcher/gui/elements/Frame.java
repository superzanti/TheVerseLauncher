package com.superzanti.launcher.gui.elements;

import com.superzanti.launcher.gui.Utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Frame extends JFrame {

	private static final long serialVersionUID = -7625310292650863963L;
	private int posX;
	private int posY;
	
	public Frame() {
		try {
			setIconImage(Utils.scaleImage(ImageIO.read(Frame.class.getResource("/com/superzanti/resources/images/logo.png")), 200, 200));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		posX = 0;
		posY = 0;
		addMouseListener(new MouseAdapter()
		{
		   public void mousePressed(MouseEvent e)
		   {
		      posX=e.getX();
		      posY=e.getY();
		   }
		});
		addMouseMotionListener(new MouseAdapter()
		{
		     public void mouseDragged(MouseEvent evt)
		     {		
				setLocation(evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
							
		     }
		});
	}

}
