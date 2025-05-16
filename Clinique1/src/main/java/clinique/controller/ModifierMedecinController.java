package main.java.clinique.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.clinique.dao.MedecinDAO;
import main.java.clinique.dao.UtilisateurDAO;
import main.java.clinique.model.Medecin;

public class ModifierMedecinController {

    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfSpecialite;
    @FXML private TextField tfTelephone;
    @FXML private TextField tfEmail;
    @FXML private Label lblErreur;

    private Medecin medecin;
    private final MedecinDAO medecinDAO = new MedecinDAO();

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
        // Pré-remplir le formulaire
        tfNom.setText(medecin.getNom());
        tfPrenom.setText(medecin.getPrenom());
        tfSpecialite.setText(medecin.getSpecialite());
        tfTelephone.setText(medecin.getTelephone());
        tfEmail.setText(medecin.getEmail());
    }

    @FXML
    private void handleEnregistrer() {
        try {
            String nom = tfNom.getText().trim();
            String prenom = tfPrenom.getText().trim();
            String specialite = tfSpecialite.getText().trim();
            String telephone = tfTelephone.getText().trim();
            String email = tfEmail.getText().trim();

            if (nom.isEmpty() || prenom.isEmpty() || specialite.isEmpty()) {
                lblErreur.setText("Veuillez remplir tous les champs obligatoires.");
                return;
            }

            medecin.setNom(nom);
            medecin.setPrenom(prenom);
            medecin.setSpecialite(specialite);
            medecin.setTelephone(telephone);
            medecin.setEmail(email);

            medecinDAO.modifierMedecin(medecin);
            
            new UtilisateurDAO().modifierUtilisateurDepuisMedecin(medecin);


            ((Stage) tfNom.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            lblErreur.setText("Erreur lors de la modification.");
        }
    }

    @FXML
    private void handleAnnuler() {
        ((Stage) tfNom.getScene().getWindow()).close();
    }
}
