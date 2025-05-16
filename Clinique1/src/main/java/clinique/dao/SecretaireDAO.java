package main.java.clinique.dao;

import main.java.clinique.model.Secretaire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SecretaireDAO {

    private Connection getConnection() throws SQLException {
        // Implémente ta méthode de connexion à la base
        return DBConnection.getConnection(); // Exemple, adapte selon ton code
    }

    public List<Secretaire> listerSecretaires() throws SQLException {
        List<Secretaire> liste = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, email, telephone FROM secretaire";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Secretaire s = new Secretaire();
                s.setId(rs.getInt("id"));
                s.setNom(rs.getString("nom"));
                s.setPrenom(rs.getString("prenom"));
                s.setEmail(rs.getString("email"));
                s.setTelephone(rs.getString("telephone"));
                liste.add(s);
            }
        }
        return liste;
    }

    public void ajouterSecretaire(Secretaire s) throws SQLException {
        String sql = "INSERT INTO secretaire (nom, prenom, email, telephone) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getNom());
            stmt.setString(2, s.getPrenom());
            stmt.setString(3, s.getEmail());
            stmt.setString(4, s.getTelephone());
            stmt.executeUpdate();
        }
    }

    public void modifierSecretaire(Secretaire s) throws SQLException {
        String sql = "UPDATE secretaire SET nom=?, prenom=?, email=?, telephone=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getNom());
            stmt.setString(2, s.getPrenom());
            stmt.setString(3, s.getEmail());
            stmt.setString(4, s.getTelephone());
            stmt.setInt(5, s.getId());
            stmt.executeUpdate();
        }
    }

    public void supprimerSecretaire(int id) throws SQLException {
        String sql = "DELETE FROM secretaire WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Secretaire trouverParId(int id) throws SQLException {
        Secretaire s = null;
        String sql = "SELECT id, nom, prenom, email, telephone FROM secretaire WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    s = new Secretaire();
                    s.setId(rs.getInt("id"));
                    s.setNom(rs.getString("nom"));
                    s.setPrenom(rs.getString("prenom"));
                    s.setEmail(rs.getString("email"));
                    s.setTelephone(rs.getString("telephone"));
                }
            }
        }
        return s;
    }
}
