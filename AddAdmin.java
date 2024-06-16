import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.database;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class AddAdmin extends JFrame {

    final private Font mainFont = new Font("Times New Roman", Font.BOLD, 18);
    final private Dimension textFieldSize = new Dimension(300, 40);
    final private int arcWidth = 20;
    final private int arcHeight = 20;
    final private Color semiTransparentColor = new Color(255, 255, 255, 128);
    final private Color lightGrayColor = new Color(211, 211, 211, 128); // Gris clair transparent
    private int userId; // Field to store the userId

    public AddAdmin(int userId) {
        this.userId = userId; // Set the userId
        initialize();
    }

    private JTextField textFieldNom;
    private JTextField textFieldPrenom;
    private JTextField textFieldDateNaissance;
    private JTextField textFieldEmail;
    private JTextField textFieldPassWord;

    private void initialize() {
        setTitle("Ajouter un administrateur");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600); // Augmenter la taille pour mieux s'adapter
        setLocationRelativeTo(null);

        // Charger le GIF comme une ImageIcon
        ImageIcon backgroundIcon = new ImageIcon("images/addusertemplate3.gif"); // Remplacez par votre chemin de GIF

        // Créer un JPanel pour le fond
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Création l'image de fond adaptée à la taille du panneau
                g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);

        // Remplacez le texte du bouton "Accueil" par une icône avec animation
        ImageIcon homeIcon = new ImageIcon("images/menulogo.png"); // Remplacez par votre chemin d'icône
        RoundedIconButton accueil = new RoundedIconButton(homeIcon);
        accueil.setToolTipText("Accueil");
        accueil.setBorderPainted(false);
        accueil.setContentAreaFilled(false);
        accueil.setFocusPainted(false);

        accueil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvrir la page SearchPageAdmin et fermer AddAdmin
                new SearchPageAdmin(userId);
                dispose();
            }
        });

        menuBar.add(accueil); // Ajouter le bouton à gauche
        menuBar.add(Box.createHorizontalGlue()); // Ajouter un espace flexible pour pousser les éléments suivants à
                                                 // droite
        setJMenuBar(menuBar);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        textFieldNom = createPlaceholderTextField("Nom", lightGrayColor);
        backgroundPanel.add(textFieldNom, gbc);
        gbc.gridy++;

        textFieldPrenom = createPlaceholderTextField("Prenom", lightGrayColor);
        backgroundPanel.add(textFieldPrenom, gbc);
        gbc.gridy++;

        textFieldDateNaissance = createPlaceholderTextField("Date de naissance", lightGrayColor);
        backgroundPanel.add(textFieldDateNaissance, gbc);
        gbc.gridy++;

        textFieldEmail = createPlaceholderTextField("Email", lightGrayColor);
        backgroundPanel.add(textFieldEmail, gbc);
        gbc.gridy++;

        textFieldPassWord = createPlaceholderTextField("Mot de passe", lightGrayColor);
        backgroundPanel.add(textFieldPassWord, gbc);
        gbc.gridy++;

        JLabel profileLabel = new JLabel("Votre profil est automatiquement défini sur Administrateur");
        profileLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridy++;
        backgroundPanel.add(profileLabel, gbc);

        RoundedButton boutonValider = new RoundedButton("Valider", arcWidth, arcHeight, lightGrayColor);
        boutonValider.setFont(mainFont);
        boutonValider.setForeground(Color.GRAY); // Même couleur de texte que les champs de texte
        boutonValider.setPreferredSize(new Dimension(150, 40));
        boutonValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupérer les valeurs des champs de texte
                String nom = textFieldNom.getText();
                String prenom = textFieldPrenom.getText();
                String dateNaissance = textFieldDateNaissance.getText();
                String email = textFieldEmail.getText();
                String password = textFieldPassWord.getText();

                // Ajouter l'administrateur dans la base de données
                database db = new database();
                boolean isSuccess = db.addAdmin(nom, prenom, dateNaissance, email, password);
                db.close();

                if (isSuccess) {
                    JOptionPane.showMessageDialog(null, "Administrateur ajouté avec succès.");
                    new SearchPageAdmin(userId);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de l'administrateur.", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc.gridy++;
        backgroundPanel.add(boutonValider, gbc);

        add(backgroundPanel);
        setVisible(true);
    }

    private JTextField createPlaceholderTextField(String placeholder, Color backgroundColor) {
        JTextField textField = new RoundedTextField(placeholder, arcWidth, arcHeight, backgroundColor);
        textField.setPreferredSize(textFieldSize);
        return textField;
    }

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
            setBorder(new EmptyBorder(5, 10, 5, 10)); // padding inside the text field
            setFont(mainFont);
            setHorizontalAlignment(JTextField.CENTER);
            setForeground(Color.GRAY);
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals(text)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setForeground(Color.GRAY);
                        setText(text);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(backgroundColor);
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            // Do not paint border to make it invisible
        }

        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
            }
            return shape.contains(x, y);
        }
    }

    class RoundedButton extends JButton {
        private Shape shape;
        private int arcWidth;
        private int arcHeight;
        private Color backgroundColor;
        private int shadowSize = 5;

        private boolean isPressed = false;
        private int pressedOffset = 5;
        private Timer pressTimer;
        private int animationDuration = 100;
        private int animationSteps = 5;
        private double scale = 1.0;
        private double targetScale = 1.0;

        public RoundedButton(String text, int arcWidth, int arcHeight, Color backgroundColor) {
            super(text);
            this.arcWidth = arcWidth;
            this.arcHeight = arcHeight;
            this.backgroundColor = backgroundColor;
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBorder(new EmptyBorder(5, 15, 5, 15));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    targetScale = 0.9; // Zoom avant
                    startPressAnimation();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    targetScale = 1.0; // Zoom arrière
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

            // Dessiner le bouton
            g2.translate((getWidth() - width) / 2, (getHeight() - height) / 2);
            g2.scale(scale, scale);
            super.paintComponent(g2);
            g2.scale(1.0 / scale, 1.0 / scale);
            g2.translate(-(getWidth() - width) / 2, -(getHeight() - height) / 2);
        }

        @Override
        protected void paintBorder(Graphics g) {
            // Do not paint border to make it invisible
        }

        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
            }
            return shape.contains(x, y);
        }
    }

    class RoundedIconButton extends JButton {
        private Shape shape;
        private int arcWidth = 20;
        private int arcHeight = 20;

        // Propriétés de l'animation
        private boolean isPressed = false;
        private int pressedOffset = 5; // Déplacement lors de l'appui
        private Timer pressTimer;
        private int animationDuration = 100; // Durée en millisecondes
        private int animationSteps = 5; // Nombre d'étapes dans l'animation
        private double scale = 1.0;
        private double targetScale = 1.0;

        public RoundedIconButton(ImageIcon icon) {
            super(icon);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false); // Désactiver la peinture du focus
            setBorderPainted(false); // Désactiver la peinture de la bordure
            setBorder(new EmptyBorder(5, 5, 5, 5)); // Padding à l'intérieur du bouton

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    targetScale = 0.9; // Zoom avant
                    startPressAnimation();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    targetScale = 1.0; // Zoom arrière
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

            // Dessiner le bouton
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
        SwingUtilities.invokeLater(() -> new AddAdmin(1));
    }
}
