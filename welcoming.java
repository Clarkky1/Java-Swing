import java.awt.Color;
import java.awt.Font;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class welcoming {

    private JFrame frame;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    String username = "Clark"; 
                    welcoming window = new welcoming(username);
                    window.setVisible(true); 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public welcoming(String username) {
        initialize(username);
    }

    private void initialize(String username) {
        frame = new JFrame();
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setBounds(100, 100, 882, 582);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
		panel.setBackground(new Color(128, 0, 0));
		panel.setBounds(0, 0, 166, 543);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(10, 56, 151, 150);
		panel.add(lblNewLabel);
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\User\\eclipse-workspace\\SAMS\\src\\img\\Group 1.png"));
		
		JButton btnNewButton = new JButton("Home");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Home window = new Home();
							window.frame.setVisible(true);
							frame.dispose();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnNewButton.setBounds(27, 259, 117, 23);
		panel.add(btnNewButton);
		
		JButton btnDashboard = new JButton("Dashboard");
		btnDashboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Dashboard window = new Dashboard();
							window.frame.setVisible(true);
							frame.dispose();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnDashboard.setBounds(27, 309, 117, 23);
		panel.add(btnDashboard);
		
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
		            public void run() {
		                try {
		                    login window = new login();
		                    window.frame.setVisible(true);
		                    frame.dispose();
		                } catch (Exception e) {
		                    e.printStackTrace();
		                    
		                }
		            }
		        });
			}
		});
		btnLogOut.setBounds(27, 354, 117, 23);
		panel.add(btnLogOut);
		
        
        JLabel welcomeLabel = new JLabel("Welcome " + username + " to the SAMS!");
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(139, 0, 0));
        welcomeLabel.setHorizontalAlignment(JLabel.RIGHT);
        welcomeLabel.setBounds(151, 229, 520, 50); 
        frame.getContentPane().add(welcomeLabel);
        
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(128, 0, 0));
        panel_1.setBounds(245, 273, 438, 5);
        frame.getContentPane().add(panel_1);
    }

    public void setVisible(boolean b) {
        frame.setVisible(b); 
    }
}