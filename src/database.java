package src;

import java.sql.*;

public class database {
    // URL de la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/annuaire_project";
    // Nom d'utilisateur de la base de données
    private static final String USER = "root";
    // Mot de passe de la base de données
    private static final String PASSWORD = "";

    // Objet Connection
    private Connection connection;

    // Constructeur
    public database() {
        try {
            // Charger le pilote JDBC pour MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Établir la connexion à la base de données
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion à la base réussie !");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Erreur de connexion à la base de données.");
            e.printStackTrace();
        }
    }

    // Méthode pour vérifier les informations de connexion
    public boolean isValidUser(String email, String password) {
        String query = "SELECT * FROM Compte WHERE email = ? AND mot_de_passe = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour récupérer le type de compte de l'utilisateur
    public int getUserType(String email, String password) {
        String query = "SELECT id_typeCompte FROM Compte WHERE email = ? AND mot_de_passe = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_typeCompte");
            } else {
                return -1; // Utilisateur non trouvé
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Méthode pour récupérer l'ID de l'utilisateur
    public int getUserId(String email, String password) {
        String query = "SELECT id_compte FROM Compte WHERE email = ? AND mot_de_passe = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_compte");
            } else {
                return -1; // Utilisateur non trouvé
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Méthode pour rechercher un utilisateur par nom, prénom, email ou profil
    public ResultSet searchUser(String searchText) {
        String query = "SELECT c.nom, c.prenom, c.date_naissance, c.email, c.profil, a.no_rue, a.rue, a.code_postal " +
                "FROM Compte c LEFT JOIN Adresse a ON c.id_adresse = a.id_adresse " +
                "WHERE c.nom = ? OR c.prenom = ? OR c.email = ? OR c.profil = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, searchText);
            stmt.setString(2, searchText);
            stmt.setString(3, searchText);
            stmt.setString(4, searchText);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour ajouter un administrateur
    public boolean addAdmin(String nom, String prenom, String dateNaissance, String email, String password) {
        String insertAdminQuery = "INSERT INTO Compte (nom, prenom, date_naissance, email, mot_de_passe, profil, id_typeCompte) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertAdminQuery)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, dateNaissance);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, "Administrateur"); // Set the profil to "Administrateur"
            stmt.setInt(7, 2); // id_typeCompte = 2 for admin
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour ajouter un utilisateur
    public boolean addUser(String nom, String prenom, String dateNaissance, String noRue, String rue, String codePostal,
            String email, String password, String profil) {
        String insertAdresseQuery = "INSERT INTO Adresse (no_rue, rue, code_postal) VALUES (?, ?, ?)";
        String insertUserQuery = "INSERT INTO Compte (nom, prenom, date_naissance, email, mot_de_passe, profil, id_adresse, id_typeCompte) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false); // Start transaction

            // Insert address
            try (PreparedStatement stmtAdresse = connection.prepareStatement(insertAdresseQuery,
                    Statement.RETURN_GENERATED_KEYS)) {
                stmtAdresse.setString(1, noRue);
                stmtAdresse.setString(2, rue);
                stmtAdresse.setString(3, codePostal);
                stmtAdresse.executeUpdate();

                ResultSet generatedKeys = stmtAdresse.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long adresseId = generatedKeys.getLong(1);

                    // Insert user with the address ID
                    try (PreparedStatement stmtUser = connection.prepareStatement(insertUserQuery)) {
                        stmtUser.setString(1, nom);
                        stmtUser.setString(2, prenom);
                        stmtUser.setString(3, dateNaissance);
                        stmtUser.setString(4, email);
                        stmtUser.setString(5, password);
                        stmtUser.setString(6, profil);
                        stmtUser.setLong(7, adresseId);
                        stmtUser.setInt(8, 1); // id_typeCompte = 1 for particulier

                        stmtUser.executeUpdate();
                        connection.commit();
                        return true;
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Restore default commit behavior
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Méthode pour mettre à jour un utilisateur
    public boolean updateUser(int userId, String nom, String prenom, String dateNaissance, String noRue, String rue,
            String codePostal, String email, String password, String profil) {
        String query = "UPDATE Compte SET nom = ?, prenom = ?, date_naissance = ?, email = ?, mot_de_passe = ?, profil = ? WHERE id_compte = ?";
        String queryAddress = "UPDATE Adresse SET no_rue = ?, rue = ?, code_postal = ? WHERE id_adresse = (SELECT id_adresse FROM Compte WHERE id_compte = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query);
                PreparedStatement stmtAddress = connection.prepareStatement(queryAddress)) {
            connection.setAutoCommit(false); // Start transaction

            // Mise à jour de l'adresse
            stmtAddress.setString(1, noRue);
            stmtAddress.setString(2, rue);
            stmtAddress.setString(3, codePostal);
            stmtAddress.setInt(4, userId);
            stmtAddress.executeUpdate();

            // Mise à jour des informations de l'utilisateur
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, dateNaissance);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, profil);
            stmt.setInt(7, userId);

            int rowsAffected = stmt.executeUpdate();
            connection.commit(); // Commit transaction
            return rowsAffected > 0;
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback transaction on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Restore default commit behavior
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour récupérer un utilisateur par son ID
    public ResultSet getUserById(int userId) {
        String query = "SELECT c.nom, c.prenom, c.date_naissance, c.email, c.mot_de_passe, c.profil, a.no_rue, a.rue, a.code_postal "
                +
                "FROM Compte c LEFT JOIN Adresse a ON c.id_adresse = a.id_adresse WHERE c.id_compte = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour fermer la connexion
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode principale pour tester la connexion
    public static void main(String[] args) {
        // Créer une instance de la classe Database
        database db = new database();
        // Test de la méthode isValidUser
        boolean isValid = db.isValidUser("test_email@gmail.com", "test_password");
        System.out.println("Utilisateur valide : " + isValid);
        // Fermer la connexion après utilisation
        db.close();
    }
}
