# Grammar Solver

A recursive generator for **context-free grammars** in Java. Give it a grammar in BNF and a
symbol to expand, and it produces randomly generated strings that the grammar permits.

Written for CPT_S 123 (data structures), kept here as a small, self-contained example of
recursive expansion over a rule table.

---

## Grammar format

One rule per line, `symbol ::= alternative | alternative | …`, whitespace ignored:

```
num::=1|2|3|4|5|6|7|8|9

op ::= +|-|*|/

exp::= num |exp op num
```

Any symbol appearing on the left of a `::=` is a **nonterminal** and gets expanded
recursively. Everything else is a **terminal** and is emitted literally. Expanding `exp`
against the grammar above yields things like `7`, `3 + 5`, `2 * 8 - 1`.

Note that `exp ::= num | exp op num` is left-recursive: `exp` can expand back into itself.
Generation terminates because `num` is a competing alternative, so each expansion has a
chance of bottoming out — which is a probabilistic guarantee, not a structural one. A grammar
whose every alternative recursed would not terminate.

---

## Design

- Rules are parsed once into a `Map<String, String[]>` — nonterminal to its alternatives —
  so every lookup during generation is O(1).
- `generate(symbol)` checks the map. A miss means the symbol is terminal and is returned
  as-is; a hit picks a random alternative, splits it on whitespace, and recurses on each
  part, concatenating the results.
- Duplicate left-hand sides and malformed lines are rejected at parse time, so a bad grammar
  fails where the mistake is rather than somewhere deep in the recursion.

```
GrammarSolver.java   parsing, rule storage, recursive generation
GrammarMain.java     CLI — loads a grammar file, prompts for a symbol and a count
simple.txt           arithmetic expressions (above)
sentence.txt         English sentence grammar
sentence3.txt        a larger sentence grammar
```

---

## Running it

Requires JDK 8 or newer.

```bash
javac GrammarSolver.java GrammarMain.java
java GrammarMain
```

Then supply a grammar file (`simple.txt`), a symbol to expand (`exp`), and how many strings
to generate.

---

## Concepts

Recursion over a tree of expansions, `Map`-backed rule lookup, tokenizing structured text
input, and validating input at the boundary instead of trusting it inward.
