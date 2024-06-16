import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.geom.RoundRectangle2D;
import src.database;

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("Times New Roman", Font.BOLD, 18);
    RoundedTextField tfUsername;
    RoundedPasswordField pfPassword;
    RoundedButton btnLogin;
    JLabel lbIcon1, lbIcon2;
    Timer timer;
    int xOffset = 0;
    final int ANIMATION_STEPS = 50;
    final int ANIMATION_DELAY = 18;

    public void initialize() {
        /*************** Form Panel ***************/
        lbIcon1 = new JLabel();
        lbIcon1.setHorizontalAlignment(JLabel.CENTER);
        lbIcon1.setIcon(new ImageIcon(
                new ImageIcon("images/logouser1.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

        lbIcon2 = new JLabel();
        lbIcon2.setHorizontalAlignment(JLabel.CENTER);
        lbIcon2.setIcon(new ImageIcon(
                new ImageIcon("images/logouser2.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

        Color semiTransparentColor = new Color(255, 255, 255, 128);

        tfUsername = new RoundedTextField("Username", 20, 20, semiTransparentColor);
        tfUsername.setFont(mainFont);
        tfUsername.setPreferredSize(new Dimension(300, 40));
        tfUsername.setHorizontalAlignment(JTextField.CENTER);
        tfUsername.setForeground(Color.GRAY);
        tfUsername.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tfUsername.getText().equals("Username")) {
                    tfUsername.setText("");
                    tfUsername.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tfUsername.getText().isEmpty()) {
                    tfUsername.setForeground(Color.GRAY);
                    tfUsername.setText("Username");
                }
            }
        });

        pfPassword = new RoundedPasswordField("Password", 20, 20, semiTransparentColor);
        pfPassword.setFont(mainFont);
        pfPassword.setPreferredSize(new Dimension(300, 40));
        pfPassword.setHorizontalAlignment(JTextField.CENTER);
        pfPassword.setForeground(Color.GRAY);
        pfPassword.setEchoChar((char) 0); // Afficher le texte par défaut
        pfPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(pfPassword.getPassword()).equals("Password")) {
                    pfPassword.setText("");
                    pfPassword.setEchoChar('\u2022');
                    pfPassword.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(pfPassword.getPassword()).isEmpty()) {
                    pfPassword.setForeground(Color.GRAY);
                    pfPassword.setText("Password");
                    pfPassword.setEchoChar((char) 0);
                }
            }
        });

        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                lbIcon2.setLocation(lbIcon1.getX() + xOffset, lbIcon1.getY());
            }
        };
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lbIcon1, gbc);
        formPanel.add(lbIcon2, gbc); // Add the second icon on top of the first
        gbc.gridy++;
        formPanel.add(tfUsername, gbc);
        gbc.gridy++;
        formPanel.add(pfPassword, gbc);

        /*************** Buttons Panel ***************/
        btnLogin = new RoundedButton("LOGIN", 20, 20, semiTransparentColor);
        btnLogin.setFont(mainFont);
        btnLogin.setForeground(Color.GRAY); // Same text color as the text fields
        btnLogin.setPreferredSize(new Dimension(150, 40));
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tfUsername.getText();
                String password = new String(pfPassword.getPassword());

                database db = new database();
                int userId = db.getUserId(username, password); // Récupérer l'ID de l'utilisateur
                int userType = db.getUserType(username, password);

                if (userType != -1) {
                    if (userType == 1) {
                        new SearchPageUser(userId); // Passer l'ID de l'utilisateur
                    } else if (userType == 2) {
                        new SearchPageAdmin(userId); // Passer l'ID de l'utilisateur
                    }
                    dispose();
                } else {
                    // Show error message if login fails
                    JOptionPane.showMessageDialog(null, "Login ou mot de passe incorrect", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
                db.close();
            }
        });

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        gbc.gridy++;
        buttonsPanel.add(btnLogin, gbc);

        /*************** Main Panel ***************/
        // Load the GIF as an ImageIcon
        ImageIcon backgroundIcon = new ImageIcon("images/templateconnect.gif");

        // Create a JLabel with the ImageIcon
        JLabel backgroundLabel = new JLabel(backgroundIcon) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
            }
        };

        // Panel to overlay components on top of the GIF
        JPanel overlayPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Redraw the background GIF to fill the panel
                g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        overlayPanel.setOpaque(false);

        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        overlayPanel.add(formPanel, gbc);
        gbc.gridy++;
        overlayPanel.add(buttonsPanel, gbc);

        // Set layout and add the overlay panel to the frame
        setLayout(new BorderLayout());
        add(backgroundLabel, BorderLayout.CENTER);
        add(overlayPanel, BorderLayout.CENTER);

        // Animation Timer
        timer = new Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xOffset += 2;
                if (xOffset >= 50) { // Adjust the offset limit as needed
                    timer.stop();
                }
                formPanel.repaint();
            }
        });
        timer.start();

        setTitle("Welcome");
        setSize(500, 600);
        setMinimumSize(new Dimension(300, 400));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Custom JTextField with rounded corners and no visible border
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

    // Custom JPasswordField with rounded corners and no visible border
    class RoundedPasswordField extends JPasswordField {
        private Shape shape;
        private int arcWidth;
        private int arcHeight;
        private Color backgroundColor;

        public RoundedPasswordField(String text, int arcWidth, int arcHeight, Color backgroundColor) {
            super(text);
            this.arcWidth = arcWidth;
            this.arcHeight = arcHeight;
            this.backgroundColor = backgroundColor;
            setOpaque(false);
            setBorder(new EmptyBorder(5, 10, 5, 10)); // padding inside the password field
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

    // Custom JButton with rounded corners, shadow, and no visible border
    class RoundedButton extends JButton {
        private Shape shape;
        private int arcWidth;
        private int arcHeight;
        private Color backgroundColor;
        private int shadowSize = 5; // size of the shadow

        // Animation properties
        private boolean isPressed = false;
        private int pressedOffset = 5; // offset when pressed
        private Timer pressTimer;
        private int animationDuration = 100; // duration in milliseconds
        private int animationSteps = 5; // number of steps in the animation

        public RoundedButton(String text, int arcWidth, int arcHeight, Color backgroundColor) {
            super(text);
            this.arcWidth = arcWidth;
            this.arcHeight = arcHeight;
            this.backgroundColor = backgroundColor;
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false); // Disable focus painting
            setBorderPainted(false); // Disable border painting
            setBorder(new EmptyBorder(5, 15, 5, 15)); // padding inside the button

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    startPressAnimation();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
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
                    if (step >= animationSteps) {
                        ((Timer) e.getSource()).stop();
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
                    if (step >= animationSteps) {
                        ((Timer) e.getSource()).stop();
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

            int offset = isPressed ? pressedOffset : 0;

            // Draw shadow
            g2.setColor(new Color(0, 0, 0, 64)); // semi-transparent black for the shadow
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);

            // Draw button
            g2.setColor(backgroundColor);
            g2.fillRoundRect(offset, offset, getWidth() - 1 - offset, getHeight() - 1 - offset, arcWidth, arcHeight);
            super.paintComponent(g2);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame myFrame = new MainFrame();
            myFrame.initialize();
        });
    }
}
