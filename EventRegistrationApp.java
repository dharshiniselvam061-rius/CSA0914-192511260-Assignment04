import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.sql.*;
import java.util.Vector;

/**
 * Task 6: Swing + JDBC event registration.
 * Configure MySQL credentials with environment variables DB_URL, DB_USER, DB_PASSWORD.
 * MySQL Connector/J must be on the classpath.
 */
public class EventRegistrationApp extends JFrame {
    private final JTextField studentId = new JTextField(12);
    private final JTextField studentName = new JTextField(14);
    private final JComboBox<String> event = new JComboBox<>(new String[]{"TechFest", "CulturalFest", "SportsMeet"});
    private final JComboBox<String> filter = new JComboBox<>(new String[]{"All", "TechFest", "CulturalFest", "SportsMeet"});
    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID", "Student ID", "Student Name", "Event", "Registered At"}, 0);
    private final JTable table = new JTable(model);
    private final RegistrationService service = new RegistrationService();

    public EventRegistrationApp() {
        super("Campus Event Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem export = new JMenuItem("Export CSV");
        JMenuItem exit = new JMenuItem("Exit");
        export.addActionListener(e -> exportCsv());
        exit.addActionListener(e -> dispose());
        file.add(export); file.add(exit); bar.add(file); setJMenuBar(bar);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Student ID:")); form.add(studentId);
        form.add(new JLabel("Name:")); form.add(studentName);
        form.add(new JLabel("Event:")); form.add(event);
        JButton register = new JButton("Register"); form.add(register);
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter event:")); filterPanel.add(filter);
        JButton apply = new JButton("Apply Filter"); filterPanel.add(apply);
        JPanel north = new JPanel(new BorderLayout()); north.add(form, BorderLayout.NORTH); north.add(filterPanel, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        register.addActionListener(e -> registerAsync());
        apply.addActionListener(e -> loadAsync());
        loadAsync();
        setSize(850, 480); setLocationRelativeTo(null);
    }

    private void registerAsync() {
        String id = studentId.getText().trim(), name = studentName.getText().trim();
        String ev = (String) event.getSelectedItem();
        if (!id.matches("[A-Za-z0-9-]{3,20}") || name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter a valid student ID and name.", "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception { service.register(id, name, ev); return null; }
            protected void done() {
                try { get(); JOptionPane.showMessageDialog(EventRegistrationApp.this, "Registration successful."); loadAsync(); }
                catch (Exception ex) { JOptionPane.showMessageDialog(EventRegistrationApp.this,
                    rootMessage(ex), "Registration failed", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void loadAsync() {
        String ev = (String) filter.getSelectedItem();
        new SwingWorker<Vector<Vector<Object>>, Void>() {
            protected Vector<Vector<Object>> doInBackground() throws Exception { return service.list(ev, 0, 100); }
            protected void done() {
                try {
                    Vector<Vector<Object>> rows = get(); model.setRowCount(0);
                    for (Vector<Object> row : rows) model.addRow(row);
                } catch (Exception ex) { JOptionPane.showMessageDialog(EventRegistrationApp.this,
                    rootMessage(ex), "Load failed", JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                if (c > 0) writer.append(',');
                writer.append(model.getColumnName(c));
            }
            writer.append('\n');
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < model.getColumnCount(); c++) {
                    if (c > 0) writer.append(',');
                    writer.append('"').append(String.valueOf(model.getValueAt(r, c)).replace("\"", "\"\"")).append('"');
                }
                writer.append('\n');
            }
            JOptionPane.showMessageDialog(this, "CSV exported.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Export failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String rootMessage(Throwable t) {
        while (t.getCause() != null) t = t.getCause();
        return t.getMessage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventRegistrationApp().setVisible(true));
    }
}

class RegistrationService {
    private static final String URL = System.getenv().getOrDefault("DB_URL",
        "jdbc:mysql://localhost:3306/campus_events");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    void register(String studentId, String name, String event) throws SQLException {
        String sql = "INSERT INTO registrations(student_id, student_name, event_name) VALUES(?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, studentId); ps.setString(2, name); ps.setString(3, event);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw new SQLException("This student is already registered for this event.", ex);
        }
    }

    Vector<Vector<Object>> list(String event, int offset, int limit) throws SQLException {
        Vector<Vector<Object>> rows = new Vector<>();
        boolean all = "All".equals(event);
        String sql = "SELECT id, student_id, student_name, event_name, registered_at " +
            "FROM registrations " + (all ? "" : "WHERE event_name=? ") +
            "ORDER BY registered_at DESC LIMIT ? OFFSET ?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            if (!all) ps.setString(i++, event);
            ps.setInt(i++, limit); ps.setInt(i, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("id")); row.add(rs.getString("student_id"));
                    row.add(rs.getString("student_name")); row.add(rs.getString("event_name"));
                    row.add(rs.getTimestamp("registered_at")); rows.add(row);
                }
            }
        }
        return rows;
    }
}
