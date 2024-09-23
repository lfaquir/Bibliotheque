package Biblio;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextArea;

public class Aframe extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPasswordField passwordField; // champ pour le mot de passe
	private JTextField textField_1; // champ pour le login


	public Aframe() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 704, 403);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(128, 128, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBackground(new Color(0, 128, 192));
		lblNewLabel.setBounds(10, 101, 86, 18);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel);
		
		JLabel lblMotDePasse = new JLabel("Mot de passe");
		lblMotDePasse.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMotDePasse.setHorizontalAlignment(SwingConstants.CENTER);
		lblMotDePasse.setBounds(10, 130, 146, 33);
		contentPane.add(lblMotDePasse);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(150, 138, 123, 20);
		contentPane.add(passwordField);
		passwordField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(150, 100, 123, 20);
		contentPane.add(textField_1);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verifyLogin(); // Appel à la méthode pour vérifier les informations de connexion
			}
		});
		btnNewButton.setBounds(218, 193, 55, 23);
		contentPane.add(btnNewButton);
	}
	
	// Méthode pour vérifier les informations de connexion
	private void verifyLogin() {
		String username = textField_1.getText(); // Récupérer le login
		String password = new String(passwordField.getPassword()); // Récupérer le mot de passe

		// Vérification des informations de connexion (exemple basique)
		if ("anwar".equals(username) && "udev2".equals(password)) {
			// Connexion réussie
			JOptionPane.showMessageDialog(this, "Connexion réussie !", 
					"Succès", JOptionPane.INFORMATION_MESSAGE);
			openDataTable();
			dispose();
		} else {
			// Connexion échouée
			JOptionPane.showMessageDialog(this, "Login ou mot de passe incorrect.",
					"Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
		
		@SuppressWarnings("unused")
		private void openDataTable(){
			DataTablePanel dataTableFrame = new DataTablePanel();
			dataTableFrame.setVisible(true);
		
	}
}

