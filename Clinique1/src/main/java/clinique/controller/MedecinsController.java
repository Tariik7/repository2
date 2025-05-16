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
import main.java.clinique.dao.MedecinDAO;
import main.java.clinique.dao.UtilisateurDAO;
import main.java.clinique.model.Medecin;

public class MedecinsController {

    @FXML private TableView<Medecin> tableMedecins;
    @FXML private TableColumn<Medecin, String> colNom;
    @FXML private TableColumn<Medecin, String> colPrenom;
    @FXML private TableColumn<Medecin, String> colEmail;
    @FXML private TableColumn<Medecin, String> colSpecialite;
    @FXML private TableColumn<Medecin, String> colTelephone;
    @FXML private TableColumn<Medecin, Void> colActions;
    @FXML private TextField searchField;

    @FXML private TextField tfNom, tfPrenom, tfSpecialite, tfTelephone, tfEmail;
    @FXML private Label lblErreur;

    private Medecin medecin;
    private final MedecinDAO medecinDAO = new MedecinDAO();

    @FXML
    public void initialize() {
        if (tableMedecins != null) {
            colNom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
            colPrenom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrenom()));
            colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
            colSpecialite.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSpecialite()));
            colTelephone.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelephone()));
            ajouterColonneActions();
            chargerMedecins();
        }
    }

    private void chargerMedecins() {
        try {
            ObservableList<Medecin> list = FXCollections.observableArrayList(medecinDAO.listerMedecins());
            tableMedecins.setItems(list);
        } catch (Exception e) {
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
                    Medecin m = getTableView().getItems().get(getIndex());
                    afficherPopupDetailsMedecin(m);
                });

                btnModifier.setOnAction(e -> {
                    Medecin m = getTableView().getItems().get(getIndex());
                    ouvrirFenetreModification(m);
                });

                btnSupprimer.setOnAction(e -> {
                    Medecin m = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer ce médecin ?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            try {
                                new UtilisateurDAO().supprimerUtilisateur(m.getId());
                                medecinDAO.supprimerMedecin(m.getId());
                                tableMedecins.getItems().remove(m);
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
                    setGraphic(new HBox(5, btnVoir, btnModifier, btnSupprimer));
                }
            }
        });
    }

    private void afficherPopupDetailsMedecin(Medecin m) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du Médecin");
        alert.setHeaderText(m.getNom() + " " + m.getPrenom());
        alert.setContentText("Spécialité : " + m.getSpecialite() + "\nTéléphone : " + m.getTelephone() + "\nEmail : " + m.getEmail());
        alert.showAndWait();
    }

    private void ouvrirFenetreModification(Medecin m) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/medecin/modifier.fxml"));
            Parent root = loader.load();

            MedecinsController controller = loader.getController();
            controller.setMedecin(m);

            Stage stage = new Stage();
            stage.setTitle("Modifier Médecin");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            chargerMedecins();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur d'ouverture de la fenêtre de modification.");
        }
    }

    public void setMedecin(Medecin m) {
        this.medecin = m;
        if (tfNom != null) {
            tfNom.setText(m.getNom());
            tfPrenom.setText(m.getPrenom());
            tfSpecialite.setText(m.getSpecialite());
            tfTelephone.setText(m.getTelephone());
            tfEmail.setText(m.getEmail());
        }
    }

    @FXML
    private void handleEnregistrer() {
        try {
            String nom = tfNom.getText().trim();
            String prenom = tfPrenom.getText().trim();
            String specialite = tfSpecialite.getText().trim();
            String telephone = tfTelephone.getText().trim();
            String email = tfEmail.getText().trim();

            if (nom.isEmpty() || prenom.isEmpty() || specialite.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
                lblErreur.setText("Veuillez remplir tous les champs.");
                return;
            }

            if (medecin == null) {
                // Ajout
                String login = email;
                String motDePasse = "1234";
                int idUtilisateur = new UtilisateurDAO().creerUtilisateur(login, motDePasse, "medecin");
                medecin = new Medecin(idUtilisateur, nom, prenom, specialite, telephone, email);
                medecinDAO.ajouterMedecinAvecUtilisateur(medecin, idUtilisateur);
            } else {
                // Modification
                medecin.setNom(nom);
                medecin.setPrenom(prenom);
                medecin.setSpecialite(specialite);
                medecin.setTelephone(telephone);
                medecin.setEmail(email);

                medecinDAO.modifierMedecin(medecin);
                new UtilisateurDAO().modifierUtilisateurDepuisMedecin(medecin);
            }

            ((Stage) tfNom.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            lblErreur.setText("Erreur lors de l'enregistrement.");
        }
    }

    @FXML
    private void handleAnnuler() {
        ((Stage) tfNom.getScene().getWindow()).close();
    }

    @FXML
    private void handleAjouterMedecin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/medecin/ajouter.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un médecin");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            chargerMedecins();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
