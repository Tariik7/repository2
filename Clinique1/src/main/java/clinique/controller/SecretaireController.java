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
import main.java.clinique.dao.SecretaireDAO;
import main.java.clinique.model.Secretaire;

public class SecretaireController {

    @FXML private TableView<Secretaire> tableSecretaires;
    @FXML private TableColumn<Secretaire, String> colNom;
    @FXML private TableColumn<Secretaire, String> colPrenom;
    @FXML private TableColumn<Secretaire, String> colEmail;
    @FXML private TableColumn<Secretaire, String> colTelephone;
    @FXML private TableColumn<Secretaire, Void> colActions;

    private final SecretaireDAO secretaireDAO = new SecretaireDAO();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        colPrenom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrenom()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colTelephone.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelephone()));

        ajouterColonneActions();
        chargerSecretaires();
    }

    private void chargerSecretaires() {
        try {
            ObservableList<Secretaire> liste = FXCollections.observableArrayList(secretaireDAO.listerSecretaires());
            tableSecretaires.setItems(liste);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors du chargement des secrétaires.");
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
                    Secretaire sec = getTableView().getItems().get(getIndex());
                    afficherDetails(sec);
                });

                btnModifier.setOnAction(e -> {
                    Secretaire sec = getTableView().getItems().get(getIndex());
                    ouvrirModifierSecretaire(sec);
                });

                btnSupprimer.setOnAction(e -> {
                    Secretaire sec = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer ce secrétaire ?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            try {
                                secretaireDAO.supprimerSecretaire(sec.getId());
                                chargerSecretaires();
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

    private void afficherDetails(Secretaire sec) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du Secrétaire");
        alert.setHeaderText(sec.getNom() + " " + sec.getPrenom());
        alert.setContentText(
            "Email : " + sec.getEmail() + "\n" +
            "Téléphone : " + sec.getTelephone()
        );
        alert.showAndWait();
    }

    private void ouvrirModifierSecretaire(Secretaire sec) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifier_secretaire.fxml"));
            Parent root = loader.load();

            ModifierSecretaireController controller = loader.getController();
            controller.setSecretaire(sec);

            Stage stage = new Stage();
            stage.setTitle("Modifier un secrétaire");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            chargerSecretaires();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ouverture du formulaire de modification.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public class ModifierSecretaireController {

        @FXML private TextField tfNom;
        @FXML private TextField tfPrenom;
        @FXML private TextField tfEmail;
        @FXML private TextField tfTelephone;
        @FXML private Label lblErreur;

        private Secretaire secretaire;
        private final SecretaireDAO secretaireDAO = new SecretaireDAO();

        // Méthode appelée depuis SecretaireController pour initialiser le formulaire avec les données du secrétaire
        public void setSecretaire(Secretaire secretaire) {
            this.secretaire = secretaire;
            tfNom.setText(secretaire.getNom());
            tfPrenom.setText(secretaire.getPrenom());
            tfEmail.setText(secretaire.getEmail());
            tfTelephone.setText(secretaire.getTelephone());
            lblErreur.setText("");
        }

        @FXML
        private void handleEnregistrer() {
            try {
                String nom = tfNom.getText().trim();
                String prenom = tfPrenom.getText().trim();
                String email = tfEmail.getText().trim();
                String telephone = tfTelephone.getText().trim();

                if (nom.isEmpty() || prenom.isEmpty()) {
                    lblErreur.setText("Nom et prénom sont obligatoires.");
                    return;
                }

                secretaire.setNom(nom);
                secretaire.setPrenom(prenom);
                secretaire.setEmail(email);
                secretaire.setTelephone(telephone);

                secretaireDAO.modifierSecretaire(secretaire);

                fermerFenetre();

            } catch (Exception e) {
                e.printStackTrace();
                lblErreur.setText("Erreur lors de la modification.");
            }
        }

        @FXML
        private void handleAnnuler() {
            fermerFenetre();
        }

        private void fermerFenetre() {
            Stage stage = (Stage) tfNom.getScene().getWindow();
            stage.close();
        }
    }
}
