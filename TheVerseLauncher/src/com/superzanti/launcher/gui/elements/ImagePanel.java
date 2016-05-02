package com.superzanti.launcher.gui.elements;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = -6433860056217964933L;
	private Image img;
	public ImagePanel(Image img) {
		this.img = img;
		setLayout(null);
	}
	
	public void setImage(Image img) {
		this.img = img;
	}

	  public void paintComponent(Graphics g) {
		  super.paintComponent(g);
		  int w = img.getWidth(this);
          int h = img.getHeight(this);
          if (w > 0 && h > 0) {
              for (int x = 0; x < getWidth(); x += w) {
                  for (int y = 0; y < getHeight(); y += h) {
                      g.drawImage(img, x, y, w, h, null);
                  }
              }
          }
	  }
}
