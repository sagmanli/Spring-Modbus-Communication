package com.ves.main.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.ves.main.config.ComAuthenticationProvider;
import com.ves.main.config.ComCrypto;

public class PasswordGenerator extends JFrame {
	/**
	 * Auto generated.
	 */
	private static final long serialVersionUID = 4611876558468897037L;

	public PasswordGenerator() {
		setSize(300, 200);
		setLayout(null);

		JLabel labelSifre = new JLabel("Şifre");
		labelSifre.setSize(75, 20);
		labelSifre.setLocation(75, 30);

		final JTextField textSifre = new JTextField();
		textSifre.setSize(120, 20);
		textSifre.setLocation(140, 30);

		JLabel labelRole = new JLabel("Rol");
		labelRole.setSize(75, 20);
		labelRole.setLocation(75, 55);

		final JComboBox roles = new JComboBox();
		final Set<Entry<String, String>> entries = ComAuthenticationProvider.roleTypes.entrySet();
		for (Entry<String, String> entry : entries) {
			roles.addItem(entry.getValue());
		}

		roles.setSize(120, 20);
		roles.setLocation(140, 55);

		JButton generateButton = new JButton("Şifre oluştur!");
		generateButton.setSize(120, 20);
		generateButton.setLocation(75, 90);

		final JTextField result = new JTextField();
		result.setSize(170, 20);
		result.setLocation(50, 125);

		generateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String roleName = (String) roles.getSelectedItem();
				for (Entry<String, String> entry : entries) {
					if (roleName.equals(entry.getValue())) {
						try {
							result.setText(ComCrypto.encrypt(textSifre.getText(), entry.getKey()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		add(labelSifre);
		add(textSifre);
		add(labelRole);
		add(roles);
		add(generateButton);
		add(result);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args) {
		new PasswordGenerator().setVisible(true);
	}
}
