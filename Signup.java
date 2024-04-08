import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

public class Signup {

    public JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Signup window = new Signup();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Signup() {
        initialize();
        frame.setLocationRelativeTo(null);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Administrator\\Pictures\\Group 1.png"));
        frame.getContentPane().setBackground(new Color(128, 0, 0));
        frame.getContentPane().setLayout(null);
        
        JLabel lblNewLabel_1 = new JLabel("SIGN UP");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNewLabel_1.setForeground(new Color(255, 255, 255));
        lblNewLabel_1.setBounds(350, 242, 73, 29);
        frame.getContentPane().add(lblNewLabel_1);
        
        usernameField = new JTextField();
        usernameField.setBounds(273, 293, 219, 20);
        frame.getContentPane().add(usernameField);
        usernameField.setColumns(10);
        
        JLabel lblNewLabel_2 = new JLabel("Username");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel_2.setForeground(new Color(255, 255, 255));
        lblNewLabel_2.setBounds(273, 278, 60, 14);
        frame.getContentPane().add(lblNewLabel_2);
        
        JLabel lblNewLabel_2_1 = new JLabel("Password");
        lblNewLabel_2_1.setForeground(Color.WHITE);
        lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel_2_1.setBounds(273, 324, 73, 14);
        frame.getContentPane().add(lblNewLabel_2_1);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(273, 340, 219, 20);
        frame.getContentPane().add(passwordField);
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(273, 387, 219, 20);
        frame.getContentPane().add(confirmPasswordField);
        
        JLabel lblNewLabel_2_1_1 = new JLabel("Confirm Password");
        lblNewLabel_2_1_1.setForeground(Color.WHITE);
        lblNewLabel_2_1_1.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblNewLabel_2_1_1.setBounds(273, 371, 119, 14);
        frame.getContentPane().add(lblNewLabel_2_1_1);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\User\\eclipse-workspace\\SAMS\\src\\img\\Group 1.png"));
        lblNewLabel.setBounds(310, 67, 150, 177);
        frame.getContentPane().add(lblNewLabel);
        
        JButton btnNewButton = new JButton("Signup");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnNewButton.setForeground(new Color(128, 0, 0));
        btnNewButton.setBounds(273, 416, 104, 24);
        frame.getContentPane().add(btnNewButton);
        
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        btnClear.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnClear.setForeground(new Color(0, 0, 0));
        btnClear.setBounds(387, 416, 104, 24);
        frame.getContentPane().add(btnClear);
        
        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                login loginWindow = new login();
                loginWindow.main(null);
            }
        });
        btnLogin.setForeground(new Color(128, 0, 0));
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 10));
        btnLogin.setBounds(350, 449, 63, 20);
        frame.getContentPane().add(btnLogin);

        frame.setBounds(90, 100, 780, 582);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void signUp() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(frame, "Passwords do not match");
            return;
        }

        Connection con = null;
        PreparedStatement checkUsernameStmt = null;
        PreparedStatement signUpStmt = null;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sams", "root", "");

            String checkUsernameQuery = "SELECT * FROM signup WHERE username = ?";
            checkUsernameStmt = con.prepareStatement(checkUsernameQuery);
            checkUsernameStmt.setString(1, username);
            if (checkUsernameStmt.executeQuery().next()) {
                JOptionPane.showMessageDialog(frame, "Username already exists");
                return;
            }

            String signUpQuery = "INSERT INTO signup (username, password, confirm_password) VALUES (?, ?, ?)";
            signUpStmt = con.prepareStatement(signUpQuery);
            signUpStmt.setString(1, username);
            signUpStmt.setString(2, password);
            signUpStmt.setString(3, confirmPassword);

            int rowsInserted = signUpStmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "User signed up successfully");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to sign up user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        } finally {
            try {
                if (checkUsernameStmt != null) {
                    checkUsernameStmt.close();
                }
                if (signUpStmt != null) {
                    signUpStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}