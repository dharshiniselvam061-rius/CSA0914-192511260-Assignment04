import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** Task 5: Four fully working listener styles in separate converter tabs. */
public class CurrencyConverterStyles extends JFrame implements ActionListener {
    private final ConverterPanel frameStyle = new ConverterPanel("Frame implements ActionListener");
    private final ConverterPanel namedStyle = new ConverterPanel("Named inner class");
    private final ConverterPanel anonymousStyle = new ConverterPanel("Anonymous inner class");
    private final ConverterPanel lambdaStyle = new ConverterPanel("Lambda expression");

    public CurrencyConverterStyles() {
        super("Currency Converter – Four Event Styles");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Frame implements", frameStyle);
        tabs.addTab("Named inner", namedStyle);
        tabs.addTab("Anonymous", anonymousStyle);
        tabs.addTab("Lambda", lambdaStyle);
        add(tabs);
        // Frame implements ActionListener style
        frameStyle.convert.addActionListener(this);
        frameStyle.amount.addActionListener(this);

        // Named inner class style
        ActionListener namedHandler = new NamedConvertHandler(namedStyle);
        namedStyle.convert.addActionListener(namedHandler);
        namedStyle.amount.addActionListener(namedHandler);

        // Anonymous inner class style
        ActionListener anonymousHandler = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { convert(anonymousStyle); }
        };
        anonymousStyle.convert.addActionListener(anonymousHandler);
        anonymousStyle.amount.addActionListener(anonymousHandler);

        // Lambda expression style
        lambdaStyle.convert.addActionListener(e -> convert(lambdaStyle));
        lambdaStyle.amount.addActionListener(e -> convert(lambdaStyle));

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(CurrencyConverterStyles.this,
                    "Exit converter?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    dispose();
            }
        });
        setSize(720, 250);
        setLocationRelativeTo(null);
    }

    @Override public void actionPerformed(ActionEvent e) {
        convert(frameStyle);
    }

    private class NamedConvertHandler implements ActionListener {
        private final ConverterPanel panel;
        NamedConvertHandler(ConverterPanel panel) { this.panel = panel; }
        @Override public void actionPerformed(ActionEvent e) { convert(panel); }
    }

    private void convert(ConverterPanel panel) {
        try {
            double value = Double.parseDouble(panel.amount.getText().trim());
            if (!Double.isFinite(value) || value < 0) throw new NumberFormatException();
            String f = (String) panel.from.getSelectedItem();
            String t = (String) panel.to.getSelectedItem();
            double inr = switch (f) {
                case "USD" -> value * 83.0;
                case "EUR" -> value * 90.0;
                default -> value;
            };
            double converted = switch (t) {
                case "USD" -> inr / 83.0;
                case "EUR" -> inr / 90.0;
                default -> inr;
            };
            String message = String.format("%.2f %s = %.2f %s", value, f, converted, t);
            panel.result.setText(message);
            JOptionPane.showMessageDialog(this, message, "Conversion Result", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            panel.result.setText("Invalid amount. Enter a non-negative number.");
            JOptionPane.showMessageDialog(this, panel.result.getText(), "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class ConverterPanel extends JPanel {
        final JTextField amount = new JTextField("100", 8);
        final JComboBox<String> from = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
        final JComboBox<String> to = new JComboBox<>(new String[]{"INR", "USD", "EUR"});
        final JButton convert = new JButton("Convert");
        final JLabel result = new JLabel("Enter an amount and convert.");

        ConverterPanel(String style) {
            super(new BorderLayout(8, 8));
            JPanel controls = new JPanel(new FlowLayout());
            controls.add(new JLabel("Amount:")); controls.add(amount);
            controls.add(new JLabel("From:")); controls.add(from);
            controls.add(new JLabel("To:")); controls.add(to);
            controls.add(convert);
            add(new JLabel(style, SwingConstants.CENTER), BorderLayout.NORTH);
            add(controls, BorderLayout.CENTER);
            result.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            add(result, BorderLayout.SOUTH);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CurrencyConverterStyles().setVisible(true));
    }
}
