package com.superzanti.launcher.gui.elements;

import com.superzanti.launcher.gui.Utils;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class TextField extends JTextField{

	private static final long serialVersionUID = -6702093839402499758L;
	
	public TextField(String placeholder) {
		setFont(Utils.getRobotoLight().deriveFont(20f));
		setBorder(new LineBorder(new Color(255, 255, 255), 4));
		setText(placeholder);
	}
}
