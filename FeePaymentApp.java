import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/** Task 1: UI collects data; FeeService owns validation and payment rules. */
public class FeePaymentApp extends JFrame {
    private final JTextField studentId = new JTextField(14);
    private final JTextField amount = new JTextField(14);
    private final JLabel status = new JLabel("Enter payment details.");
    private final FeeService service = new FeeService();

    public FeePaymentApp() {
        super("Student Fee Payment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        form.add(new JLabel("Student ID:")); form.add(studentId);
        form.add(new JLabel("Amount (INR):")); form.add(amount);

        JButton pay = new JButton("Pay Fee");
        JButton receipt = new JButton("View Receipt");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actions.add(pay); actions.add(receipt);

        pay.addActionListener(e -> processPayment());
        receipt.addActionListener(e -> showReceipt());

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        status.setBorder(BorderFactory.createEmptyBorder(0, 15, 12, 15));
        add(status, BorderLayout.NORTH);
        setSize(420, 240);
        setLocationRelativeTo(null);
    }

    private void processPayment() {
        try {
            FeeService.PaymentReceipt result =
                service.pay(studentId.getText().trim(), new BigDecimal(amount.getText().trim()));
            status.setText("Payment successful. Receipt: " + result.receiptId());
            JOptionPane.showMessageDialog(this, "Payment successful.\nReceipt: " +
                result.receiptId() + "\nAmount: ₹" + result.amount());
        } catch (IllegalArgumentException ex) {
            status.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid payment",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showReceipt() {
        JOptionPane.showMessageDialog(this, service.lastReceiptText(),
            "Latest Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeePaymentApp().setVisible(true));
    }
}

class FeeService {
    private PaymentReceipt lastReceipt;

    record PaymentReceipt(String receiptId, String studentId, BigDecimal amount) {}

    public PaymentReceipt pay(String studentId, BigDecimal amount) {
        if (studentId == null || !studentId.matches("[A-Za-z0-9-]{3,20}"))
            throw new IllegalArgumentException("Enter a valid student ID (3–20 letters, digits or hyphens).");
        if (amount == null || amount.signum() <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero.");
        if (amount.scale() > 2)
            throw new IllegalArgumentException("Amount can have at most two decimal places.");
        // Demo only: replace this line with a secure payment gateway / DAO transaction.
        lastReceipt = new PaymentReceipt("FEE" + System.currentTimeMillis(), studentId, amount);
        return lastReceipt;
    }

    public String lastReceiptText() {
        if (lastReceipt == null) return "No payment has been made in this session.";
        return "Receipt: " + lastReceipt.receiptId() + "\nStudent: " +
            lastReceipt.studentId() + "\nAmount: ₹" + lastReceipt.amount();
    }
}
