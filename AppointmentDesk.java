import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/** Task 2: Hospital appointment desk; doctor list refreshes from a department Map. */
public class AppointmentDesk extends JFrame {
    private final JComboBox<String> department = new JComboBox<>(new String[]{"Cardiology", "Dermatology", "Neurology"});
    private final JComboBox<String> doctor = new JComboBox<>();
    private final JTextField patient = new JTextField(16);
    private final JTextField date = new JTextField(LocalDate.now().plusDays(1).toString(), 10);
    private final JLabel status = new JLabel("Select a department and enter patient details.");

    private final Map<String, List<String>> doctors = Map.of(
        "Cardiology", List.of("Dr. Anand", "Dr. Meera"),
        "Dermatology", List.of("Dr. Karthik", "Dr. Nisha"),
        "Neurology", List.of("Dr. Ravi", "Dr. Fathima")
    );

    public AppointmentDesk() {
        super("Hospital Outpatient Appointment Desk");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(14, 14, 8, 14));
        form.add(new JLabel("Patient name:")); form.add(patient);
        form.add(new JLabel("Department:")); form.add(department);
        form.add(new JLabel("Doctor:")); form.add(doctor);
        form.add(new JLabel("Appointment date (YYYY-MM-DD):")); form.add(date);
        JButton book = new JButton("Book Appointment");
        form.add(new JLabel()); form.add(book);
        add(form, BorderLayout.CENTER);
        status.setBorder(BorderFactory.createEmptyBorder(0, 14, 12, 14));
        add(status, BorderLayout.SOUTH);

        department.addActionListener(e -> refreshDoctors());
        book.addActionListener(e -> bookAppointment());
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                int answer = JOptionPane.showConfirmDialog(AppointmentDesk.this,
                    "Close appointment desk?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) dispose();
            }
        });
        refreshDoctors();
        pack(); setMinimumSize(new Dimension(520, 260)); setLocationRelativeTo(null);
    }

    private void refreshDoctors() {
        doctor.removeAllItems();
        List<String> names = doctors.getOrDefault((String) department.getSelectedItem(), List.of());
        names.forEach(doctor::addItem);
    }

    private void bookAppointment() {
        String name = patient.getText().trim();
        if (name.isEmpty() || doctor.getSelectedItem() == null) {
            status.setText("Error: patient name and doctor are required."); return;
        }
        try {
            LocalDate selected = LocalDate.parse(date.getText().trim());
            if (selected.isBefore(LocalDate.now())) throw new IllegalArgumentException("Date cannot be in the past.");
            status.setText("Appointment booked for " + name + " with " +
                doctor.getSelectedItem() + " on " + selected + ".");
        } catch (Exception ex) {
            status.setText("Error: enter a valid future date (YYYY-MM-DD).");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentDesk().setVisible(true));
    }
}
