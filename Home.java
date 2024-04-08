import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Home {

    public JFrame frame;
    private JTable table;
    private final JButton btnNewButton_3 = new JButton("Delete Students");
    private int lastAddedRowIndex = -1;
    private DefaultTableModel model;

    public static void main(String[] args) {
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
    }

    public Home() {
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
        lblNewLabel.setIcon(new ImageIcon("C:\\Users\\User\\eclipse-workspace\\SAMS\\src\\img\\Group 1.png"));

        JButton btnNewButton = new JButton("Home");
        btnNewButton.setBounds(27, 259, 117, 23);
        panel.add(btnNewButton);

        JButton btnDashboard = new JButton("Dashboard");
        btnDashboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            Dashboard dashboardWindow = new Dashboard(model);

                            LocalDateTime currentDate = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String formattedDate = currentDate.format(formatter);
                            dashboardWindow.displayAttendanceData(model, formattedDate);
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
                            frame.dispose(); // Close the current window
                        } catch (Exception ex) {
                            ex.printStackTrace();
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

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(195, 83, 641, 401);
        frame.getContentPane().add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        table.setBackground(new Color(192, 192, 192));

        model = new DefaultTableModel(
            new Object[][]{},
            new String[]{"List of students", "Attendance"}
        );

        try {
            String url = "jdbc:mysql://localhost:3306/sams";
            String username = "root";
            String password = "";
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "SELECT student_name FROM students";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String studentName = rs.getString("student_name");
                model.addRow(new Object[]{studentName, ""});
            }
            rs.close();
            stmt.close();
            conn.close();
            table.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred while retrieving student names: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Present");
        comboBox.addItem("Absent");
        comboBox.addItem("Late");
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));

        JButton btnNewButton_1 = new JButton("Save");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                boolean hasMissingAttendance = false;

                for (int i = 0; i < model.getRowCount(); i++) {
                    String attendance = (String) model.getValueAt(i, 1);
                    if (attendance == null || attendance.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter attendance for all students.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                        hasMissingAttendance = true;
                        break;
                    }
                }

                if (hasMissingAttendance) {
                    return;
                }
                try {

                    String url = "jdbc:mysql://localhost:3306/sams";
                    String username = "root";
                    String password = "";
                    Connection conn = DriverManager.getConnection(url, username, password);

                    String sql = "INSERT INTO attendance (student_name, attendance, time_added) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);

                    for (int i = 0; i < model.getRowCount(); i++) {
                        String studentName = (String) model.getValueAt(i, 0);
                        String attendance = (String) model.getValueAt(i, 1);
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        String date = String.valueOf(currentDateTime);

                        stmt.setString(1, studentName);
                        stmt.setString(2, attendance);
                        stmt.setString(3, date);

                        stmt.executeUpdate();
                    }

                    stmt.close();
                    conn.close();

                    JOptionPane.showMessageDialog(null, "Data saved successfully!");
                    createNewTable(); // Add this line to create a new table with the current date

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "An error occurred while saving data to the database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }

            private void createNewTable() {
                JTable newTable = new JTable();
                newTable.setModel(new DefaultTableModel(
                    new Object[][] {},
                    new String[] { "List of students", "Attendance" }
                ));
                JScrollPane newTableScrollPane = new JScrollPane(newTable);
                newTableScrollPane.setBounds(195, 83, 641, 401);
                frame.getContentPane().add(newTableScrollPane);
            }
        });

        btnNewButton_1.setBounds(471, 495, 89, 23);
        frame.getContentPane().add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Clear Attendance");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowCount = model.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    model.setValueAt("", i, 1);
                }
            }
        });
        btnNewButton_2.setBounds(699, 48, 137, 23);
        frame.getContentPane().add(btnNewButton_2);

        JButton btnAddRow = new JButton("Add Students\r\n");
        btnAddRow.setBounds(421, 49, 107, 23);
        frame.getContentPane().add(btnAddRow);

        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getRowCount() > 0) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String studentName = (String) model.getValueAt(selectedRow, 0);
                        String attendance = (String) model.getValueAt(selectedRow, 1);
                        try {

                            String url = "jdbc:mysql://localhost:3306/sams";
                            String username = "root";
                            String password = "";
                            Connection conn = DriverManager.getConnection(url, username, password);

                            String deleteStudentSQL = "DELETE FROM students WHERE student_name = ?";
                            PreparedStatement deleteStudentStmt = conn.prepareStatement(deleteStudentSQL);
                            deleteStudentStmt.setString(1, studentName);

                            deleteStudentStmt.executeUpdate();
                            deleteStudentStmt.close();

                            String deleteAttendanceSQL = "DELETE FROM attendance WHERE student_name = ? AND attendance = ?";
                            PreparedStatement deleteAttendanceStmt = conn.prepareStatement(deleteAttendanceSQL);
                            deleteAttendanceStmt.setString(1, studentName);
                            deleteAttendanceStmt.setString(2, attendance);

                            deleteAttendanceStmt.executeUpdate();
                            deleteAttendanceStmt.close();

                            conn.close();

                            model.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(frame, "Row deleted successfully.", "Delete Row", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(frame, "An error occurred while deleting data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Please select a row to delete.", "Delete Row", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        btnNewButton_3.setBounds(550, 49, 132, 23);
        frame.getContentPane().add(btnNewButton_3);

        // ActionListener for adding a new student
        btnAddRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newStudentName = JOptionPane.showInputDialog(frame, "Enter new student's name:");
                if (newStudentName != null && !newStudentName.isEmpty()) {
                    try {
                        String url = "jdbc:mysql://localhost:3306/sams";
                        String username = "root";
                        String password = "";
                        Connection conn = DriverManager.getConnection(url, username, password);
                        String sql = "INSERT INTO students (student_name) VALUES (?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, newStudentName);
                        stmt.executeUpdate();
                        stmt.close();
                        conn.close();

                        model.addRow(new Object[]{newStudentName, ""});
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame, "An error occurred while adding new student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

    }
}
