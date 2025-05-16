package main.java.clinique.controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.java.clinique.dao.MedecinDAO;
import main.java.clinique.dao.UtilisateurDAO;
import main.java.clinique.model.Medecin;

public class ListeMedecinsController {

    @FXML private TableView<Medecin> tableMedecins;
    @FXML private TableColumn<Medecin, String> colNom;
    @FXML private TableColumn<Medecin, String> colPrenom;
    @FXML private TableColumn<Medecin, String> colEmail;
    @FXML private TableColumn<Medecin, String> colSpecialite;
    @FXML private TableColumn<Medecin, String> colTelephone;
    @FXML private TableColumn<Medecin, Void> colActions;
    
    @FXML private TextField searchField;
    
    private final MedecinDAO medecinDAO = new MedecinDAO();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        colPrenom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrenom()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colSpecialite.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSpecialite()));
        colTelephone.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelephone()));

        ajouterColonneActions();

        chargerMedecins();
    }

    private void chargerMedecins() {
        try {
            ObservableList<Medecin> list = FXCollections.observableArrayList(medecinDAO.listerMedecins());
            tableMedecins.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouterMedecin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ajout_medecin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un médecin");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            chargerMedecins(); // Refresh
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void ajouterColonneActions() {
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnVoir = new Button("Voir");
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            
     

            {
            	
            	   btnVoir.getStyleClass().add("button-view");
            	    btnModifier.getStyleClass().add("button-edit");
            	    btnSupprimer.getStyleClass().add("button-delete"); 
            	    
                btnVoir.setOnAction(e -> {
                    Medecin med = getTableView().getItems().get(getIndex());
                    afficherPopupDetailsMedecin(med);
                });

                btnModifier.setOnAction(e -> {
                    Medecin med = getTableView().getItems().get(getIndex());
                    ouvrirModifierMedecin(med);
                });

                btnSupprimer.setOnAction(e -> {
                    Medecin med = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer ce médecin ?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            try {
                                new UtilisateurDAO().supprimerUtilisateur(med.getId());
                                tableMedecins.getItems().remove(med);
                            } catch (Exception ex) {
                                ex.printStackTrace();
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
        }
        
        		);
    }


    private void afficherPopupDetailsMedecin(Medecin med) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du Médecin");
        alert.setHeaderText(med.getNom() + " " + med.getPrenom());
        alert.setContentText(
            "Spécialité : " + med.getSpecialite() + "\n" +
            "Téléphone : " + med.getTelephone() + "\n" +
            "Email : " + med.getEmail()
        );
        alert.showAndWait();
    }
    
    private void ouvrirModifierMedecin(Medecin medecin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifier_medecin.fxml"));
            Parent root = loader.load();

            ModifierMedecinController controller = loader.getController();
            controller.setMedecin(medecin);

            Stage stage = new Stage();
            stage.setTitle("Modifier Médecin");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            // Après modification, recharge la liste
            chargerMedecins();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ouverture de la fenêtre de modification.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
	private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        try {
            ObservableList<Medecin> all = FXCollections.observableArrayList(medecinDAO.listerMedecins());
            ObservableList<Medecin> filtered = all.filtered(m ->
                m.getNom().toLowerCase().contains(keyword) ||
                m.getPrenom().toLowerCase().contains(keyword) ||
                m.getEmail().toLowerCase().contains(keyword)
            );
            tableMedecins.setItems(filtered);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        chargerMedecins();
    }
    
    }



