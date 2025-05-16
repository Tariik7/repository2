package main.java.clinique.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.clinique.dao.MedecinDAO;
import main.java.clinique.dao.UtilisateurDAO;
import main.java.clinique.model.Medecin;

public class AjoutMedecinController {

    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfSpecialite;
    @FXML private TextField tfTelephone;
    @FXML private TextField tfEmail;
    @FXML private Label lblErreur;

    private final MedecinDAO medecinDAO = new MedecinDAO();

    @FXML
    private void handleEnregistrer() {
        String nom = tfNom.getText().trim();
        String prenom = tfPrenom.getText().trim();
        String specialite = tfSpecialite.getText().trim();
        String telephone = tfTelephone.getText().trim();
        String email = tfEmail.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || specialite.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
            lblErreur.setText("Tous les champs sont obligatoires.");
            return;
        }

        // Ici, on crée un login et un mot de passe par défaut par exemple
        String login = email;  // tu peux changer la logique
        String motDePasse = "1234"; // à changer avec un vrai mot de passe sécurisé

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        MedecinDAO medecinDAO = new MedecinDAO();

        try {
            // Crée l'utilisateur et récupère l'id généré
            int idUtilisateur = utilisateurDAO.creerUtilisateur(login, motDePasse, "medecin");

            // Crée le médecin avec cet id utilisateur
            Medecin m = new Medecin(idUtilisateur, nom, prenom, specialite, telephone, email);
            medecinDAO.ajouterMedecinAvecUtilisateur(m, idUtilisateur);

            // Fermer la fenêtre
            Stage stage = (Stage) tfNom.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            lblErreur.setText("Erreur lors de l'ajout du médecin.");
        }
    }


    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) tfNom.getScene().getWindow();
        stage.close();
    }
}
