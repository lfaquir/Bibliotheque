package Biblio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class DataTablePanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;

    public DataTablePanel() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Définir les colonnes du tableau
        String[] columnNames = { "Code", "Titre", "Auteur", "Année", "Prix" };

        // Créer le modèle de table
        model = new DefaultTableModel(new Object[][]{}, columnNames);
        table = new JTable(model);
        
        //ajouter une table à une interface graphique Java
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 660, 300);
        contentPane.add(scrollPane);

        // Bouton Ajouter
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBounds(10, 320, 100, 25);
        contentPane.add(btnAjouter);
        btnAjouter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddDialog(); // Afficher la fenêtre pour ajouter un livre
            }
        });

        // Bouton Modifier
        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBounds(120, 320, 100, 25);
        contentPane.add(btnModifier);
        btnModifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifierLigne();
            }
        });

        // Bouton Supprimer
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBounds(230, 320, 100, 25);
        contentPane.add(btnSupprimer);
        btnSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supprimerLigne();
            }
        });

        // Bouton Imprimer (afficher)
        JButton btnImprimer = new JButton("Imprimer");
        btnImprimer.setBounds(340, 320, 100, 25);
        contentPane.add(btnImprimer);
        btnImprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean complete = table.print();
                    if (complete) {
                        JOptionPane.showMessageDialog(null, "Impression réussie.", "Succès",
                        		JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Impression annulée.", "Annulé",
                        		JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                    		"Erreur lors de l'impression: " + ex.getMessage(), "Erreur",
                    		JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // Charger les données initiales
        afficherDonnees();
    }

    // Méthode pour afficher la fenêtre d'ajout
    private void openAddDialog() {
        JDialog dialog = new JDialog(this, "Ajouter un livre", true);
        dialog.setSize(400, 300);
        dialog.setLayout(null);

        JLabel lblCode = new JLabel("Code:");
        lblCode.setBounds(10, 10, 100, 25);
        dialog.add(lblCode);

        JTextField txtCode = new JTextField();
        txtCode.setBounds(150, 10, 200, 25);
        dialog.add(txtCode);

        JLabel lblTitre = new JLabel("Titre:");
        lblTitre.setBounds(10, 50, 100, 25);
        dialog.add(lblTitre);

        JTextField txtTitre = new JTextField();
        txtTitre.setBounds(150, 50, 200, 25);
        dialog.add(txtTitre);

        JLabel lblAuteur = new JLabel("Auteur:");
        lblAuteur.setBounds(10, 90, 100, 25);
        dialog.add(lblAuteur);

        JTextField txtAuteur = new JTextField();
        txtAuteur.setBounds(150, 90, 200, 25);
        dialog.add(txtAuteur);

        JLabel lblAnnee = new JLabel("Année:");
        lblAnnee.setBounds(10, 130, 100, 25);
        dialog.add(lblAnnee);

        JTextField txtAnnee = new JTextField();
        txtAnnee.setBounds(150, 130, 200, 25);
        dialog.add(txtAnnee);

        JLabel lblPrix = new JLabel("Prix:");
        lblPrix.setBounds(10, 170, 100, 25);
        dialog.add(lblPrix);

        JTextField txtPrix = new JTextField();
        txtPrix.setBounds(150, 170, 200, 25);
        dialog.add(txtPrix);

        JButton btnOk = new JButton("OK");
        btnOk.setBounds(150, 210, 80, 25);
        dialog.add(btnOk);
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String code = txtCode.getText();
                String titre = txtTitre.getText();
                String auteur = txtAuteur.getText();
                int annee = Integer.parseInt(txtAnnee.getText());
                double prix = Double.parseDouble(txtPrix.getText());
                ajouterLigne(code, titre, auteur, annee, prix);
                dialog.dispose();
            }
        });

        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setBounds(240, 210, 80, 25);
        dialog.add(btnAnnuler);
        btnAnnuler.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.setLocationRelativeTo(this);  // Centrer la boîte de dialogue
        dialog.setVisible(true);
    }

    // Méthode pour ajouter une ligne à la base de données
    private void ajouterLigne(String code, String titre, String auteur, int annee, double prix) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO livres (code, titre, auteur, annee, prix) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, code);
            preparedStatement.setString(2, titre);
            preparedStatement.setString(3, auteur);
            preparedStatement.setInt(4, annee);
            preparedStatement.setDouble(5, prix);
            preparedStatement.executeUpdate();
            afficherDonnees(); // Rafraîchir les données
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour modifier une ligne
    private void modifierLigne() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.");
            return;
        }

        // Récupérer les données actuelles de la ligne sélectionnée
        String code = model.getValueAt(selectedRow, 0).toString();
        String titre = model.getValueAt(selectedRow, 1).toString();
        String auteur = model.getValueAt(selectedRow, 2).toString();
        int annee = Integer.parseInt(model.getValueAt(selectedRow, 3).toString());
        double prix = Double.parseDouble(model.getValueAt(selectedRow, 4).toString());

        // Créer une boîte de dialogue pour la modification
        JDialog dialog = new JDialog(this, "Modifier le livre", true);
        dialog.setSize(400, 300);
        dialog.setLayout(null);

        // Code (non modifiable)
        JLabel lblCode = new JLabel("Code:");
        lblCode.setBounds(10, 10, 100, 25);
        dialog.add(lblCode);

        JTextField txtCode = new JTextField(code);
        txtCode.setBounds(150, 10, 200, 25);
        txtCode.setEditable(false);  // Le code ne peut pas être modifié
        dialog.add(txtCode);

        // Titre
        JLabel lblTitre = new JLabel("Titre:");
        lblTitre.setBounds(10, 50, 100, 25);
        dialog.add(lblTitre);

        JTextField txtTitre = new JTextField(titre);
        txtTitre.setBounds(150, 50, 200, 25);
        dialog.add(txtTitre);

        // Auteur
        JLabel lblAuteur = new JLabel("Auteur:");
        lblAuteur.setBounds(10, 90, 100, 25);
        dialog.add(lblAuteur);

        JTextField txtAuteur = new JTextField(auteur);
        txtAuteur.setBounds(150, 90, 200, 25);
        dialog.add(txtAuteur);

        // Année
        JLabel lblAnnee = new JLabel("Année:");
        lblAnnee.setBounds(10, 130, 100, 25);
        dialog.add(lblAnnee);

        JTextField txtAnnee = new JTextField(String.valueOf(annee));
        txtAnnee.setBounds(150, 130, 200, 25);
        dialog.add(txtAnnee);

        // Prix
        JLabel lblPrix = new JLabel("Prix:");
        lblPrix.setBounds(10, 170, 100, 25);
        dialog.add(lblPrix);

        JTextField txtPrix = new JTextField(String.valueOf(prix));
        txtPrix.setBounds(150, 170, 200, 25);
        dialog.add(txtPrix);

        // Bouton OK pour sauvegarder les modifications
        JButton btnOk = new JButton("OK");
        btnOk.setBounds(150, 210, 80, 25);
        dialog.add(btnOk);
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nouveauTitre = txtTitre.getText();
                String nouveauAuteur = txtAuteur.getText();
                int nouvelleAnnee = Integer.parseInt(txtAnnee.getText());
                double nouveauPrix = Double.parseDouble(txtPrix.getText());

                // Mise à jour de la base de données
                try (Connection connection = DataBaseConnection.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(
                             "UPDATE livres SET titre=?, auteur=?, annee=?, prix=? WHERE code=?")) {
                    preparedStatement.setString(1, nouveauTitre);
                    preparedStatement.setString(2, nouveauAuteur);
                    preparedStatement.setInt(3, nouvelleAnnee);
                    preparedStatement.setDouble(4, nouveauPrix);
                    preparedStatement.setString(5, code);
                    preparedStatement.executeUpdate();
                    afficherDonnees(); // Rafraîchir les données après la modification
                    dialog.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Bouton Annuler pour fermer la fenêtre sans sauvegarder
        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setBounds(240, 210, 80, 25);
        dialog.add(btnAnnuler);
        btnAnnuler.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.setLocationRelativeTo(this);  // Centrer la boîte de dialogue
        dialog.setVisible(true);
    }


    // Méthode pour supprimer une ligne
    private void supprimerLigne() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.");
            return;
        }

        String code = model.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous supprimer ce livre ?",
        		"Confirmation", JOptionPane.OK_CANCEL_OPTION);

        if (confirm == JOptionPane.OK_OPTION) {
            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE "
                 		+ "FROM livres WHERE code=?")) {
                preparedStatement.setString(1, code);
                preparedStatement.executeUpdate();
                afficherDonnees(); // Rafraîchir les données
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour afficher les données de la base dans le tableau
    private void afficherDonnees() {
        model.setRowCount(0); // Vider le modèle de table

        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM livres")) {

            while (resultSet.next()) {
                String code = resultSet.getString("code");
                String titre = resultSet.getString("titre");
                String auteur = resultSet.getString("auteur");
                int annee = resultSet.getInt("annee");
                double prix = resultSet.getDouble("prix");
                model.addRow(new Object[]{code, titre, auteur, annee, prix});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
