package Biblio;

import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        // Lancer l'interface de connexion
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Aframe frame = new Aframe();  // Crée et affiche l'interface de connexion
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

