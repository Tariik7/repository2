package main.java.clinique.dao;

import main.java.clinique.model.Secretaire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SecretaireDAO {

    public void ajouterSecretaire(Secretaire secretaire) throws SQLException {
        String sql = "INSERT INTO Secretaire (id_secretaire, nom, prenom, telephone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, secretaire.getId());
            stmt.setString(2, secretaire.getNom());
            stmt.setString(3, secretaire.getPrenom());
            stmt.setString(4, secretaire.getTelephone());
            stmt.setString(5, secretaire.getEmail());
            stmt.executeUpdate();
        }
    }

    public List<Secretaire> listerSecretaires() throws SQLException {
        List<Secretaire> liste = new ArrayList<>();
        String sql = "SELECT * FROM Secretaire";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Secretaire s = new Secretaire(
                    rs.getInt("id_secretaire"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("email")
                );
                liste.add(s);
            }
        }

        return liste;
    }

    public void supprimerSecretaire(int id) throws SQLException {
        String sql = "DELETE FROM Secretaire WHERE id_secretaire = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierSecretaire(Secretaire secretaire) throws SQLException {
        String sql = "UPDATE Secretaire SET nom=?, prenom=?, telephone=?, email=? WHERE id_secretaire=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, secretaire.getNom());
            stmt.setString(2, secretaire.getPrenom());
            stmt.setString(3, secretaire.getTelephone());
            stmt.setString(4, secretaire.getEmail());
            stmt.setInt(5, secretaire.getId());
            stmt.executeUpdate();
        }
    }
}
