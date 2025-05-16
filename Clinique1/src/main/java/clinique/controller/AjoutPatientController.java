package main.java.clinique.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.clinique.dao.PatientDAO;
import main.java.clinique.model.Patient;

public class AjoutPatientController {

    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private DatePicker dpNaissance;
    @FXML private TextField tfSexe;
    @FXML private TextField tfTelephone;
    @FXML private TextField tfEmail;
    @FXML private TextField tfAdresse;
    @FXML private Label lblErreur;

    private final PatientDAO patientDAO = new PatientDAO();

    @FXML
    private void handleEnregistrer() {
        try {
            String nom = tfNom.getText().trim();
            String prenom = tfPrenom.getText().trim();
            String sexe = tfSexe.getText().trim().toUpperCase();
            String telephone = tfTelephone.getText().trim();
            String email = tfEmail.getText().trim();
            String adresse = tfAdresse.getText().trim();

            if (nom.isEmpty() || prenom.isEmpty() || dpNaissance.getValue() == null || sexe.isEmpty()) {
                lblErreur.setText("Veuillez remplir tous les champs obligatoires.");
                return;
            }

            Patient p = new Patient(0, nom, prenom, dpNaissance.getValue(), sexe.charAt(0), telephone, email, adresse);
            patientDAO.ajouterPatient(p);

            ((Stage) tfNom.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            lblErreur.setText("Erreur lors de l'ajout du patient.");
        }
    }

    @FXML
    private void handleAnnuler() {
        ((Stage) tfNom.getScene().getWindow()).close();
    }
}
