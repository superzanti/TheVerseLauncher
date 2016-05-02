package com.superzanti.launcher.gui.elements;

import com.superzanti.launcher.gui.Utils;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class Button extends JButton {

	private static final long serialVersionUID = 4812775577283223418L;
	
	public Button(String text) {
		setForeground(Color.WHITE);
		setFocusPainted(false);
		setBackground(new Color(26, 154, 54));
		setFont(Utils.getRobotoLight().deriveFont(20f));
		setBorder(new LineBorder(new Color(26, 154, 54), 4));
		setText(text);
	}
	
	public Button(String text, Color bg, Color color) {
		setForeground(color);
		setFocusPainted(false);
		setBackground(bg);
		setFont(Utils.getRobotoLight().deriveFont(20f));
		setBorder(new LineBorder(bg, 4));
		setText(text);
	}

}
