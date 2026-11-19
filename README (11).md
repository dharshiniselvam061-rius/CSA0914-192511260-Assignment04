# CSA09-192511260-Assignment4

**Student:** S Dharshini  
**Register Number:** 192511260  
**Course:** Programming in Java – CO4 (Event-Driven GUI Design)  
**Java:** Java 21 recommended (records and modern switch expressions are used)

## Repository structure

- `Task01` – Fee payment GUI review/redesign (`FeePaymentApp.java`)
- `Task02` – Hospital appointment desk (`AppointmentDesk.java`)
- `Task03` – Campus library search and responsive layout (`LibrarySearch.java`)
- `Task04` – Mouse drawing pad and repainting (`DrawingPad.java`)
- `Task05` – Currency converter and listener styles (`CurrencyConverterStyles.java`)
- `Task06` – Swing + MySQL event registration (`EventRegistrationApp.java`, `schema.sql`)

## Compile and run

From the repository root, compile each standalone task:

```bash
javac Task01/FeePaymentApp.java
java -cp Task01 FeePaymentApp

javac Task02/AppointmentDesk.java
java -cp Task02 AppointmentDesk

javac Task03/LibrarySearch.java
java -cp Task03 LibrarySearch

javac Task04/DrawingPad.java
java -cp Task04 DrawingPad

javac Task05/CurrencyConverterStyles.java
java -cp Task05 CurrencyConverterStyles
```

### Task 6 (MySQL)

1. Run `mysql -u root -p < Task06/schema.sql`.
2. Add MySQL Connector/J to the classpath.
3. Set `DB_URL`, `DB_USER`, and `DB_PASSWORD`.
4. Compile and run (replace the connector JAR path):

```bash
javac -cp ".;mysql-connector-j-8.x.x.jar" Task06/EventRegistrationApp.java
java -cp "Task06;mysql-connector-j-8.x.x.jar" EventRegistrationApp
```

On macOS/Linux, use `:` instead of `;` in the classpath.

## Task notes

- Task 1 separates Swing UI from `FeeService`; payment processing is simulated, not connected to a real payment gateway.
- Task 2 uses a preloaded `Map` of doctors to refresh the doctor list without a database request on each department change.
- Task 3 uses nested `BorderLayout` and `FlowLayout`; the centre scroll pane expands with the window.
- Task 4 stores `Point` objects and repaints them in `paintComponent`, so dots remain after minimizing/restoring.
- Task 5 demonstrates the lambda listener in the working converter. The other three listener styles and comparison patterns are documented in `Task05/README.md`.
- Task 6 uses prepared statements, a unique constraint, paginated `LIMIT`/`OFFSET`, and `SwingWorker` for database operations.

## Evidence still to capture

Run the GUIs on a desktop and place genuine screenshots in the corresponding task folder. Capture the required states:
- Task 1: successful payment and invalid amount/ID.
- Task 2: department change, doctor refresh, successful booking and invalid date.
- Task 3: three window sizes showing results region resizing.
- Task 4: dots/counter before and after minimizing/restoring.
- Task 5: valid conversion and invalid amount dialog; document the four styles.
- Task 6: successful registration, invalid input, duplicate registration, filtered table and CSV export.

## GitHub workflow

Create `CSA09-192511260-Assignment4` and commit each task progressively:

```bash
git init
git add README.md Task01 && git commit -m "Complete Task 1 fee payment GUI"
git add Task02 && git commit -m "Complete Task 2 appointment event design"
git add Task03 && git commit -m "Complete Task 3 library layout"
git add Task04 && git commit -m "Complete Task 4 event handling forensics"
git add Task05 && git commit -m "Complete Task 5 listener styles"
git add Task06 && git commit -m "Complete Task 6 event registration JDBC app"
```

Add your GitHub URL to the report before submission. Do not claim screenshots or database tests are complete until you have run and captured them locally.
