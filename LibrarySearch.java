import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Task 3: Nested BorderLayout/FlowLayout panels; centre results grow on resize. */
public class LibrarySearch extends JFrame {
    private final JTextField query = new JTextField(22);
    private final JComboBox<String> category = new JComboBox<>(new String[]{"All", "Books", "Journals", "E-books"});
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> results = new JList<>(model);
    private final JLabel status = new JLabel("Ready.");

    public LibrarySearch() {
        super("Campus Library Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JLabel title = new JLabel("Campus Library", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setBorder(BorderFactory.createEmptyBorder(12, 8, 8, 8));
        add(title, BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.add(new JLabel("Search:")); searchRow.add(query);
        searchRow.add(category);
        JButton search = new JButton("Search"); searchRow.add(search);
        JPanel top = new JPanel(new BorderLayout());
        top.add(searchRow, BorderLayout.CENTER);
        add(top, BorderLayout.PAGE_START);

        results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        results.setBorder(BorderFactory.createTitledBorder("Search Results"));
        add(new JScrollPane(results), BorderLayout.CENTER);

        JButton prev = new JButton("Prev"), next = new JButton("Next"), reserve = new JButton("Reserve");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(prev); buttons.add(next); buttons.add(reserve);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(buttons, BorderLayout.CENTER);
        status.setBorder(BorderFactory.createEmptyBorder(5, 10, 8, 10));
        bottom.add(status, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        search.addActionListener(e -> doSearch());
        query.addActionListener(e -> doSearch());
        reserve.addActionListener(e -> {
            String selected = results.getSelectedValue();
            status.setText(selected == null ? "Select a result first." : "Reserved: " + selected);
        });
        prev.addActionListener(e -> status.setText("Previous page (demo)."));
        next.addActionListener(e -> status.setText("Next page (demo)."));

        setSize(650, 430); setMinimumSize(new Dimension(420, 300)); setLocationRelativeTo(null);
        doSearch();
    }

    private void doSearch() {
        model.clear();
        String q = query.getText().trim().toLowerCase();
        List<String> sample = List.of("Java: The Complete Reference", "Database System Concepts",
            "Clean Code", "Artificial Intelligence: A Modern Approach", "Data Mining Essentials");
        sample.stream().filter(s -> q.isEmpty() || s.toLowerCase().contains(q)).forEach(model::addElement);
        status.setText(model.size() + " result(s) found in " + category.getSelectedItem() + ".");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearch().setVisible(true));
    }
}
