import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/** Task 4: Stores clicked points and redraws them after repaint/expose events. */
public class DrawingPad extends JFrame {
    private final JLabel counter = new JLabel("Clicks: 0");
    private final List<Point> points = new ArrayList<>();

    public DrawingPad() {
        super("Mouse Drawing Pad");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setBackground(Color.WHITE);
        canvas.setPreferredSize(new Dimension(560, 360));
        canvas.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                points.add(e.getPoint());
                counter.setText("Clicks: " + points.size());
                canvas.repaint();
            }
        });
        add(canvas, BorderLayout.CENTER);
        counter.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        add(counter, BorderLayout.SOUTH);
        pack(); setLocationRelativeTo(null);
    }

    private class DrawingCanvas extends JPanel {
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(25, 95, 180));
            for (Point p : points) g.fillOval(p.x - 5, p.y - 5, 10, 10);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DrawingPad().setVisible(true));
    }
}
