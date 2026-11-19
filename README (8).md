# Task 2 – Requirement to Event-Driven Design

## Event table

| Event source | Event object | Listener interface | Action |
|---|---|---|---|
| Department JComboBox | ActionEvent | ActionListener | Refresh doctor choices from department Map |
| Book JButton | ActionEvent | ActionListener | Validate patient/date and request booking |
| Window close button | WindowEvent | WindowListener / WindowAdapter | Ask confirmation, then dispose |
| Patient JTextField | ActionEvent | ActionListener (optional) | Validate or submit on Enter |

## Component containment tree

`AppointmentDesk (JFrame, BorderLayout)`
- `form JPanel (GridLayout)`
  - patient `JTextField`
  - department `JComboBox`
  - doctor `JComboBox`
  - date `JTextField`
  - book `JButton`
- status `JLabel` in SOUTH

## Pseudocode: refresh doctors
```text
on department ActionEvent:
    selectedDepartment = departmentCombo.getSelectedItem()
    doctorCombo.removeAllItems()
    for each doctor in doctorMap[selectedDepartment]:
        doctorCombo.addItem(doctor)
```

## Pseudocode: validate and book
```text
on Book ActionEvent:
    if patient name is blank or no doctor selected:
        show validation error
        return
    parse date
    if date is invalid or in the past:
        show date error
        return
    call AppointmentService.book(patient, doctor, date)
    show confirmation
```

## Window method-call trace
1. `main` calls `SwingUtilities.invokeLater`.
2. Constructor creates components, registers listeners and packs frame.
3. `setVisible(true)` displays the frame.
4. User interaction dispatches an event on the EDT.
5. Registered listener invokes its handler.
6. User clicks close; `windowClosing` runs.
7. Confirmation accepted; `dispose()` releases the window.

## Window vs applet lifecycle
A Swing window is created by application code, shown with `setVisible`, receives events, and is closed/disposed. Applet lifecycle methods were `init()`, `start()`, `paint()`, `stop()` and `destroy()`; applets are obsolete and are not the lifecycle model for a modern desktop Swing application.

## Database each time vs Map
Querying the database on every department change provides fresher data but adds network/database latency and load. Loading doctors once into a Map gives expected O(1) department lookup plus O(d) list population for d doctors, but the cache can become stale and needs refresh/invalidation.
