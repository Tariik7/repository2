package main.java.clinique.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.java.clinique.model.Medecin;

public class DetailsMedecinController {

    @FXML private Label lblNom, lblPrenom, lblSpecialite, lblTelephone, lblEmail;

    public void setMedecin(Medecin med) {
        lblNom.setText("Nom: " + med.getNom());
        lblPrenom.setText("Prénom: " + med.getPrenom());
        lblSpecialite.setText("Spécialité: " + med.getSpecialite());
        lblTelephone.setText("Téléphone: " + med.getTelephone());
        lblEmail.setText("Email: " + med.getEmail());
    }

    @FXML
    private void handleFermer() {
        ((Stage) lblNom.getScene().getWindow()).close();
    }
}
