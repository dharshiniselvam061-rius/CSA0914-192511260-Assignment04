# Task 5 – Same Screen, Different Event Styles

`CurrencyConverterStyles.java` contains four working converter tabs. Each tab supports clicking Convert and pressing Enter in the amount field. Each conversion displays a result label and dialog. The window close event is handled with `WindowAdapter`.

## Implemented styles
1. **Frame implements ActionListener:** the JFrame implements `ActionListener`; both button and text field register the frame (`this`).
2. **Named inner class:** `NamedConvertHandler` implements `ActionListener` and stores a reference to its converter panel.
3. **Anonymous inner class:** an inline anonymous `ActionListener` handles the selected panel.
4. **Lambda expression:** `e -> convert(lambdaStyle)` is used for the lambda tab.

## Comparison
| Style | Code size | Field access | Reuse | Ease of change |
|---|---|---|---|---|
| Frame implements | Low for one action; grows as actions grow | Direct | Low/medium | Frame can become crowded |
| Named inner | Medium | Via enclosing instance and stored panel | High | Best for substantial reusable logic |
| Anonymous inner | Verbose | Lexical access | Low | Local but less reusable |
| Lambda | Shortest | Lexical access | Low unless extracted | Best for concise handlers |

A method reference (`e -> convert(panel)` cannot be a direct method reference because the panel argument is required; a helper bound to a panel can be referenced) or a shared Swing `Action` can be used when an action is reused by buttons, menus and keyboard shortcuts.

The exchange rates in this demonstration are illustrative only (USD 83 INR, EUR 90 INR), not live financial data.
