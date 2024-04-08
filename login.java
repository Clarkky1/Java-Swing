import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

import javax.swing.*;

public class login {

    public JFrame frame;
    private JTextField username;
    private JPasswordField password;
    private JLabel lblStatus;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    login window = new login();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public login() {
        initialize();
        frame.setLocationRelativeTo(null);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Administrator\\Pictures\\Group 1.png"));
        frame.getContentPane().setBackground(new Color(128, 0, 0));
        frame.getContentPane().setLayout(null);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\User\\eclipse-workspace\\SAMS\\src\\img\\Group 1.png"));
        lblNewLabel.setBounds(314, 88, 151, 168);
        frame.getContentPane().add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("STUDENT ATTENDANCE MANAGEMENT SYSTEM");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNewLabel_1.setForeground(new Color(255, 255, 255));
        lblNewLabel_1.setBounds(192, 271, 410, 14);
        frame.getContentPane().add(lblNewLabel_1);
        
        username = new JTextField();
        username.setBounds(276, 311, 219, 20);
        frame.getContentPane().add(username);
        username.setColumns(10);
        
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblUsername.setForeground(new Color(255, 255, 255));
        lblUsername.setBounds(276, 296, 88, 14);
        frame.getContentPane().add(lblUsername);
        
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblPassword.setBounds(276, 342, 73, 14);
        frame.getContentPane().add(lblPassword);
        
        password = new JPasswordField();
        password.setBounds(276, 357, 219, 19);
        frame.getContentPane().add(password);
        
        JButton btnNewButton = new JButton("Login");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usernameInput = username.getText();
                String passwordInput = new String(password.getPassword());

                if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                   JOptionPane.showMessageDialog(frame, "Please enter all fields");
                    return;
                }

                

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sams", "root", "");

                    String sql = "SELECT * FROM login WHERE username=? AND password=?";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, usernameInput);
                    pstmt.setString(2, passwordInput);

                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                       
                        welcoming wel = new welcoming(usernameInput);
                        wel.setVisible(true);
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    Home window = new Home();
                                    window.frame.setVisible(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        frame.dispose();
                    } else {
                        String signupSql = "SELECT * FROM signup WHERE username=? AND password=?";
                        PreparedStatement signupPstmt = con.prepareStatement(signupSql);
                        signupPstmt.setString(1, usernameInput);
                        signupPstmt.setString(2, passwordInput);

                        ResultSet signupRs = signupPstmt.executeQuery();

                        if (signupRs.next()) {
                            showMessage("Welcome, " + usernameInput + "! You have successfully logged in.");
                            
                            welcoming wel = new welcoming(usernameInput);
                            wel.setVisible(true);
                            frame.dispose(); 
                        } else {
                        	JOptionPane.showMessageDialog(frame, "Invalid username or password. Please try again.");
                            
                        }

                        signupRs.close();
                        signupPstmt.close();
                    }

                    rs.close();
                    pstmt.close();
                    con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame , "An error occurred while processing your request. Please try again later.");
                    ex.printStackTrace();
                }
            }
        });

        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnNewButton.setForeground(new Color(139, 0, 0));
        btnNewButton.setBounds(274, 387, 110, 25);
        frame.getContentPane().add(btnNewButton);
        
        JButton btnSignup = new JButton("");
        btnSignup.setIcon(new ImageIcon("C:\\Users\\User\\eclipse-workspace\\SAMS\\src\\img\\Group 8 (1).png"));
        btnSignup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Signup signupWindow = new Signup();
                signupWindow.main(null);
            }
        });
        btnSignup.setForeground(new Color(139, 0, 0));
        btnSignup.setFont(new Font("Tahoma", Font.BOLD, 8));
        btnSignup.setBounds(329, 435, 117, 32);
        frame.getContentPane().add(btnSignup);
        
        lblStatus = new JLabel("");
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblStatus.setBounds(300, 460, 300, 14);
        frame.getContentPane().add(lblStatus);
        
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        btnClear.setForeground(new Color(0, 0, 0));
        btnClear.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnClear.setBounds(394, 386, 101, 26);
        frame.getContentPane().add(btnClear);
        
        frame.setBounds(100, 100, 780, 582);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void clearFields() {
        username.setText("");
        password.setText("");
        lblStatus.setText("");
    }

    
    private void showMessage(String message) {
        lblStatus.setText(message);
        
       
        int xOffset = 90; 
        
        int x = (frame.getWidth() - lblStatus.getWidth()) / 2 + xOffset;
        lblStatus.setBounds(x, 470, lblStatus.getWidth(), 14);
    }
}
