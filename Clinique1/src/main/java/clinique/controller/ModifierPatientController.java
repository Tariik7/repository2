package main.java.clinique.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.clinique.dao.PatientDAO;
import main.java.clinique.model.Patient;

import java.sql.Date;

public class ModifierPatientController {

    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private DatePicker dpNaissance;
    @FXML private TextField tfSexe;
    @FXML private TextField tfTelephone;
    @FXML private TextField tfEmail;
    @FXML private TextField tfAdresse;
    @FXML private Label lblErreur;

    private Patient patient;
    private final PatientDAO patientDAO = new PatientDAO();

    public void setPatient(Patient patient) {
        this.patient = patient;
        // Pré-remplir le formulaire
        tfNom.setText(patient.getNom());
        tfPrenom.setText(patient.getPrenom());
        dpNaissance.setValue(patient.getDateNaissance().toLocalDate());
        tfSexe.setText(String.valueOf(patient.getSexe()));
        tfTelephone.setText(patient.getTelephone());
        tfEmail.setText(patient.getEmail());
        tfAdresse.setText(patient.getAdresse());
    }

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

            patient.setNom(nom);
            patient.setPrenom(prenom);
            patient.setDateNaissance(Date.valueOf(dpNaissance.getValue()));
            patient.setSexe(sexe.charAt(0));
            patient.setTelephone(telephone);
            patient.setEmail(email);
            patient.setAdresse(adresse);

            patientDAO.modifierPatient(patient);

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
