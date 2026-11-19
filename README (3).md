# Task 6 – Campus Event Registration

## Users, screens and data
- Student: enters student ID, name and event; registers once per event.
- Committee member: views registrations, filters by event and exports CSV.
- Data: registration ID, student ID, student name, event name, registration timestamp.

## Testable requirements
1. Valid registration is saved and displayed.
2. Blank/invalid student ID or blank name is rejected.
3. Duplicate student/event registration is rejected by the database unique constraint.
4. Committee member can filter registrations by event.
5. Current table data can be exported as CSV.
6. JDBC work is executed in SwingWorker, not on the EDT.

## Layering
`EventRegistrationApp (Swing UI) -> RegistrationService (business rules) -> JDBC/MySQL`
The SQL schema is in `schema.sql`. PreparedStatement prevents SQL injection. The unique key `(student_id, event_name)` enforces duplicate prevention even if two requests race.

## Register pseudocode
```text
validate student ID and name on UI
start SwingWorker:
    service.register(id, name, event)
service:
    INSERT using PreparedStatement
    let unique constraint reject duplicate
on EDT when worker completes:
    show success or meaningful error
    refresh table asynchronously
```

## Filter pseudocode
```text
selectedEvent = filterCombo selection
start SwingWorker:
    SELECT registrations
    WHERE event_name = selectedEvent (unless All)
    ORDER BY registered_at DESC
    LIMIT pageSize OFFSET pageOffset
on EDT:
    replace table model rows
```

## Pagination vs loading 100,000 rows
Loading all 100,000 rows has O(n) transfer and memory cost and can delay initial display. Pagination with page size p transfers O(p) rows per request and uses O(p) client-side row memory, improving responsiveness; OFFSET pagination can become slower for large offsets, where keyset pagination may be preferable. Database work is offloaded with SwingWorker so the EDT remains responsive.

## SQL script
Run `schema.sql` before starting the application. Configure `DB_URL`, `DB_USER`, `DB_PASSWORD`, and include MySQL Connector/J on the classpath. The unique constraint is the final protection against duplicate registration.
