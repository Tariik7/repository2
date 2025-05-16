package main.java.clinique.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.java.clinique.dao.PatientDAO;
import main.java.clinique.dao.UtilisateurDAO;
import main.java.clinique.model.Patient;

public class ListePatientsController {

    @FXML private TableView<Patient> tablePatients;
    @FXML private TableColumn<Patient, String> colNom;
    @FXML private TableColumn<Patient, String> colPrenom;
    @FXML private TableColumn<Patient, String> colEmail;
    @FXML private TableColumn<Patient, String> colSexe;
    @FXML private TableColumn<Patient, String> colTelephone;
    @FXML private TableColumn<Patient, Void> colActions;

    private final PatientDAO patientDAO = new PatientDAO();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        colPrenom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrenom()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colSexe.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getSexe())));
        colTelephone.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelephone()));

        colActions.setPrefWidth(250);

        tablePatients.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        ajouterColonneActions();

        chargerPatients();
    }

    private void ajouterColonneActions() {
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnVoir = new Button("Voir");
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
 
            {
                btnVoir.getStyleClass().add("button-green");
                btnModifier.getStyleClass().add("button-blue");
                btnSupprimer.getStyleClass().add("button-red");

                btnVoir.setOnAction(e -> {
                    Patient pat = getTableView().getItems().get(getIndex());
                    afficherPopupDetailsPatient(pat);
                });

                btnModifier.setOnAction(e -> {
                    Patient pat = getTableView().getItems().get(getIndex());
                    ouvrirModifierPatient(pat);
                });

                btnSupprimer.setOnAction(e -> {
                    Patient pat = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer ce patient ?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            try {
                                new UtilisateurDAO().supprimerUtilisateur(pat.getId()); // si tu as lié utilisateur et patient par id
                                patientDAO.supprimerPatient(pat.getId());
                                tablePatients.getItems().remove(pat);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                showAlert("Erreur lors de la suppression.");
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, btnVoir, btnModifier, btnSupprimer);
                    setGraphic(box);
                }
            }
        });
    }

    private void afficherPopupDetailsPatient(Patient patient) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du Patient");
        alert.setHeaderText(patient.getNom() + " " + patient.getPrenom());
        alert.setContentText(
            "Sexe : " + patient.getSexe() + "\n" +
            "Téléphone : " + patient.getTelephone() + "\n" +
            "Email : " + patient.getEmail()
        );
        alert.showAndWait();
    }

    private void ouvrirModifierPatient(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifier_patient.fxml"));
            Parent root = loader.load();

            ModifierPatientController controller = loader.getController();
            controller.setPatient(patient);

            Stage stage = new Stage();
            stage.setTitle("Modifier un patient");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            chargerPatients();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ouverture du formulaire de modification.");
        }
    }

    private void chargerPatients() {
        try {
            ObservableList<Patient> list = FXCollections.observableArrayList(patientDAO.listerPatients());
            tablePatients.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur de chargement des patients.");
        }
    }

    @FXML
    private void handleAjouterPatient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ajout_patient.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un patient");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            chargerPatients();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
