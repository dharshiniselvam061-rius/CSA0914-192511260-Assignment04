# Task 3 – From Sketch to Layout

## Nested panel structure and layout choices
```text
LibrarySearch JFrame – BorderLayout
├── Title JLabel – NORTH
├── Search row JPanel – FlowLayout(LEFT)
│   ├── JTextField
│   ├── JComboBox (category)
│   └── JButton (Search)
├── JScrollPane(JList results) – CENTER
└── Bottom JPanel – BorderLayout, SOUTH
    ├── JPanel FlowLayout (Prev / Next / Reserve)
    └── JLabel status
```

The central scroll pane receives remaining space and expands when the frame is resized. FlowLayout keeps the search controls and bottom buttons at their preferred sizes. BorderLayout provides the main regions.

## Test sizes
Capture screenshots at 650×430 (default), 900×600 (large), and 420×300 (minimum). Confirm the results area grows/shrinks while the control rows remain usable.

## Alternative layouts
GridBagLayout offers precise constraints and flexible resizing but needs more layout code. Absolute positioning (`null` layout) is fragile because component coordinates do not adapt to font, platform or window-size changes. Nested standard layouts are simpler and responsive for this screen.
