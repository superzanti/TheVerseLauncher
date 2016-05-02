package com.superzanti.launcher.gui.elements;

import com.superzanti.launcher.gui.Utils;

import java.awt.Color;

import javax.swing.JPasswordField;
import javax.swing.border.LineBorder;

public class PasswordField extends JPasswordField {

	private static final long serialVersionUID = -4308418895987978036L;
	
	public PasswordField(String text) {
		setText(text);
		setFont(Utils.getRobotoLight().deriveFont(20f));
		setBorder(new LineBorder(new Color(255, 255, 255), 4));
	}

}
