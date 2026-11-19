# Task 1 – GUI Design Review

## Diagnosed design problems
1. One listener for every button: creates conditional branching and makes changes harder; use separate listeners or Swing `Action` objects.
2. All payment logic inside the event handler: mixes presentation and business rules; move validation/payment into `FeeService`.
3. Comparing strings with `==`: compares object references; use `.equals()` or `.equalsIgnoreCase()`.
4. Missing input validation: empty IDs, non-numeric amounts and non-positive values can cause failures; validate at the service boundary.
5. Database/payment work on the EDT: freezes the window; use `SwingWorker` for slow operations.
6. Calling `Thread.sleep()` in an event handler: blocks repainting and user input; use a background worker/timer.
7. SQL concatenation: permits SQL injection; use `PreparedStatement`.
8. Resources not closed: can leak database connections/statements; use try-with-resources.
9. Weak exception handling/user feedback: show safe actionable messages and log technical details separately.
10. Receipt button has no defined action: connect it to a receipt view/export action.

## Class structure
`FeePaymentApp (JFrame) -> FeeService -> PaymentReceipt`
The sample simulates payment. A production system must use a payment gateway and transactional persistence.

## Shared listener vs separate listeners
A shared listener centralizes common handling but often needs source-based branching. Separate listener objects or `Action` instances improve cohesion and reuse. For two simple actions, concise lambdas are readable.

## Design justification (under 100 words)
The JFrame is responsible only for collecting input and displaying results. FeeService validates the student identifier and amount and performs the payment operation. This separation makes business rules testable without opening a window and prevents event handlers from becoming large. Input is validated before processing, failures are shown as clear messages, and receipt display is a separate action. Real database or gateway work should run off the Event Dispatch Thread, use prepared statements and close resources deterministically.
