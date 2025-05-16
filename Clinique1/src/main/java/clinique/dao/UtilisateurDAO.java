package main.java.clinique.dao;

import main.java.clinique.model.Medecin;
import main.java.clinique.model.Utilisateur;

import java.sql.*;

public class UtilisateurDAO {

	public int creerUtilisateur(String login, String motDePasse, String role) throws SQLException {
	    String sql = "INSERT INTO Utilisateur (login, mot_de_passe, role) VALUES (?, ?, ?)";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        stmt.setString(1, login);
	        stmt.setString(2, motDePasse); // ⚠️ Pense au hashage en prod
	        stmt.setString(3, role);
	        stmt.executeUpdate();

	        ResultSet rs = stmt.getGeneratedKeys();
	        if (rs.next()) {
	            return rs.getInt(1); // retourne l'id_utilisateur généré
	        } else {
	            throw new SQLException("Échec de la création de l'utilisateur, pas de clé générée.");
	        }
	    }
	}


    public Utilisateur trouverParLoginEtMotDePasse(String login, String motDePasse) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE login = ? AND mot_de_passe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, motDePasse);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("login"),
                        rs.getString("mot_de_passe"),
                        rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    public void supprimerUtilisateur(int id) throws SQLException {
        String sql = "DELETE FROM Utilisateur WHERE id_utilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierUtilisateurDepuisMedecin(Medecin medecin) throws SQLException {
        String sql = "UPDATE utilisateur SET login=? WHERE id_utilisateur=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medecin.getEmail());
            stmt.setInt(2, medecin.getId());  // Assumes id_medecin = id_utilisateur
            stmt.executeUpdate();
        }
    }


    
    public Utilisateur getById(int id) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE id_utilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("login"),
                        rs.getString("mot_de_passe"),
                        rs.getString("role")
                    );
                }
            }
        }
        return null;
    }
}
