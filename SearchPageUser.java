import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.database;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchPageUser extends JFrame {
    final private Font mainFont = new Font("Times New Roman", Font.BOLD, 18);
    RoundedTextField tfSearch;
    private int userId;

    public SearchPageUser(int userId) {
        this.userId = userId;
        setTitle("Search Page");
        setSize(800, 600);
        setMinimumSize(new Dimension(400, 400));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Charger le GIF comme une ImageIcon
        ImageIcon backgroundIcon = new ImageIcon("images/templatesearch.gif");

        // Créer un JPanel pour le fond
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        // Récupérer le nom et le prénom de l'utilisateur à partir de la base de données
        String userName = getUserName(userId);

        // Créer et configurer le label pour afficher le nom et prénom
        JLabel userNameLabel = new JLabel(userName);
        userNameLabel.setFont(mainFont);
        userNameLabel.setForeground(Color.WHITE); // Mettre la couleur du texte en blanc pour qu'il soit lisible

        // Ajouter le label en haut à gauche avec le logo
        ImageIcon userIcon = new ImageIcon("images/userlogo.png"); // Remplacez par votre icône de logo
        JLabel userIconLabel = new JLabel(userIcon);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.setOpaque(false);
        userPanel.add(userIconLabel);
        userPanel.add(userNameLabel);

        // Créer et configurer le champ de recherche
        tfSearch = new RoundedTextField("Search", 20, 20, new Color(255, 255, 255, 128));
        tfSearch.setFont(mainFont);
        tfSearch.setPreferredSize(new Dimension(400, 40));
        tfSearch.setHorizontalAlignment(JTextField.CENTER);
        tfSearch.setForeground(Color.GRAY);
        tfSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tfSearch.getText().equals("Search")) {
                    tfSearch.setText("");
                    tfSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tfSearch.getText().isEmpty()) {
                    tfSearch.setForeground(Color.GRAY);
                    tfSearch.setText("Search");
                }
            }
        });

        // Créer le bouton de recherche avec une icône
        ImageIcon searchIcon = new ImageIcon("images/searchlogo.png");
        if (searchIcon.getIconWidth() == -1) {
            System.out.println("Image non trouvée : images/searchlogo.png");
        }
        RoundedIconButton searchButton = new RoundedIconButton(searchIcon);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = tfSearch.getText();
                database db = new database();

                // Ajouter la recherche par nom ou prénom
                ResultSet rs = db.searchUser(searchText);
                List<InfoUsersforuser.User> users = new ArrayList<>();
                try {
                    while (rs != null && rs.next()) {
                        String noRue = null;
                        String rue = null;
                        String codePostal = null;

                        try {
                            noRue = rs.getString("no_rue");
                        } catch (SQLException ex) {
                            System.out.println("Colonne 'no_rue' non trouvée.");
                        }
                        try {
                            rue = rs.getString("rue");
                        } catch (SQLException ex) {
                            System.out.println("Colonne 'rue' non trouvée.");
                        }
                        try {
                            codePostal = rs.getString("code_postal");
                        } catch (SQLException ex) {
                            System.out.println("Colonne 'code_postal' non trouvée.");
                        }

                        InfoUsersforuser.User user = new InfoUsersforuser.User(
                                rs.getString("nom"), rs.getString("prenom"),
                                rs.getString("date_naissance"),
                                rs.getString("email"), rs.getString("profil"),
                                noRue, rue, codePostal);

                        users.add(user);
                    }

                    if (!users.isEmpty()) {
                        new InfoUsersforuser(users);
                    } else {
                        JOptionPane.showMessageDialog(null, "Aucun utilisateur trouvé.", "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    db.close();
                }
            }
        });

        // Créer un JPanel pour contenir le champ de recherche et le bouton
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.add(tfSearch, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Ajouter le panneau de recherche au panneau de fond
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(searchPanel, gbc);

        // Ajouter la barre de menu
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(userPanel); // Ajouter le panneau utilisateur à gauche

        // Ajouter le bouton de menu déroulant avec la même animation que le bouton de
        // recherche
        ImageIcon menuIcon = new ImageIcon("images/menulogo.png");
        if (menuIcon.getIconWidth() == -1) {
            System.out.println("Image non trouvée : images/menulogo.png");
        }
        RoundedIconButton menuButton = new RoundedIconButton(menuIcon);
        menuButton.setPreferredSize(new Dimension(40, 40));

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem modifyProfile = new JMenuItem("Modifier Profil");
                JMenuItem logout = new JMenuItem("Déconnexion");

                modifyProfile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new ModifyProfile(userId);
                        dispose();
                    }
                });

                logout.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new MainFrame().initialize();
                        dispose();
                    }
                });

                popupMenu.add(modifyProfile);
                popupMenu.add(logout);

                popupMenu.show(menuButton, menuButton.getWidth(), menuButton.getHeight());
            }
        });

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuButton);
        setJMenuBar(menuBar);

        // Définir la disposition et ajouter le panneau de fond au cadre
        setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Méthode pour récupérer le nom et le prénom de l'utilisateur
    private String getUserName(int userId) {
        database db = new database();
        ResultSet rs = db.getUserById(userId);
        String userName = "";
        try {
            if (rs.next()) {
                userName = rs.getString("nom") + " " + rs.getString("prenom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            db.close();
        }
        return userName;
    }

    // JTextField personnalisé avec coins arrondis et bordure invisible
    class RoundedTextField extends JTextField {
        private Shape shape;
        private int arcWidth;
        private int arcHeight;
        private Color backgroundColor;

        public RoundedTextField(String text, int arcWidth, int arcHeight, Color backgroundColor) {
            super(text);
            this.arcWidth = arcWidth;
            this.arcHeight = arcHeight;
            this.backgroundColor = backgroundColor;
            setOpaque(false);
            setBorder(new EmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(backgroundColor);
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            // Ne pas dessiner de bordure pour la rendre invisible
        }

        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
            }
            return shape.contains(x, y);
        }
    }

    // JButton personnalisé avec coins arrondis et aucune bordure visible
    class RoundedIconButton extends JButton {
        private Shape shape;
        private int arcWidth = 20;
        private int arcHeight = 20;

        // Propriétés de l'animation
        private boolean isPressed = false;
        private int pressedOffset = 5;
        private Timer pressTimer;
        private int animationDuration = 100;
        private int animationSteps = 5;
        private double scale = 1.0;
        private double targetScale = 1.0;

        public RoundedIconButton(ImageIcon icon) {
            super(icon);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBorder(new EmptyBorder(5, 5, 5, 5));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    targetScale = 0.9;
                    startPressAnimation();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    targetScale = 1.0;
                    startReleaseAnimation();
                }
            });
        }

        private void startPressAnimation() {
            if (pressTimer != null && pressTimer.isRunning()) {
                pressTimer.stop();
            }

            pressTimer = new Timer(animationDuration / animationSteps, new ActionListener() {
                private int step = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    step++;
                    scale += (targetScale - scale) / animationSteps;
                    if (step >= animationSteps) {
                        ((Timer) e.getSource()).stop();
                        scale = targetScale;
                    }
                    repaint();
                }
            });
            pressTimer.start();
        }

        private void startReleaseAnimation() {
            if (pressTimer != null && pressTimer.isRunning()) {
                pressTimer.stop();
            }

            pressTimer = new Timer(animationDuration / animationSteps, new ActionListener() {
                private int step = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    step++;
                    scale += (targetScale - scale) / animationSteps;
                    if (step >= animationSteps) {
                        ((Timer) e.getSource()).stop();
                        scale = targetScale;
                    }
                    repaint();
                }
            });
            pressTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = (int) (getWidth() * scale);
            int height = (int) (getHeight() * scale);

            g2.translate((getWidth() - width) / 2, (getHeight() - height) / 2);
            g2.scale(scale, scale);
            super.paintComponent(g2);
            g2.scale(1.0 / scale, 1.0 / scale);
            g2.translate(-(getWidth() - width) / 2, -(getHeight() - height) / 2);
        }

        @Override
        protected void paintBorder(Graphics g) {
            // Ne pas dessiner de bordure pour la rendre invisible
        }

        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
            }
            return shape.contains(x, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SearchPageUser(1);
            }
        });
    }
}
