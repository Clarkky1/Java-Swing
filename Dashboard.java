import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Dashboard {

    public JFrame frame;
    private JTable table;
    private JLabel lblDate;
    private int tableIndex = 0;
    private static DefaultTableModel model;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Dashboard window = new Dashboard(model);
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Dashboard(DefaultTableModel model) { // Modify constructor to accept model
        this.model = model; // Assign the passed model to the class variable
        initialize();
        frame.setLocationRelativeTo(null);
    }

    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setBounds(100, 100, 882, 582);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(128, 0, 0));
        panel.setBounds(0, 0, 166, 543);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setBounds(10, 56, 151, 150);
        panel.add(lblNewLabel);
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Group 1.png"));

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

        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\User\\Desktop\\KinProject\\src\\DashBoardFrame.png"));
        lblNewLabel_1.setBounds(233, 91, 597, 103);
        frame.getContentPane().add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("DASHBOARD");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblNewLabel_2.setBounds(233, 28, 289, 77);
        frame.getContentPane().add(lblNewLabel_2);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(243, 163, 500, 300);
        frame.getContentPane().add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        table.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] { "Student Name", "Attendance" }
        ));

        lblDate = new JLabel();
        lblDate.setBounds(639, 119, 104, 22);
        frame.getContentPane().add(lblDate);

        displayAttendanceData((DefaultTableModel) table.getModel(), ""); // Initial display without date

        // Add delete button
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String studentName = (String) table.getValueAt(selectedRow, 0);
                    try {
                        String url = "jdbc:mysql://localhost:3306/sams";
                        String username = "root";
                        String password = ""; // Replace with your MySQL password
                        Connection conn = DriverManager.getConnection(url, username, password);
                        String deleteSQL = "DELETE FROM attendance WHERE student_name = ?";
                        PreparedStatement pst = conn.prepareStatement(deleteSQL);
                        pst.setString(1, studentName);
                        pst.executeUpdate();
                        pst.close();
                        conn.close();

                        // Remove row from the table
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.removeRow(selectedRow);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        btnDelete.setBounds(753, 163, 89, 23);
        frame.getContentPane().add(btnDelete);
    }

    // Method to display attendance data in the table
    public void displayAttendanceData(DefaultTableModel model, String date) {
        try {
            // Display date in the date panel
            lblDate.setText("Date: " + date);

            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(formatter);

            String url = "jdbc:mysql://localhost:3306/sams";
            String username = "root";
            String password = ""; // Replace with your MySQL password
            Connection conn = DriverManager.getConnection(url, username, password);
            String select = "SELECT student_name, attendance FROM attendance";
            PreparedStatement pst = conn.prepareStatement(select);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String studentName = rs.getString("student_name");
                String attendance = rs.getString("attendance");
                model.addRow(new Object[] { studentName, attendance });
            }
            pst.close();
            rs.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Method to create a new table with the current date and display it at the top
    private void createNewTable() {
        JTable newTable = new JTable();
        newTable.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] { "Student Name", "Attendance" }
        ));
        JScrollPane newTableScrollPane = new JScrollPane(newTable);
        newTableScrollPane.setPreferredSize(new Dimension(500, 300));

        // Display current date above the new table
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        JLabel dateLabel = new JLabel("Date: " + formattedDate);
        dateLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        dateLabel.setBounds(639, 50, 200, 20);

        // Add new table and date label to the beginning of the frame's content pane
        frame.getContentPane().add(dateLabel, 0);
        frame.getContentPane().add(newTableScrollPane, 1);

        // Shift existing components down
        Component[] components = frame.getContentPane().getComponents();
        for (Component component : components) {
            if (component != dateLabel && component != newTableScrollPane && !(component instanceof JPanel)) {
                component.setLocation(component.getX(), component.getY() + 350);
            }
        }
    }
}
