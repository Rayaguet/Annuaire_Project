import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class Infosuserforadmin extends JFrame {
    final private Font mainFont = new Font("Times New Roman", Font.BOLD, 18);

    // Constructeur qui prend une liste d'utilisateurs comme paramètre
    public Infosuserforadmin(List<User> users) {
        setTitle("User Info");
        setSize(800, 600);
        setMinimumSize(new Dimension(400, 400));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Charger le GIF comme une ImageIcon
        ImageIcon backgroundIcon = new ImageIcon("images/templatesearch.gif"); // Remplacez par votre chemin de GIF

        // Créer un JPanel pour le fond
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dessiner l'image de fond adaptée à la taille du panneau
                g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        for (User user : users) {
            // Parcourir chaque utilisateur pour créer et ajouter leurs informations dans
            // des panneaux séparés
            JPanel userInfoPanel = new JPanel();
            userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
            userInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            userInfoPanel.setBackground(new Color(255, 255, 255, 128));
            userInfoPanel.setOpaque(true);
            userInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(Color.GRAY, 1)));

            userInfoPanel.add(createLabel("Nom: " + user.getNom()));
            userInfoPanel.add(createLabel("Prénom: " + user.getPrenom()));
            userInfoPanel.add(createLabel("Date de naissance: " + user.getDateNaissance()));
            userInfoPanel.add(createLabel("Email: " + user.getEmail()));
            userInfoPanel.add(createLabel("Profil: " + user.getProfil()));
            if (user.getNoRue() != null) {
                userInfoPanel.add(createLabel("No Rue: " + user.getNoRue()));
            }
            if (user.getRue() != null) {
                userInfoPanel.add(createLabel("Rue: " + user.getRue()));
            }
            if (user.getCodePostal() != null) {
                userInfoPanel.add(createLabel("Code Postal: " + user.getCodePostal()));
            }

            mainPanel.add(userInfoPanel);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        backgroundPanel.add(mainPanel, gbc);

        add(backgroundPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(mainFont);
        return label;
    }

    public static class User {
        private String nom;
        private String prenom;
        private String dateNaissance;
        private String email;
        private String profil;
        private String noRue;
        private String rue;
        private String codePostal;

        public User(String nom, String prenom, String dateNaissance, String email, String profil, String noRue,
                String rue, String codePostal) {
            this.nom = nom;
            this.prenom = prenom;
            this.dateNaissance = dateNaissance;
            this.email = email;
            this.profil = profil;
            this.noRue = noRue;
            this.rue = rue;
            this.codePostal = codePostal;
        }

        public String getNom() {
            return nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public String getDateNaissance() {
            return dateNaissance;
        }

        public String getEmail() {
            return email;
        }

        public String getProfil() {
            return profil;
        }

        public String getNoRue() {
            return noRue;
        }

        public String getRue() {
            return rue;
        }

        public String getCodePostal() {
            return codePostal;
        }
    }
}
